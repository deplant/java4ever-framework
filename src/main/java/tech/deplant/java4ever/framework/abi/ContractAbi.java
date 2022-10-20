package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Holds entire ABI structure and has helper methods to check functions availability or
 * getting input types and so on. Use factory methods to create from File or JsonNode.
 *
 * @param abiVersion
 * @param version
 * @param header
 * @param data
 * @param fields
 * @param functions
 * @param events
 */
public record ContractAbi(@JsonProperty("ABI version") Integer abiVersion,
                          @JsonProperty("abi_version") Number abiVersionOld,
                          String version,
                          String[] header,
                          Abi.AbiData[] data,
                          Abi.AbiParam[] fields,
                          Abi.AbiFunction[] functions,
                          Abi.AbiEvent[] events
) {

	private static System.Logger logger = System.getLogger(ContractAbi.class.getName());

	public static ContractAbi ofString(String jsonString) throws JsonProcessingException {
		return ContextBuilder.DEFAULT_MAPPER.readValue(jsonString, ContractAbi.class);
	}

	public static ContractAbi ofResource(String resourceName) throws JsonProcessingException {
		return ofString(new JsonResource(resourceName).get());
	}

	public static ContractAbi ofFile(String filePath) throws JsonProcessingException {
		return ofString(new JsonFile(filePath).get());
	}

	public static ContractAbi ofJsonNode(JsonNode node) throws JsonProcessingException {
		return ofString(ContextBuilder.DEFAULT_MAPPER.writeValueAsString(node));
	}

	public String json() throws JsonProcessingException {
		return ContextBuilder.DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		                                    .writeValueAsString(this);
	}

	public boolean hasHeader(String name) {
		return Arrays.asList(header()).contains(name);
	}

	public boolean hasFunction(String name) {
		return Arrays.stream(functions()).anyMatch(function -> name.equals(function.name()));
	}

	public boolean hasInitDataParam(String initDataName) {
		return Arrays.stream(data())
		             .anyMatch(data ->
				                       initDataName.equals(data.name()));
	}

	public boolean hasInput(String functionName, String inputName) {
		return Arrays.stream(functions())
		             .anyMatch(function ->
				                       functionName.equals(function.name()) &&
				                       Arrays.stream(function.inputs())
				                             .anyMatch(input -> inputName.equals(input.name())));
	}

	public boolean hasOutput(String functionName, String outputName) {
		return Arrays.stream(functions())
		             .anyMatch(function ->
				                       functionName.equals(function.name()) &&
				                       Arrays.stream(function.outputs())
				                             .anyMatch(output -> outputName.equals(output.name())));
	}

	private Abi.AbiFunction findFunction(Abi.AbiFunction[] functionArr, String name) {
		return Arrays.stream(functionArr).filter(function -> name.equals(function.name())).findAny().orElseThrow();
	}

	private Abi.AbiEvent findEvent(Abi.AbiEvent[] functionArr, String name) {
		return Arrays.stream(functionArr).filter(event -> name.equals(event.name())).findAny().orElseThrow();
	}

	private Abi.AbiParam findParam(Abi.AbiParam[] paramArr, String name) {
		return Arrays.stream(paramArr).filter(input -> name.equals(input.name())).findAny().orElseThrow();
	}

	public Abi.AbiParam functionInputType(String functionName, String inputName) {
		return findParam(findFunction(functions(), functionName).inputs(), inputName);
	}

	public Abi.AbiParam initDataType(String initDataName) {
		var dataParam = Arrays.stream(data())
		                      .filter(data ->
				                              initDataName.equals(data.name())).findAny().orElseThrow();
		return new Abi.AbiParam(dataParam.name(), dataParam.type(), dataParam.components());
	}

	public Abi.AbiParam functionOutputType(String functionName, String outputName) {
		return findParam(findFunction(functions(), functionName).outputs(), outputName);
	}

	public Abi.AbiParam eventInputType(String functionName, String inputName) {
		return findParam(findEvent(events(), functionName).inputs(), inputName);
	}

	public Abi.ABI ABI() {
		try {
			return new Abi.ABI.Json(json());
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR, () -> "This JsonAbi can't be strigified!" + e.getMessage());
			return new Abi.ABI.Json("{}");
		}
	}

	protected Object serializeSingle(AbiValueType type, int size, Object inputValue) throws EverSdkException {
		return switch (type) {
			case UINT, INT -> switch (inputValue) {
				case BigInteger b -> new AbiUint(b).serialize();
				case Long l -> new AbiUint(l).serialize();
				case Instant i -> new AbiUint(i).serialize();
				case String strPrefixed
						when strPrefixed.length() >= 2 && "0x".equals(strPrefixed.substring(0, 2)) ->
						new AbiUint(new BigInteger(strPrefixed.substring(2), 16)).serialize();
				case String str -> new AbiUint(size, new BigInteger(str, 16)).serialize();
				default -> inputValue;
			};
			case STRING, BYTES, BYTE -> switch (inputValue) {
				case String s -> new AbiString(s).serialize();
				default -> inputValue.toString();
			};
			case ADDRESS -> switch (inputValue) {
				case Address a -> new AbiAddress(a).serialize();
				case String s -> new AbiAddress(s).serialize();
				default -> inputValue;
			};
			case BOOL -> switch (inputValue) {
				case Boolean b -> b;
				default -> inputValue;
			};
			case CELL, SLICE, BUILDER -> switch (inputValue) {
				case String s -> s;
				case AbiTvmCell abiCell -> abiCell.serialize();
				default -> inputValue;
			};
			case TUPLE -> {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-301,
				                                                               "ABI Parsing unexpected! Shouldn't get here!"),
				                              new RuntimeException());
				logger.log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		};
	}

	protected TypePair typeParser(String typeString) throws EverSdkException {
		var sizePattern = Pattern.compile("([a-zA-Z]+)(\\d{1,3})");
		var matcher = sizePattern.matcher(typeString);
		while (matcher.find()) {
			return new TypePair(AbiValueType.valueOf(matcher.group(1).toUpperCase()),
			                    Integer.parseInt(matcher.group(2)));
		}
		var typePattern = Pattern.compile("([a-zA-Z]+)");
		matcher = typePattern.matcher(typeString);
		while (matcher.find()) {
			return new TypePair(AbiValueType.valueOf(matcher.group(1).toUpperCase()),
			                    0);
		}
		var ex = new EverSdkException(new EverSdkException.ErrorResult(-300,
		                                                               "ABI Type parsing failed! Type: " + typeString),
		                              new RuntimeException());
		logger.log(System.Logger.Level.WARNING, () -> ex.toString());
		throw ex;
	}

	protected boolean arrayMatcher(String typeString) {
		var arrayPattern = Pattern.compile("([a-zA-Z]+\\d{0,3})(\\[\\])");
		var matcher = arrayPattern.matcher(typeString);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	protected Object serializeTree(Abi.AbiParam param, Object inputValue) throws EverSdkException {

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
		           () -> "Param: " + param.name() + "Type: " + param.type() + "Parsed: " + rootTypeString + "Value: " +
		                 inputValue);
		var matcher = mapPattern.matcher(rootTypeString);
		while (matcher.find()) {
			rootIsMap = true;
			//log.info("Root is map: true");
			keyTypeString = matcher.group(2);
			//log.info("Key string: " + keyTypeString);
			valueTypeString = matcher.group(4);
			//log.info("Value string: " + valueTypeString);
		}

		// map = Map<String,Object> from types
		// map(type,tuple) = Map<String,Map<String,Object>>
		if (rootIsMap) {
			// Key Parse
			final boolean keyIsArray = arrayMatcher(keyTypeString);
			var keyTypePair = typeParser(keyTypeString);
			final AbiValueType keyType = keyTypePair.type();
			final int keySize = keyTypePair.size();
			//log.info("MapKey Is Array: " + keyIsArray);
			// Value Parse
			final boolean valueIsArray = arrayMatcher(valueTypeString);
			var valueTypePair = typeParser(valueTypeString);
			final AbiValueType valueType = valueTypePair.type();
			final int valueSize = valueTypePair.size();
			//log.info("MapValue Is Array: " + valueIsArray);

			Map<Object, Object> mapValue = (Map<Object, Object>) inputValue;
			if (mapValue.size() == 1) {
				final Object key = mapValue.keySet().toArray()[0];
				final Object value = mapValue.values().toArray()[0];
				return Map.of(serializeSingle(keyType, keySize, key),
				              // serializeTree is used for map(type,tuple) cases,
				              // thus it will continue to serialize tuple part
				              serializeTree(new Abi.AbiParam(valueTypeString, valueTypeString, param.components()),
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
			final boolean rootIsArray = arrayMatcher(rootTypeString);
			var rootTypePair = typeParser(rootTypeString);
			final AbiValueType rootType = rootTypePair.type();
			final int rootSize = rootTypePair.size();
			//log.info("Root Is Array: " + rootIsArray);
			// tuples
			if (rootType.equals(AbiValueType.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) inputValue;
				return Arrays.stream(param.components()).collect(
						Collectors.toMap(component -> component.name(),
						                 component -> {
							                 try {
								                 return serializeTree(component, mapValue.get(component.name()));
							                 } catch (EverSdkException e) {
								                 // in the complex cases, if we can't serialize, we can try to put object as is
								                 return mapValue.get(component.name());
							                 }
						                 }
						));
				// arrays
			} else if (rootIsArray) {
				return switch (inputValue) {
					case String s -> new Object[]{serializeSingle(rootType, rootSize, s)};
					case Object[] arr -> Arrays.stream(arr).map(element -> {
						try {
							return serializeSingle(rootType, rootSize, element);
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					case List list -> list.stream().map(element -> {
						try {
							return serializeSingle(rootType, rootSize, element);
						} catch (EverSdkException e) {
							// in the complex cases, if we can't serialize, we can try to put object as is
							return element;
						}
					}).toArray();
					default -> new Object[]{serializeSingle(rootType, rootSize, inputValue)};
				};
			} else {
				// all others
				return serializeSingle(rootType, rootSize, inputValue);
			}
		}
	}

	/**
	 * Checks and converts provided Java Map containing inputs for function call
	 * to correct representation that will be accepted by ABI. If map doesn't meet
	 * ABI input structure, this method will fail. If some type doesn't have conversion,
	 * it will be serialized as is.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> convertFunctionInputs(String functionName,
	                                                 Map<String, Object> functionInputs) throws EverSdkException {
		if (functionInputs != null) {
			Map<String, Object> convertedInputs = new HashMap<>();
			for (Map.Entry<String, Object> entry : functionInputs.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (hasInput(functionName, key)) {
					var type = this.functionInputType(functionName, key);
					try {
						convertedInputs.put(key, serializeTree(type, value));
					} catch (EverSdkException e) {
						// in the complex cases, if we can't serialize, we can try to put object as is
						convertedInputs.put(key, value);
					}
				} else {
					logger.log(System.Logger.Level.ERROR, () ->
							"ABI Function " + functionName + " doesn't contain input '" + key + "'");
					throw new EverSdkException(new EverSdkException.ErrorResult(-303,
					                                                            "Function " + functionName +
					                                                            " doesn't contain input (" + key +
					                                                            ") in ABI"), new Exception());
				}
			}
			return convertedInputs;
		} else {
			return null;
		}
	}

	/**
	 * Checks and converts provided Java Map containing inputs for initialData
	 * to correct representation that will be accepted by ABI. If map doesn't meet
	 * ABI input structure, this method will fail. If some type doesn't have conversion,
	 * it will be serialized as is.
	 *
	 * @param initDataInputs
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> convertInitDataInputs(Map<String, Object> initDataInputs) throws EverSdkException {
		if (initDataInputs != null) {
			Map<String, Object> convertedInputs = new HashMap<>();
			for (Map.Entry<String, Object> entry : initDataInputs.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (hasInitDataParam(key)) {
					var type = initDataType(key);
					try {
						convertedInputs.put(key, serializeTree(type, value));
					} catch (EverSdkException e) {
						// in the complex cases, if we can't serialize, we can try to put object as is
						convertedInputs.put(key, value);
					}
				} else {
					logger.log(System.Logger.Level.ERROR, () ->
							"ABI doesn't contain initData parameter '" + key + "'");
					throw new EverSdkException(new EverSdkException.ErrorResult(-304,
					                                                            "ABI doesn't contain initData parameter '" +
					                                                            key +
					                                                            "'"), new Exception());
				}
			}
			return convertedInputs;
		} else {
			return null;
		}

	}

	record TypePair(AbiValueType type, Integer size) {
	}

}
