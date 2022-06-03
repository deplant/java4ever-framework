package tech.deplant.java4ever.framework.artifact;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public record LocalJsonArtifact(Path path) implements Artifact<String> {

    @Override
    public void write(String content) throws IOException {
        log.info("Writing string to path: " + this.path.toString());
        Files.writeString(this.path, content, StandardCharsets.UTF_8);
    }

    @Override
    public String read() {
        try {
            log.info("Reading string from path: " + this.path.toString());
            return Files.readString(this.path).replaceAll("[\u0000-\u001f]", "");
        } catch (IOException e) {
            log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return "";
        }
    }

}
