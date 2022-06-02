package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Abi;

import java.util.Map;

public interface IAbi {

    String json();

    boolean hasHeader(String name);

    boolean hasFunction(String name);

    boolean hasInput(String functionName, String inputName);

    boolean hasOutput(String functionName, String outputName);

    String inputType(String functionName, String inputName);

    String outputType(String functionName, String outputName);

    Abi.ABI ABI();

    record Function(Map<String, String> inputs, Map<String, String> outputs) {
    }

}
