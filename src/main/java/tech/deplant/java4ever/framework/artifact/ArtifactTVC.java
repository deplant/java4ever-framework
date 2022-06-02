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
