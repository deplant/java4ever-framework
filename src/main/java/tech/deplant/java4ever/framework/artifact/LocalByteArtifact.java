package tech.deplant.java4ever.framework.artifact;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public record LocalByteArtifact(Path path) implements Artifact<byte[]> {

    @Override
    public void write(byte[] content) throws IOException {
        log.info("Writing bytes to path: " + this.path.toString());
        Files.write(this.path, content);
    }

    @Override
    public byte[] read() {
        try {
            log.info("Reading bytes from path: " + this.path.toString());
            return Files.readAllBytes(this.path);
        } catch (IOException e) {
            log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return new byte[0];
        }
    }

}
