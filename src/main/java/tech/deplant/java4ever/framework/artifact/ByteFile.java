package tech.deplant.java4ever.framework.artifact;

import tech.deplant.commons.Fls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents byte-organized external artifacts with absolute and relative disk paths to file
 */
public record ByteFile(Path filePath) implements Artifact<byte[],byte[]> {

	/**
	 * Instantiates a new ByteFile artifact.
	 *
	 * @param filePathString path to external file
	 */
	public ByteFile(String filePathString) {
		this(Paths.get(filePathString));
	}

	@Override
	public byte[] get() {
		try {
			return Files.readAllBytes(filePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void accept(byte[] bytes) {
		try {
			Fls.write(filePath(), bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
