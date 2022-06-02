package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record LocalByteArtifact(Path path) implements Artifact<byte[]> {

    @Override
    public void write(byte[] content) throws IOException {
        Files.write(this.path, content);
    }

    @Override
    public byte[] read() {
        try {
            return Files.readAllBytes(this.path);
        } catch (IOException e) {
            //log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return new byte[0];
        }
    }

}
