package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
			Files.write(Paths.get(filePath()),
			            bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
