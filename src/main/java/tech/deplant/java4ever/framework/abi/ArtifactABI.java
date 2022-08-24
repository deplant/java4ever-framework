package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.LocalJsonArtifact;
import tech.deplant.java4ever.framework.artifact.Persistable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public record ArtifactABI(IAbi abi, Artifact<String> artifact) implements IAbi, Persistable {

    public ArtifactABI(Sdk sdk, Artifact<String> artifact) throws JsonProcessingException {
        this(new CachedABI(new JsonAbi(sdk, artifact.read())), artifact);
    }

    public ArtifactABI(Sdk sdk, Path path) throws JsonProcessingException {
        this(sdk, new LocalJsonArtifact(path));
    }


    public static ArtifactABI ofResource(Sdk sdk, String resourcePath) throws JsonProcessingException {
        return new ArtifactABI(sdk, Artifact.resourceToPath(resourcePath));
    }

    public static ArtifactABI ofAbsolute(Sdk sdk, String absolutePath) throws JsonProcessingException {
        return new ArtifactABI(sdk, Artifact.ofAbsolutePath(absolutePath));
    }

    @Override
    public void persist() throws IOException {
        artifact().write(json());
    }

    @Override
    public Sdk sdk() {
        return abi().sdk();
    }

    @Override
    public Map<String, Function> functions() throws JsonProcessingException {
        return abi().functions();
    }

    @Override
    public List<String> headers() throws JsonProcessingException {
        return abi().headers();
    }

    @Override
    public String json() {
        return abi().json();
    }

    @Override
    public boolean hasHeader(String name) {
        return abi().hasHeader(name);
    }

    @Override
    public boolean hasFunction(String name) {
        return abi().hasFunction(name);
    }

    @Override
    public boolean hasInput(String functionName, String inputName) {
        return abi().hasInput(functionName, inputName);
    }

    @Override
    public boolean hasOutput(String functionName, String outputName) {
        return abi().hasOutput(functionName, outputName);
    }

    @Override
    public String inputType(String functionName, String inputName) {
        return abi().inputType(functionName, inputName);
    }

    @Override
    public String outputType(String functionName, String outputName) {
        return abi().outputType(functionName, outputName);
    }

    @Override
    public Abi.ABI ABI() {
        return abi().ABI();
    }

    @Override
    public Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs) {
        return abi().convertInputs(functionName, functionInputs);
    }
}