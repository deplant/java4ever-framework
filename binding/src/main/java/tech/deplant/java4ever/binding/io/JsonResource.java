package tech.deplant.java4ever.binding.io;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Helper class for working with JSON-organized project resources.
 */
public record JsonResource(String resourceName) implements Supplier<String>, Consumer<String> {



	@Override
	public String get() {
		return new String(
				new ByteResource(resourceName()).get(), StandardCharsets.UTF_8)
				.replaceAll("[\u0000-\u001f]", "");
	}

	@Override
	public void accept(String jsonString) {
		try {
			Path path = Paths.get(resourceName).toAbsolutePath();
			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			}
			Files.writeString(path,
			                  jsonString, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}