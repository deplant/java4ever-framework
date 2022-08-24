package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;

import java.util.List;
import java.util.Map;

public interface IAbi {

    Sdk sdk();

    Map<String, Function> functions() throws JsonProcessingException;

    List<String> headers() throws JsonProcessingException;

    String json();

    boolean hasHeader(String name);

    boolean hasFunction(String name);

    boolean hasInput(String functionName, String inputName);

    boolean hasOutput(String functionName, String outputName);

    String inputType(String functionName, String inputName);

    String outputType(String functionName, String outputName);

    Abi.ABI ABI();

    Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs);

    record Function(Map<String, String> inputs, Map<String, String> outputs) {
    }

}
