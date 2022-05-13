package tech.deplant.java4ever.framework.artifact;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Log4j2
public record FileArtifact(Path path) implements Artifact {

    public static FileArtifact ofResourcePath(String resourcePath) {
        var cleanedPath = processPath(resourcePath);
        if (cleanedPath.length() > 0 && !"/".equals(cleanedPath.substring(0, 1))) {
            cleanedPath = "/" + cleanedPath;
        }
        // workaround for problem that Resource addresses should start with .
        if (cleanedPath.length() > 0 && !".".equals(cleanedPath.substring(0, 1))) {
            cleanedPath = "." + cleanedPath;
        }
        log.trace("Accessing file:" + cleanedPath);

        try {
            return new FileArtifact(Paths.get(
                    FileArtifact.class.getClassLoader().getResource(cleanedPath).toURI()
            ));
        } catch (URISyntaxException | NullPointerException e) {
            log.error("Wrong path: Path: " + cleanedPath + ", Error: " + e.getMessage());
            return null;
        }
    }

    public static FileArtifact ofAbsolutePath(String absolutePath) {
        var cleanedPath = processPath(absolutePath);
        log.trace("Accessing file:" + cleanedPath);
        return new FileArtifact(Paths.get(cleanedPath));
    }

    private static String processPath(String providedPath) {
        return providedPath.replace(File.separator, "/");
    }

    @Override
    public byte[] getAsBytes() {
        try {
            return Files.readAllBytes(this.path);
        } catch (IOException e) {
            log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public String getAsString() {
        try {
            return Files.readString(this.path);
        } catch (IOException e) {
            log.error("File access error! Path: " + this.path + ", Error: " + e.getMessage());
            return "";
        }
    }

    @Override
    public String getAsJsonString() {
        return getAsString().replaceAll("[\u0000-\u001f]", "");
    }

    @Override
    public String getAsBase64String() {
        return Base64.getEncoder().encodeToString(getAsBytes());
    }

}
