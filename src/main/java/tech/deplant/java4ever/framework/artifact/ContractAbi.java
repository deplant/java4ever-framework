package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Value
public class ContractAbi implements IAbi {

    //public static ContractAbi SAFE_MULTISIG = ContractAbi.ofStored(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json"));
    String abiJsonString;
    List<String> headers;
    Map<String, Function> functions;

    public static ContractAbi ofArtifact(Sdk sdk, Artifact artifact) {
        var abiJsonString = artifact.getAsJsonString();
        var node = sdk.mapper().valueToTree(abiJsonString);
        return new ContractAbi(abiJsonString, extractHeaders(node), extractFunctions(node));
    }

    private static List<String> extractHeaders(JsonNode node) {
        var headers = new ArrayList<String>();
        node.get("header").elements().forEachRemaining(header -> headers.add(header.asText()));
        return headers;
    }

    private static Map<String, Function> extractFunctions(JsonNode node) {
        var functions = new HashMap<String, Function>();
        node.get("functions").elements().forEachRemaining(func -> {
            Map<String, String> inputsMap = new HashMap<>();
            Map<String, String> outputsMap = new HashMap<>();
            func.get("inputs").elements().forEachRemaining(input -> {
                inputsMap.put(input.get("name").asText(), input.get("type").asText());
            });
            func.get("outputs").elements().forEachRemaining(output -> {
                outputsMap.put(output.get("name").asText(), output.get("type").asText());
            });
            functions.put(func.get("name").asText(),
                    new Function(inputsMap, outputsMap)
            );
        });
        return functions;
    }

    @Override
    public boolean hasHeader(String name) {
        return this.headers.contains(name);
    }

    @Override
    public boolean hasFunction(String name) {
        return this.functions.containsKey(name);
    }

    @Override
    public boolean hasInput(String functionName, String inputName) {
        return this.functions.get(functionName).inputs.containsKey(inputName);
    }

    @Override
    public boolean hasOutput(String functionName, String outputName) {
        return this.functions.get(functionName).outputs.containsKey(outputName);
    }

    @Override
    public String inputType(String functionName, String inputName) {
        return this.functions.get(functionName).inputs.get(inputName);
    }

    @Override
    public String outputType(String functionName, String outputName) {
        return this.functions.get(functionName).outputs.get(outputName);
    }

    @Override
    public Abi.ABI ABI() {
        return new Abi.ABI.Json(this.abiJsonString);
    }

    /**
     * Type of content that stores ABI.
     * <ul>
     *    <li>{@link #Contract}
     * When type is 'Contract'
     * {@code value: AbiContract}</li>
     *    <li>{@link #Json}
     * When type is 'Json'
     * {@code value: string}</li>
     *    <li>{@link #Handle}
     * When type is 'Handle'
     * {@code value: AbiHandle}</li>
     *    <li>{@link #Serialized}
     * When type is 'Serialized'
     * {@code value: AbiContract}</li>
     * </ul>
     */
    public enum AbiType {
        Contract,
        Json,
        Handle,
        Serialized
    }


    public enum AbiHeader {
        time,
        pubkey,
        expire
    }

    private record Function(Map<String, String> inputs, Map<String, String> outputs) {
    }


}
