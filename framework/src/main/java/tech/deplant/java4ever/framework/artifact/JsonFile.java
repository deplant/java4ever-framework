package tech.deplant.java4ever.framework.artifact;

import tech.deplant.commons.Fls;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents JSON-organized external artifacts with absolute and relative disk paths to file
 */
public record JsonFile(Path filePath) implements Artifact<String,String> {

	/**
	 * Instantiates a new Json file.
	 *
	 * @param filePathString the file path string
	 */
	public JsonFile(String filePathString) {
    this(Paths.get(filePathString));
    }

    @Override
    public String get() {
        try {
            return Files.readString(filePath(), StandardCharsets.UTF_8)
                    .replaceAll("[\u0000-\u001f]", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void accept(String jsonString) {
        try {
            Fls.write(filePath(), jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
