package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public record SubscribeEvent(JsonNode result) implements ExternalEvent {
}
