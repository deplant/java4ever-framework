package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.JSONContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CachedABI(String json, List<String> headers,
                        Map<String, Function> functions) implements IAbi {


    private static Logger log = LoggerFactory.getLogger(CachedABI.class);

    public static CachedABI of(String json) {
        try {
            return new CachedABI(
                    json,
                    extractHeaders(JSONContext.MAPPER.readTree(json)),
                    extractFunctions(JSONContext.MAPPER.readTree(json))
            );
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error!" + e.getMessage() + e.getOriginalMessage());
            return null;
        }
    }

    static List<String> extractHeaders(JsonNode node) {
        var headers = new ArrayList<String>();
        node.get("header").elements().forEachRemaining(header -> headers.add(header.asText()));
        return headers;
    }

    static Map<String, Function> extractFunctions(JsonNode node) {
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
        return this.functions.get(functionName).inputs().containsKey(inputName);
    }

    @Override
    public boolean hasOutput(String functionName, String outputName) {
        return this.functions.get(functionName).outputs().containsKey(outputName);
    }

    @Override
    public String inputType(String functionName, String inputName) {
        return this.functions.get(functionName).inputs().get(inputName);
    }

    @Override
    public String outputType(String functionName, String outputName) {
        return this.functions.get(functionName).outputs().get(outputName);
    }

    @Override
    public Abi.ABI ABI() {
        return new Abi.ABI.Json(json());
    }

}
