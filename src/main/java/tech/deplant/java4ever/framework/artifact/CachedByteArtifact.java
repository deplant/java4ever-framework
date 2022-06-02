package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;

public record CachedByteArtifact(Artifact<byte[]> artifact, byte[] content) implements Artifact<byte[]> {

    public CachedByteArtifact(Artifact<byte[]> artifact) {
        this(artifact, artifact.read());
    }

    @Override
    public void write(byte[] content) throws IOException {
        this.artifact.write(this.content);
    }

    @Override
    public byte[] read() {
        return this.content;
    }

    @Override
    public void persist() throws IOException {
        write(read());
    }
}
