package tech.deplant.java4ever.framework;

import com.google.gson.JsonParser;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.FileArtifact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Value
public class ContractAbi {

    public static ContractAbi SAFE_MULTISIG = ContractAbi.ofStored(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json"));
    String abiJsonString;
    List<String> headers = new ArrayList<>();
    Map<String, Function> functions = new HashMap<>();

    public ContractAbi(String abiJsonString) {
        var jsonObj = JsonParser.parseString(abiJsonString).getAsJsonObject();
        jsonObj.get("functions").getAsJsonArray().iterator().forEachRemaining(func -> {
            var funcObj = func.getAsJsonObject();
            Map<String, String> inputsMap = new HashMap<>();
            Map<String, String> outputsMap = new HashMap<>();
            funcObj.get("inputs").getAsJsonArray().iterator().forEachRemaining(input -> {
                var inputObj = input.getAsJsonObject();
                inputsMap.put(inputObj.get("name").getAsString(), inputObj.get("type").getAsString());
            });
            funcObj.get("outputs").getAsJsonArray().iterator().forEachRemaining(output -> {
                var outputObj = output.getAsJsonObject();
                outputsMap.put(outputObj.get("name").getAsString(), outputObj.get("type").getAsString());
            });
            this.functions.put(funcObj.get("name").getAsString(),
                    new Function(inputsMap, outputsMap)
            );
        });
        jsonObj.get("header").getAsJsonArray().iterator().forEachRemaining(header -> this.headers.add(header.getAsString()));
        this.abiJsonString = abiJsonString;
    }

    public static ContractAbi ofStored(Artifact artifact) {
        return artifact.getAsABI();
    }

    public boolean hasHeader(String name) {
        return this.headers.contains(name);
    }

    public boolean hasFunction(String name) {
        return this.functions.containsKey(name);
    }

    public boolean hasInput(String functionName, String inputName) {
        return this.functions.get(functionName).inputs.containsKey(inputName);
    }

    public boolean hasOutput(String functionName, String outputName) {
        return this.functions.get(functionName).outputs.containsKey(outputName);
    }

    public String inputType(String functionName, String inputName) {
        return this.functions.get(functionName).inputs.get(inputName);
    }

    public String outputType(String functionName, String outputName) {
        return this.functions.get(functionName).outputs.get(outputName);
    }

    public Abi.ABI abiJson() {
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

    private record Function(Map<String, String> inputs,
                            Map<String, String> outputs) {
    }


}
