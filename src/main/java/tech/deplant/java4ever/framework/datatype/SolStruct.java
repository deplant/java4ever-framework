package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.commons.regex.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record SolStruct(Abi.AbiParam[] abiParams,
                        Map<String, Object> values) implements AbiValue<Map<String, Object>, Map<String, Object>> {

	private final static System.Logger logger = System.getLogger(SolStruct.class.getName());

	public static boolean hasParam(Abi.AbiParam[] params, String paramName) {
		return Arrays.stream(params).anyMatch(param -> paramName.equals(param.name()));
	}

	public static Abi.AbiParam getParam(Abi.AbiParam[] params, String paramName) {
		return Arrays.stream(params).filter(param -> paramName.equals(param.name())).findFirst().orElseThrow();
	}

	public static SolStruct fromJava(Abi.AbiParam[] params, Map<String, Object> inputs) {
		if (inputs != null) {
			Map<String, Object> converted = new HashMap<>();
			for (Map.Entry<String, Object> entry : inputs.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (hasParam(params, key)) {
					var abiParam = getParam(params, key);
					try {
						converted.put(key, serializeInputTree(abiParam, value));
					} catch (EverSdkException e) {
						// in the complex cases, if we can't serialize, we can try to put object as is
						converted.put(key, value);
					}
				} else {
					logger.log(System.Logger.Level.ERROR, () -> "ABI params spec doesn't contain key '" + key + "'");
				}
			}
			return new SolStruct(params, converted);
		} else {
			return null;
		}
	}

	public static SolStruct fromABI(Abi.AbiParam[] params, Map<String, Object> outputs) {
		if (outputs != null) {
			Map<String, Object> converted = new HashMap<>();
			for (Map.Entry<String, Object> entry : outputs.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (hasParam(params, key)) {
					var abiParam = getParam(params, key);
					try {
						converted.put(key, serializeOutputTree(abiParam, value));
					} catch (EverSdkException e) {
						// in the complex cases, if we can't serialize, we can try to put object as is
						converted.put(key, value);
					}
				} else {
					logger.log(System.Logger.Level.ERROR, () -> "ABI params spec doesn't contain key '" + key + "'");
				}
			}
			return new SolStruct(params, converted);
		} else {
			return null;
		}
	}

	public static boolean arrayMatcher(String typeString) {
		var arrayPattern = new Then(new GroupOf(new Then(new Occurences(new AnyOf(new Word("a-zA-Z")), 1),
		                                                 new Occurences(Special.DIGIT, 0, 3))),
		                            new GroupOf(new Then(new Symbol('['), new Symbol(']')))).toPattern();
		var matcher = arrayPattern.matcher(typeString);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	public static Object serializeInputTree(Abi.AbiParam param, Object inputValue) throws EverSdkException {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" + typeStringPattern + "(,)" + typeStringPattern + "(\\))");
		boolean rootIsMap = false;
		String rootTypeString = param.type();
		String keyTypeString = null;
		String valueTypeString = null;

		logger.log(System.Logger.Level.TRACE,
		           () -> "param: " + param.name() + " ( " + param.type() + " -> " + rootTypeString + " ): " +
		                 inputValue);
		var matcher = mapPattern.matcher(rootTypeString);
		while (matcher.find()) {
			rootIsMap = true;
			keyTypeString = matcher.group(2);
			valueTypeString = matcher.group(4);
		}

		// map = Map<String,Object> from types
		// map(type,tuple) = Map<String,Map<String,Object>>
		if (rootIsMap) {
			// Key Parse
			final var keyDetails = AbiType.of(keyTypeString);

			Map<Object, Object> inputMap = (Map<Object, Object>) inputValue;
			Map<Object, Object> convertedMap = new HashMap<>();
			for (var entry : inputMap.entrySet()) {
				convertedMap.put(AbiValue.of(keyDetails, entry.getKey()).jsonValue(),
				                 serializeInputTree(new Abi.AbiParam(valueTypeString,
				                                                     valueTypeString,
				                                                     param.components()), entry.getValue()));
			}
			return convertedMap;
		} else {
			// Normal (not map) root types
			final var rootDetails = AbiType.of(rootTypeString);
			// tuples
			if (rootDetails.prefix().equals(AbiTypePrefix.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) inputValue;
				return Arrays.stream(param.components())
				             .collect(Collectors.toMap(component -> component.name(), component -> {
					             try {
						             return serializeInputTree(component, mapValue.get(component.name()));
					             } catch (EverSdkException e) {
						             // in the complex cases, if we can't serialize, we can try to put object as is
						             return mapValue.get(component.name());
					             }
				             }));
				// arrays
			} else if (rootDetails.isArray()) {
				return switch (inputValue) {
					case String s -> new Object[]{AbiValue.of(rootDetails, s).jsonValue()};
					case Object[] arr -> Arrays.stream(arr).map(element -> {
						try {
							return AbiValue.of(rootDetails, element).jsonValue();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					case List list -> list.stream().map(element -> {
						try {
							return AbiValue.of(rootDetails, element).jsonValue();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					default -> new Object[]{AbiValue.of(rootDetails, inputValue).jsonValue()};
				};
			} else {
				// all others
				return AbiValue.of(rootDetails, inputValue).jsonValue();
			}
		}
	}

	public static Object serializeOutputTree(Abi.AbiParam param, Object outputValue) throws EverSdkException {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" + typeStringPattern + "(,)" + typeStringPattern + "(\\))");
		boolean rootIsMap = false;
		String rootTypeString = param.type();
		String keyTypeString = null;
		String valueTypeString = null;

		logger.log(System.Logger.Level.TRACE,
		           () -> "param: " + param.name() + " ( " + param.type() + " -> " + rootTypeString + " ): " +
		                 outputValue);
		var matcher = mapPattern.matcher(rootTypeString);
		while (matcher.find()) {
			rootIsMap = true;
			keyTypeString = matcher.group(2);
			valueTypeString = matcher.group(4);
		}

		// map = Map<String,Object> from types
		// map(type,tuple) = Map<String,Map<String,Object>>
		if (rootIsMap) {
			// Key Parse
			final var keyDetails = AbiType.of(keyTypeString);

			Map<Object, Object> outputMap = (Map<Object, Object>) outputValue;
			Map<Object, Object> convertedMap = new HashMap<>();
			for (var entry : outputMap.entrySet()) {
				convertedMap.put(AbiValue.ofABI(keyDetails, entry.getKey()).toJava(),
				                 serializeOutputTree(new Abi.AbiParam(valueTypeString,
				                                                      valueTypeString,
				                                                      param.components()), entry.getValue()));
			}
			return convertedMap;
		} else {
			// Normal (not map) root types
			final var rootDetails = AbiType.of(rootTypeString);
			// tuples
			if (rootDetails.prefix().equals(AbiTypePrefix.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) outputValue;
				return Arrays.stream(param.components())
				             .collect(Collectors.toMap(component -> component.name(), component -> {
					             try {
						             return serializeOutputTree(component, mapValue.get(component.name()));
					             } catch (EverSdkException e) {
						             // in the complex cases, if we can't serialize, we can try to put object as is
						             return mapValue.get(component.name());
					             }
				             }));
				// arrays
			} else if (rootDetails.isArray()) {
				return switch (outputValue) {
					case String s -> new Object[]{AbiValue.ofABI(rootDetails, s).toJava()};
					case Object[] arr -> Arrays.stream(arr).map(element -> {
						try {
							return AbiValue.ofABI(rootDetails, element).toJava();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					case List list -> list.stream().map(element -> {
						try {
							return AbiValue.ofABI(rootDetails, element).toJava();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					default ->
							new Object[]{AbiValue.ofABI(rootDetails, outputValue).toJava()};
				};
			} else {
				// all others
				return AbiValue.ofABI(rootDetails, outputValue).toJava();
			}
		}
	}

	@Override
	public Map<String, Object> toJava() {
		return values();
	}

	@Override
	public Map<String, Object> jsonValue() {
		return values();
	}
}
