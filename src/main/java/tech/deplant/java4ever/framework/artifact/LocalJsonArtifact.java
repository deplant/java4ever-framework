package tech.deplant.java4ever.framework.artifact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.framework.TvmLinker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public record LocalJsonArtifact(Path path) implements Artifact<String> {


    private static Logger log = LoggerFactory.getLogger(LocalJsonArtifact.class);

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
