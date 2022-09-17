package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.framework.Sdk;

import java.util.function.Supplier;

public record JsonResource(String resourceName) implements Supplier<JsonNode> {
	@Override
	public JsonNode get() {
		try {
			return Sdk.DEFAULT_MAPPER.readTree(new StringResource(resourceName()).get()
			                                                                     .replaceAll("[\u0000-\u001f]", ""));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}