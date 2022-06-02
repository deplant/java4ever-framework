package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public record LocalStringArtifact(Path path) implements IStringArtifact {

    @Override
    public void writeString(String content) throws IOException {
        Files.writeString(this.path, content, StandardCharsets.UTF_8);
    }

    @Override
    public String readString() {
        try {
            return Files.readString(this.path);
        } catch (IOException e) {
            //log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return "";
        }
    }
}
