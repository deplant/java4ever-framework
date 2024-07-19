package tech.deplant.java4ever.binding.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
			Files.writeString(Paths.get(getClass().getResource(resourceName()).getPath()),
			                  jsonString, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}