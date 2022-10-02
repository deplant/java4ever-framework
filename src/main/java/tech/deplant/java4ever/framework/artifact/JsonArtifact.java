package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.ContextBuilder;

import java.io.IOException;
import java.util.function.Supplier;

public record JsonArtifact(Supplier<String> source) implements Artifact<JsonNode> {

	@Override
	public void write(JsonNode content) throws IOException {

	}

	@Override
	public JsonNode read() throws IOException {
		return ContextBuilder.DEFAULT_MAPPER.readTree(source().get());
	}
}
