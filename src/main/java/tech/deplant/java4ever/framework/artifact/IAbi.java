package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Abi;

public interface IAbi {

    public boolean hasHeader(String name);

    public boolean hasFunction(String name);

    public boolean hasInput(String functionName, String inputName);

    public boolean hasOutput(String functionName, String outputName);

    public String inputType(String functionName, String inputName);

    public String outputType(String functionName, String outputName);

    public Abi.ABI ABI();

}
