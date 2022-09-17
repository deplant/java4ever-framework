package tech.deplant.java4ever.framework.artifact;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public record StringResource(String resourceName) implements Supplier<String> {
	@Override
	public String get() {
		return new String(new ByteResource(resourceName()).get(), StandardCharsets.UTF_8);
	}
}
