package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Abi;

import java.io.IOException;
import java.nio.file.Path;

public record ArtifactABI(IAbi abi, Artifact<String> artifact) implements IAbi, Persistable {

    public ArtifactABI(Artifact<String> artifact) {
        this(CachedABI.of(artifact.read()), artifact);
    }

    public ArtifactABI(Path path) {
        this(new LocalJsonArtifact(path));
    }


    public static ArtifactABI ofResource(String resourcePath) {
        return new ArtifactABI(Artifact.resourceToPath(resourcePath));
    }

    public static ArtifactABI ofAbsolute(String absolutePath) {
        return new ArtifactABI(Artifact.ofAbsolutePath(absolutePath));
    }

    @Override
    public void persist() throws IOException {
        artifact().write(json());
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
}