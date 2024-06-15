package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.datatype.SolStruct;
import tech.deplant.java4ever.framework.datatype.Uint;

import java.util.Arrays;
import java.util.Map;

/**
 * Holds entire ABI structure and has helper methods to check functions availability or
 * getting input types and so on. Use factory methods to create from File or JsonNode.
 */
public record ContractAbi(Abi.AbiContract abiContract) {

	private final static System.Logger logger = System.getLogger(ContractAbi.class.getName());

	/**
	 * Of string contract abi.
	 *
	 * @param jsonString the json string
	 * @return the contract abi
	 * @throws JsonProcessingException the json processing exception
	 */
	public static ContractAbi ofString(String jsonString) throws JsonProcessingException {
		return new ContractAbi(JsonContext.ABI_JSON_MAPPER().readValue(jsonString, Abi.AbiContract.class));
	}

	/**
	 * Of resource contract abi.
	 *
	 * @param resourceName the resource name
	 * @return the contract abi
	 * @throws JsonProcessingException the json processing exception
	 */
	public static ContractAbi ofResource(String resourceName) throws JsonProcessingException {
		return ofString(new JsonResource(resourceName).get());
	}

	/**
	 * Of file contract abi.
	 *
	 * @param filePath the file path
	 * @return the contract abi
	 * @throws JsonProcessingException the json processing exception
	 */
	public static ContractAbi ofFile(String filePath) throws JsonProcessingException {
		return ofString(new JsonFile(filePath).get());
	}

	/**
	 * Of json node contract abi.
	 *
	 * @param node the node
	 * @return the contract abi
	 * @throws JsonProcessingException the json processing exception
	 */
	public static ContractAbi ofJsonNode(JsonNode node) throws JsonProcessingException {
		return ofString(JsonContext.ABI_JSON_MAPPER().writeValueAsString(node));
	}

	/**
	 * Json string.
	 *
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	public String json() throws JsonProcessingException {
		return JsonContext.ABI_JSON_MAPPER().setSerializationInclusion(JsonInclude.Include.NON_NULL)
		                  .writeValueAsString(abiContract());
	}

	/**
	 * Function id string.
	 *
	 * @param sdk  the sdk
	 * @param name the name
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String functionId(int sdk, String name) throws EverSdkException {
		return Uint.of(32, EverSdk.await(Abi.calcFunctionId(sdk, ABI(), name, false)).functionId()).toABI();
	}

	/**
	 * Has header boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 */
	public boolean hasHeader(String name) {
		return Arrays.asList(header()).contains(name);
	}

	/**
	 * Has function boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 */
	public boolean hasFunction(String name) {
		return Arrays.stream(functions()).anyMatch(function -> name.equals(function.name()));
	}

	/**
	 * Has init data param boolean.
	 *
	 * @param initDataName the init data name
	 * @return the boolean
	 */
	public boolean hasInitDataParam(String initDataName) {
		return Arrays.stream(data()).anyMatch(data -> initDataName.equals(data.name()));
	}

	/**
	 * Has input boolean.
	 *
	 * @param functionName the function name
	 * @param inputName    the input name
	 * @return the boolean
	 */
	public boolean hasInput(String functionName, String inputName) {
		return Arrays.stream(functions())
		             .anyMatch(function -> functionName.equals(function.name()) && Arrays.stream(function.inputs())
		                                                                                 .anyMatch(input -> inputName.equals(
				                                                                                 input.name())));
	}

	/**
	 * Has output boolean.
	 *
	 * @param functionName the function name
	 * @param outputName   the output name
	 * @return the boolean
	 */
	public boolean hasOutput(String functionName, String outputName) {
		return Arrays.stream(functions())
		             .anyMatch(function -> functionName.equals(function.name()) && Arrays.stream(function.outputs())
		                                                                                 .anyMatch(output -> outputName.equals(
				                                                                                 output.name())));
	}

	private Abi.AbiFunction findFunction(String name) {
		return Arrays.stream(functions()).filter(function -> name.equals(function.name())).findAny().orElseThrow();
	}

	private Abi.AbiEvent findEvent(String name) {
		return Arrays.stream(events()).filter(event -> name.equals(event.name())).findAny().orElseThrow();
	}

	private Abi.AbiParam findParam(Abi.AbiParam[] paramArr, String name) {
		return Arrays.stream(paramArr).filter(input -> name.equals(input.name())).findAny().orElseThrow();
	}

	/**
	 * Function input type abi . abi param.
	 *
	 * @param functionName the function name
	 * @param inputName    the input name
	 * @return the abi . abi param
	 */
	public Abi.AbiParam functionInputType(String functionName, String inputName) {
		return findParam(findFunction(functionName).inputs(), inputName);
	}

	/**
	 * Init data type abi . abi param.
	 *
	 * @param initDataName the init data name
	 * @return the abi . abi param
	 */
	public Abi.AbiParam initDataType(String initDataName) {
		var dataParam = Arrays.stream(data()).filter(data -> initDataName.equals(data.name())).findAny().orElseThrow();
		return new Abi.AbiParam(dataParam.name(), dataParam.type(), dataParam.components(), true);
	}

	/**
	 * Function output type abi . abi param.
	 *
	 * @param functionName the function name
	 * @param outputName   the output name
	 * @return the abi . abi param
	 */
	public Abi.AbiParam functionOutputType(String functionName, String outputName) {
		return findParam(findFunction(functionName).outputs(), outputName);
	}

	/**
	 * Event input type abi . abi param.
	 *
	 * @param functionName the function name
	 * @param inputName    the input name
	 * @return the abi . abi param
	 */
	public Abi.AbiParam eventInputType(String functionName, String inputName) {
		return findParam(findEvent(functionName).inputs(), inputName);
	}

	/**
	 * Data abi . abi data [ ].
	 *
	 * @return the abi . abi data [ ]
	 */
	public Abi.AbiData[] data() {
		return abiContract().data();
	}

	/**
	 * Functions abi . abi function [ ].
	 *
	 * @return the abi . abi function [ ]
	 */
	public Abi.AbiFunction[] functions() {
		return abiContract().functions();
	}

	/**
	 * Events abi . abi event [ ].
	 *
	 * @return the abi . abi event [ ]
	 */
	public Abi.AbiEvent[] events() {
		return abiContract().events();
	}

	/**
	 * Header string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] header() {
		return abiContract().header();
	}

	/**
	 * Abi abi . abi.
	 *
	 * @return the abi . abi
	 */
	public Abi.ABI ABI() {
		return new Abi.ABI.Contract(abiContract());
	}

	/**
	 * Function call abi abi . abi.
	 *
	 * @param functionName the function name
	 * @return the abi . abi
	 */
	public Abi.ABI functionCallABI(String functionName) {
		return new Abi.ABI.Contract(new Abi.AbiContract(abiContract().abiVersionMajor(),
		                                                abiContract().abiVersion(),
		                                                abiContract().version(),
		                                                abiContract().header(),
		                                                new Abi.AbiFunction[]{findFunction(functionName)},
		                                                new Abi.AbiEvent[]{},
		                                                new Abi.AbiData[]{},
		                                                new Abi.AbiParam[]{}));
	}


	/**
	 * Checks and converts provided Java Map containing inputs for function call
	 * to correct representation that will be accepted by ABI. If map doesn't meet
	 * ABI input structure, this method will fail. If some type doesn't have conversion,
	 * it will be serialized as is.
	 *
	 * @param functionName    the function name
	 * @param functionOutputs the function outputs
	 * @return map
	 * @throws EverSdkException the ever sdk exception
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
	 * @param functionName   the function name
	 * @param functionInputs the function inputs
	 * @return map
	 * @throws EverSdkException the ever sdk exception
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
	 * @param initDataInputs the init data inputs
	 * @return map
	 * @throws EverSdkException the ever sdk exception
	 */
	public Map<String, Object> convertInitDataInputs(Map<String, Object> initDataInputs) throws EverSdkException {
		var initDataParams = Arrays.stream(data())
		                           .map(data -> new Abi.AbiParam(data.name(), data.type(), data.components(), true))
		                           .toArray(Abi.AbiParam[]::new);
		return SolStruct.fromJava(initDataParams, initDataInputs).values();
	}

}
