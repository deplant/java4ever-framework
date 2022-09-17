package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.util.function.Supplier;

public record ByteResource(String resourceName) implements Supplier<byte[]> {
	@Override
	public byte[] get() {
		try {
			return getClass().getResourceAsStream(resourceName()).readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
