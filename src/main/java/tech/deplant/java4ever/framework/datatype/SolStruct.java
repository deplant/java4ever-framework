package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.utils.regex.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record SolStruct(Abi.AbiParam[] abiParams, Map<String, Object> values) implements AbiType<Map<String, Object>, Map<String, Object>> {

	private final static System.Logger logger = System.getLogger(SolStruct.class.getName());

	public static boolean hasParam(Abi.AbiParam[] params, String paramName) {
		return Arrays.stream(params)
		             .anyMatch(param ->
				                       paramName.equals(param.name()));
	}

	public static Abi.AbiParam getParam(Abi.AbiParam[] params, String paramName) {
		return Arrays.stream(params)
		             .filter(param ->
				                     paramName.equals(param.name()))
		             .findFirst()
		             .orElseThrow();
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
					logger.log(System.Logger.Level.ERROR, () ->
							"ABI params spec doesn't contain key '" + key + "'");
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
					logger.log(System.Logger.Level.ERROR, () ->
							"ABI params spec doesn't contain key '" + key + "'");
				}
			}
			return new SolStruct(params, converted);
		} else {
			return null;
		}
	}

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, "tuple", abiParams());
	}

	@Override
	public String abiTypeName() {
		return "tuple";
	}

	@Override
	public Map<String, Object> toJava() {
		return values();
	}

	@Override
	public Map<String, Object> toABI() {
		return values();
	}

	public static ContractAbi.AbiTypeDetails typeParser(String typeString) throws EverSdkException {
		// Size pattern matching
		var expr = new Then(
				new GroupOf(new Occurences(new AnyOf(new Word("a-zA-Z")), 1)),
				new GroupOf(new Occurences(Special.DIGIT, 1, 3)))
				.toPattern();
		var matcher = expr.matcher(typeString);
		while (matcher.find()) {
			return new ContractAbi.AbiTypeDetails(TypePrefix.valueOf(matcher.group(1).toUpperCase()),
			                                      Integer.parseInt(matcher.group(2))
					, arrayMatcher(typeString));
		}
		// Type pattern  matching
		matcher = Pattern.compile("([a-zA-Z]+)").matcher(typeString);
		while (matcher.find()) {
			return new ContractAbi.AbiTypeDetails(TypePrefix.valueOf(matcher.group(1).toUpperCase()),
			                                      0, arrayMatcher(typeString));
		}
		var ex = new EverSdkException(new EverSdkException.ErrorResult(-300,
		                                                               "ABI Type parsing failed! Type: " + typeString),
		                              new RuntimeException());
		logger.log(System.Logger.Level.WARNING, () -> ex.toString());
		throw ex;
	}

	public static boolean arrayMatcher(String typeString) {
		var arrayPattern = new Then(
				new GroupOf(
						new Then(
								new Occurences(new AnyOf(new Word("a-zA-Z")), 1),
								new Occurences(Special.DIGIT, 0, 3)
						)
				),
				new GroupOf(new Then(new Symbol('['), new Symbol(']'))))
				.toPattern();
		var matcher = arrayPattern.matcher(typeString);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	public static Object serializeInputTree(Abi.AbiParam param, Object inputValue) throws EverSdkException {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" +
		                                 typeStringPattern +
		                                 "(,)" +
		                                 typeStringPattern +
		                                 "(\\))");
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
			final var keyDetails = typeParser(keyTypeString);

			Map<Object, Object> mapValue = (Map<Object, Object>) inputValue;
			if (mapValue.size() == 1) {
				final Object key = mapValue.keySet().toArray()[0];
				final Object value = mapValue.values().toArray()[0];
				return Map.of(AbiType.of(keyDetails.type(), keyDetails.size(), key).toABI(),
				              // serializeTree is used for map(type,tuple) cases,
				              // thus it will continue to serialize tuple part
				              serializeInputTree(new Abi.AbiParam(valueTypeString, valueTypeString, param.components()),
				                                 value));
			} else {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-302,
				                                                               "ABI Type Conversion fails. Wrong argument! Too many keys provided for single map(type,type) " +
				                                                               mapValue), new RuntimeException());
				logger.log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		} else {
			// Normal (not map) root types
			final var rootDetails = typeParser(rootTypeString);
			// tuples
			if (rootDetails.type().equals(TypePrefix.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) inputValue;
				return Arrays.stream(param.components()).collect(
						Collectors.toMap(component -> component.name(),
						                 component -> {
							                 try {
								                 return serializeInputTree(component, mapValue.get(component.name()));
							                 } catch (EverSdkException e) {
								                 // in the complex cases, if we can't serialize, we can try to put object as is
								                 return mapValue.get(component.name());
							                 }
						                 }
						));
				// arrays
			} else if (rootDetails.isArray()) {
				return switch (inputValue) {
					case String s -> new Object[]{AbiType.of(rootDetails.type(), rootDetails.size(), s).toABI()};
					case Object[] arr -> Arrays.stream(arr).map(element -> {
						try {
							return AbiType.of(rootDetails.type(), rootDetails.size(), element).toABI();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					case List list -> list.stream().map(element -> {
						try {
							return AbiType.of(rootDetails.type(), rootDetails.size(), element).toABI();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					default -> new Object[]{AbiType.of(rootDetails.type(),
					                                   rootDetails.size(),
					                                   inputValue).toABI()};
				};
			} else {
				// all others
				return AbiType.of(rootDetails.type(), rootDetails.size(), inputValue).toABI();
			}
		}
	}

	public static Object serializeOutputTree(Abi.AbiParam param, Object outputValue) throws EverSdkException {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" +
		                                 typeStringPattern +
		                                 "(,)" +
		                                 typeStringPattern +
		                                 "(\\))");
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
			final var keyDetails = typeParser(keyTypeString);

			Map<Object, Object> mapValue = (Map<Object, Object>) outputValue;
			if (mapValue.size() == 1) {
				final Object key = mapValue.keySet().toArray()[0];
				final Object value = mapValue.values().toArray()[0];
				return Map.of(AbiType.ofABI(keyDetails.type(), keyDetails.size(), key).toJava(),
				              // serializeTree is used for map(type,tuple) cases,
				              // thus it will continue to serialize tuple part
				              serializeOutputTree(new Abi.AbiParam(valueTypeString,
				                                                   valueTypeString,
				                                                   param.components()),
				                                  value));
			} else {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-302,
				                                                               "ABI Type Conversion fails. Wrong argument! Too many keys provided for single map(type,type) " +
				                                                               mapValue), new RuntimeException());
				logger.log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		} else {
			// Normal (not map) root types
			final var rootDetails = typeParser(rootTypeString);
			// tuples
			if (rootDetails.type().equals(TypePrefix.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) outputValue;
				return Arrays.stream(param.components()).collect(
						Collectors.toMap(component -> component.name(),
						                 component -> {
							                 try {
								                 return serializeOutputTree(component, mapValue.get(component.name()));
							                 } catch (EverSdkException e) {
								                 // in the complex cases, if we can't serialize, we can try to put object as is
								                 return mapValue.get(component.name());
							                 }
						                 }
						));
				// arrays
			} else if (rootDetails.isArray()) {
				return switch (outputValue) {
					case String s -> new Object[]{AbiType.ofABI(rootDetails.type(), rootDetails.size(), s).toJava()};
					case Object[] arr -> Arrays.stream(arr).map(element -> {
						try {
							return AbiType.ofABI(rootDetails.type(), rootDetails.size(), element).toJava();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					case List list -> list.stream().map(element -> {
						try {
							return AbiType.ofABI(rootDetails.type(), rootDetails.size(), element).toJava();
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					default -> new Object[]{AbiType.ofABI(rootDetails.type(),
					                                      rootDetails.size(),
					                                      outputValue).toJava()};
				};
			} else {
				// all others
				return AbiType.ofABI(rootDetails.type(), rootDetails.size(), outputValue).toJava();
			}
		}
	}
}