package tech.deplant.java4ever.binding.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Helper class for working with byte-organized files.
 */
public record ByteFile(String filePath) implements Supplier<byte[]>, Consumer<byte[]> {
	@Override
	public byte[] get() {
		try {
			return Files.readAllBytes(Paths.get(filePath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void accept(byte[] bytes) {
		try {
			var path = Paths.get(filePath());
			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			}
			Files.write(path,
			            bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
