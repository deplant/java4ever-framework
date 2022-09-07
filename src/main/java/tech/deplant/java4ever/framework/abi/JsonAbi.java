package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.type.AbiAddress;
import tech.deplant.java4ever.framework.abi.type.AbiUint;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record JsonAbi(Sdk sdk, String json) implements IAbi {


    private static Logger log = LoggerFactory.getLogger(JsonAbi.class);

    @Override
    public Map<String, Function> functions() throws JsonProcessingException {
        var functions = new HashMap<String, Function>();
        this.sdk.mapper().readTree(this.json).get("functions").elements().forEachRemaining(func -> {
            var inputsMap = new HashMap<String, String>();
            var outputsMap = new HashMap<String, String>();
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
    public List<String> headers() throws JsonProcessingException {
        var headers = new ArrayList<String>();
        this.sdk.mapper().readTree(this.json).get("header").elements().forEachRemaining(header -> headers.add(header.asText()));
        return headers;
    }

    @Override
    public boolean hasHeader(String name) {
        try {
            return headers().contains(name);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasFunction(String name) {
        try {
            return functions().containsKey(name);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasInput(String functionName, String inputName) {
        try {
            return functions().get(functionName).inputs().containsKey(inputName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasOutput(String functionName, String outputName) {
        try {
            return functions().get(functionName).outputs().containsKey(outputName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String inputType(String functionName, String inputName) {
        try {
            return functions().get(functionName).inputs().get(inputName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String outputType(String functionName, String outputName) {
        try {
            return functions().get(functionName).outputs().get(outputName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Abi.ABI ABI() {
        return new Abi.ABI.Json(json());
    }

    @Override
    public Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs) {
//        Map<String, Object> converted = new HashMap<>();
//        for (Map.Entry<String, Object> entry : functionInputs.entrySet()) {
//            if (hasInput(functionName, entry.getKey())) {
//                switch (inputType(functionName, entry.getKey())) {
//
//                }
//            }
//        }

        return functionInputs.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(), entry -> {
                    if (this.hasInput(functionName, entry.getKey())) {
                        var type = this.inputType(functionName, entry.getKey());
                        return switch (type) {
                            case "uint128", "uint256", "uint64", "uint32" -> switch (entry.getValue()) {
                                case BigInteger b -> new AbiUint(b).serialize();
                                case Instant i -> new AbiUint(i).serialize();
                                case String strPrefixed
                                        when strPrefixed.length() >= 2 && "0x".equals(strPrefixed.substring(0, 2)) ->
                                        new AbiUint(new BigInteger(strPrefixed.substring(2))).serialize();
                                case String str -> new AbiUint(new BigInteger(str)).serialize();
                                default ->
                                        throw new Sdk.SdkException(new Sdk.Error(101, "Function " + functionName + "Unsupported type for ABI conversion"));
                            };
                            case "address" -> switch (entry.getValue()) {
                                case Address a -> new AbiAddress(a).serialize();
                                case String s -> new AbiAddress(s).serialize();
                                default ->
                                        throw new Sdk.SdkException(new Sdk.Error(101, "Function " + functionName + "Unsupported type for ABI conversion"));
                            };
                            case "bool" -> switch (entry.getValue()) {
                                case Boolean b -> b;
                                default ->
                                        throw new Sdk.SdkException(new Sdk.Error(101, "Function " + functionName + "Unsupported type for ABI conversion"));
                            };
                            case "cell" -> switch (entry.getValue()) {
                                case String s -> s;
                                default ->
                                        throw new Sdk.SdkException(new Sdk.Error(101, "Function " + functionName + "Unsupported type for ABI conversion"));
                            };
                            default ->
                                    throw new Sdk.SdkException(new Sdk.Error(101, "Function " + functionName + "Unsupported type for ABI conversion"));
                        };
                    } else {
                        log.error("Function " + functionName + " doesn't contain input (" + entry.getKey() + ") in ABI");
                        throw new Sdk.SdkException(new Sdk.Error(102, "Function " + functionName + " doesn't contain input (" + entry.getKey() + ") in ABI"));
                    }
                }
        ));
    }

}
