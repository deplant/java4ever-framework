package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkContext;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.datatype.SolStruct;
import tech.deplant.java4ever.framework.datatype.TypePrefix;
import tech.deplant.java4ever.framework.datatype.Uint;

import java.util.Arrays;
import java.util.Map;

/**
 * Holds entire ABI structure and has helper methods to check functions availability or
 * getting input types and so on. Use factory methods to create from File or JsonNode.
 */
public record ContractAbi(Abi.AbiContract abiContract) {

	private final static System.Logger logger = System.getLogger(ContractAbi.class.getName());

	public static ContractAbi ofString(String jsonString) throws JsonProcessingException {
		return new ContractAbi(EverSdkContext.Builder.DEFAULT_MAPPER.readValue(jsonString, Abi.AbiContract.class));
	}

	public static ContractAbi ofResource(String resourceName) throws JsonProcessingException {
		return ofString(new JsonResource(resourceName).get());
	}

	public static ContractAbi ofFile(String filePath) throws JsonProcessingException {
		return ofString(new JsonFile(filePath).get());
	}

	public static ContractAbi ofJsonNode(JsonNode node) throws JsonProcessingException {
		return ofString(EverSdkContext.Builder.DEFAULT_MAPPER.writeValueAsString(node));
	}

	public String json() throws JsonProcessingException {
		return EverSdkContext.Builder.DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		                                            .writeValueAsString(abiContract());
	}

	public String functionId(Sdk sdk, String name) throws EverSdkException {
		return Uint.fromJava(32, Abi.calcFunctionId(sdk.context(), ABI(), name, false).functionId()).toABI();
	}

	public boolean hasHeader(String name) {
		return Arrays.asList(header()).contains(name);
	}

	public boolean hasFunction(String name) {
		return Arrays.stream(functions()).anyMatch(function -> name.equals(function.name()));
	}

	public boolean hasInitDataParam(String initDataName) {
		return Arrays.stream(data()).anyMatch(data -> initDataName.equals(data.name()));
	}

	public boolean hasInput(String functionName, String inputName) {
		return Arrays.stream(functions())
		             .anyMatch(function -> functionName.equals(function.name()) && Arrays.stream(function.inputs())
		                                                                                 .anyMatch(input -> inputName.equals(
				                                                                                 input.name())));
	}

	public boolean hasOutput(String functionName, String outputName) {
		return Arrays.stream(functions())
		             .anyMatch(function -> functionName.equals(function.name()) && Arrays.stream(function.outputs())
		                                                                                 .anyMatch(output -> outputName.equals(
				                                                                                 output.name())));
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
		var dataParam = Arrays.stream(data()).filter(data -> initDataName.equals(data.name())).findAny().orElseThrow();
		return new Abi.AbiParam(dataParam.name(), dataParam.type(), dataParam.components());
	}

	public Abi.AbiParam functionOutputType(String functionName, String outputName) {
		return findParam(findFunction(functions(), functionName).outputs(), outputName);
	}

	public Abi.AbiParam eventInputType(String functionName, String inputName) {
		return findParam(findEvent(events(), functionName).inputs(), inputName);
	}

	public Abi.AbiData[] data() {
		return abiContract().data();
	}

	public Abi.AbiFunction[] functions() {
		return abiContract().functions();
	}

	public Abi.AbiEvent[] events() {
		return abiContract().events();
	}

	public String[] header() {
		return abiContract().header();
	}

	public Abi.ABI ABI() {
		return new Abi.ABI.Contract(abiContract());
	}


	/**
	 * Checks and converts provided Java Map containing inputs for function call
	 * to correct representation that will be accepted by ABI. If map doesn't meet
	 * ABI input structure, this method will fail. If some type doesn't have conversion,
	 * it will be serialized as is.
	 *
	 * @param functionName
	 * @param functionOutputs
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> convertFunctionOutputs(String functionName,
	                                                  Map<String, Object> functionOutputs) throws EverSdkException {
		var functionParams = Arrays.stream(functions())
		                           .filter(function -> functionName.equals(function.name()))
		                           .findAny()
		                           .orElseThrow(() -> new EverSdkException(new EverSdkException.ErrorResult(-303,
		                                                                                                    "No such function: " +
		                                                                                                    functionName +
		                                                                                                    " in ABI"),
		                                                                   new Exception()))
		                           .outputs();
		return SolStruct.fromABI(functionParams, functionOutputs).values();
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
		var functionParams = Arrays.stream(functions())
		                           .filter(function -> functionName.equals(function.name()))
		                           .findAny()
		                           .orElseThrow(() -> new EverSdkException(new EverSdkException.ErrorResult(-303,
		                                                                                                    "No such function: " +
		                                                                                                    functionName +
		                                                                                                    " in ABI"),
		                                                                   new Exception()))
		                           .inputs();
		return SolStruct.fromJava(functionParams, functionInputs).values();
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
		var initDataParams = Arrays.stream(data())
		                           .map(data -> new Abi.AbiParam(data.name(), data.type(), data.components()))
		                           .toArray(Abi.AbiParam[]::new);
		return SolStruct.fromJava(initDataParams, initDataInputs).values();
	}

	public record AbiTypeDetails(TypePrefix type, int size, boolean isArray) {
	}

}
