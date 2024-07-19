package tech.deplant.java4ever.binding.gql;

/**
 * Request with external inbound message;
 */
public record Request(String id, String body, Float expireAt) {
}
