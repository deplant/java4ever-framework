package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;

import java.util.List;
import java.util.Map;

public class CachedABI implements IAbi {

    private static Logger log = LoggerFactory.getLogger(CachedABI.class);
    private final IAbi origin;
    private final Map<String, Function> functions;
    private final List<String> headers;

    public CachedABI(IAbi abi) throws JsonProcessingException {
        this.functions = abi.functions();
        this.headers = abi.headers();
        this.origin = abi;
    }

    @Override
    public Sdk sdk() {
        return this.origin.sdk();
    }

    @Override
    public Map<String, Function> functions() throws JsonProcessingException {
        return this.functions;
    }

    @Override
    public List<String> headers() throws JsonProcessingException {
        return this.headers;
    }

    @Override
    public String json() {
        return this.origin.json();
    }

    @Override
    public boolean hasHeader(String name) {
        return this.origin.hasHeader(name);
    }

    @Override
    public boolean hasFunction(String name) {
        return this.origin.hasFunction(name);
    }

    @Override
    public boolean hasInput(String functionName, String inputName) {
        return this.origin.hasInput(functionName, inputName);
    }

    @Override
    public boolean hasOutput(String functionName, String outputName) {
        return this.origin.hasOutput(functionName, outputName);
    }

    @Override
    public String inputType(String functionName, String inputName) {
        return this.origin.inputType(functionName, inputName);
    }

    @Override
    public String outputType(String functionName, String outputName) {
        return this.origin.outputType(functionName, outputName);
    }

    @Override
    public Abi.ABI ABI() {
        return this.origin.ABI();
    }
}
