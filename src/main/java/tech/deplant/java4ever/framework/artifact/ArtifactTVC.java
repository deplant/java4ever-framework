package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.Sdk;

import java.io.IOException;
import java.nio.file.Path;

public record ArtifactTVC(ITvc tvc, Artifact<byte[]> artifact) implements ITvc, Persistable {

    public ArtifactTVC(Artifact<byte[]> artifact) {
        this(new CachedTVC(artifact.read()), artifact);
    }

    public ArtifactTVC(Path path) {
        this(new LocalByteArtifact(path));
    }

    public static ArtifactTVC ofResource(String resourcePath) {
        return new ArtifactTVC(Artifact.resourceToPath(resourcePath));
    }

    public static ArtifactTVC ofAbsolute(String absolutePath) {
        return new ArtifactTVC(Artifact.ofAbsolutePath(absolutePath));
    }

    @Override
    public byte[] bytes() {
        return tvc().bytes();
    }

    @Override
    public String base64String() {
        return tvc().base64String();
    }

    @Override
    public String code(Sdk sdk) throws Sdk.SdkException {
        return tvc().code(sdk);
    }

    @Override
    public void persist() throws IOException {
        artifact().write(bytes());
    }
}
