package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.template.type.AbiAddress;
import tech.deplant.java4ever.framework.template.type.AbiString;
import tech.deplant.java4ever.framework.template.type.AbiTvmCell;
import tech.deplant.java4ever.framework.template.type.AbiUint;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record ContractAbi(@JsonProperty("ABI version") Integer abiVersion,
                          String version,
                          String[] header,
                          AbiDataParam[] data,
                          Abi.AbiParam[] fields,
                          AbiFunction[] functions,
                          AbiFunction[] events
) {
	private static Logger log = LoggerFactory.getLogger(ContractAbi.class);

	public static ContractAbi ofString(String jsonString) throws JsonProcessingException {
		return Sdk.DEFAULT_MAPPER.readValue(jsonString, ContractAbi.class);
	}

	public static ContractAbi ofResource(String resourceName) throws JsonProcessingException {
		return ofString(new JsonResource(resourceName).get());
	}

	public static ContractAbi ofFile(String filePath) throws JsonProcessingException {
		return ofString(new JsonFile(filePath).get());
	}

	public static ContractAbi ofJsonNode(JsonNode node) throws JsonProcessingException {
		return ofString(Sdk.DEFAULT_MAPPER.writeValueAsString(node));
	}

	public String json() throws JsonProcessingException {
		return Sdk.DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(this);
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

	private AbiFunction findFunction(AbiFunction[] functionArr, String name) {
		return Arrays.stream(functionArr).filter(function -> name.equals(function.name())).findAny().orElseThrow();
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
		return findParam(findFunction(events(), functionName).inputs(), inputName);
	}

	public Abi.AbiParam eventOutputType(String functionName, String outputName) {
		return findParam(findFunction(events(), functionName).outputs(), outputName);
	}

	public Abi.ABI ABI() {
		try {
			return new Abi.ABI.Json(json());
		} catch (JsonProcessingException e) {
			log.error("This JsonAbi can't be strigified!" + e.getMessage());
			return new Abi.ABI.Json("{}");
		}
	}

	private Object serializeValue(Abi.AbiParam abiType, Object inputValue) {
		var rootType = abiType.type();
		// type -> AbiType
		// rootType.regexp("type[]") -> AbiType
		// rootType.regexp("tuple") -> AbiComponent[]
		// rootType.regexp("map(type,type)") -> Map.of(AbiType,AbiType)
		// map(type,tuple)- > Map.of(AbiType,AbiComponent[])
		//else

		var errorMsg = "Type: " + abiType + " Input: " + inputValue.getClass().toString() +
		               "Unsupported type for ABI conversion";
		var exception = new Sdk.SdkException(new Sdk.Error(101, errorMsg));
		return switch (rootType) {
			case "uint128", "uint256", "uint64", "uint32" -> switch (inputValue) {
				case BigInteger b -> new AbiUint(b).serialize();
				case Instant i -> new AbiUint(i).serialize();
				case String strPrefixed
						when strPrefixed.length() >= 2 && "0x".equals(strPrefixed.substring(0, 2)) ->
						new AbiUint(new BigInteger(strPrefixed.substring(2))).serialize();
				case String str -> new AbiUint(new BigInteger(str)).serialize();
				default -> throw exception;
			};
			case "uint8" -> switch (inputValue) {
				case Integer i -> new AbiUint(i.longValue()).serialize();
				case String str -> new AbiUint(new BigInteger(str)).serialize();
				default -> throw exception;
			};
			case "string" -> switch (inputValue) {
				case String s -> new AbiString(s).serialize();
				default -> throw exception;
			};
			case "address" -> switch (inputValue) {
				case Address a -> new AbiAddress(a).serialize();
				case String s -> new AbiAddress(s).serialize();
				default -> throw exception;
			};
			case "bool" -> switch (inputValue) {
				case Boolean b -> b;
				default -> throw exception;
			};
			case "cell" -> switch (inputValue) {
				case String s -> s;
				case AbiTvmCell abiCell -> abiCell.serialize();
				default -> throw exception;
			};
			case "tuple" -> switch (inputValue) {
				case Map m -> Arrays.stream(abiType.components()).collect(
						Collectors.toMap(Abi.AbiParam::name, abiComp -> {
							                 return serializeValue(abiComp, m.get(abiComp.name()));
						                 }
						));
				default -> throw exception;
			};
			default -> throw new Sdk.SdkException(new Sdk.Error(101,
			                                                    errorMsg));
		};
	}

	public Map<String, Object> convertFunctionInputs(String functionName, Map<String, Object> functionInputs) {
		if (functionInputs != null) {
			Map<String, Object> convertedInputs = new HashMap<>();
			functionInputs.forEach(
					(key, value) -> {
						if (hasInput(functionName, key)) {
							var type = this.functionInputType(functionName, key);
							convertedInputs.put(key, serializeValue(type, value));
						} else {
							log.error(
									"ABI Function " + functionName + " doesn't contain input '" + key + "'");
							throw new Sdk.SdkException(new Sdk.Error(102,
							                                         "Function " + functionName +
							                                         " doesn't contain input (" + key +
							                                         ") in ABI"));
						}
					}
			);
			return convertedInputs;
		} else {
			return null;
		}
	}

	public Map<String, Object> convertInitDataInputs(Map<String, Object> initDataInputs) {
		if (initDataInputs != null) {
			Map<String, Object> convertedInputs = new HashMap<>();
			initDataInputs.forEach(
					(key, value) -> {
						if (hasInitDataParam(key)) {
							var type = initDataType(key);
							convertedInputs.put(key, serializeValue(type, value));
						} else {
							log.error(
									"ABI doesn't contain initData parameter '" + key + "'");
							throw new Sdk.SdkException(new Sdk.Error(102,
							                                         "ABI doesn't contain initData parameter '" + key +
							                                         "'"));
						}
					}
			);
			return convertedInputs;
		} else {
			return null;
		}

	}

//    @Override
//
//    public int abiVersion() {
//        return this.abiVersion;
//    }


}
