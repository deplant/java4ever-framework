package tech.deplant.java4ever.binding.generator.reference;

public record StringType(String name,
                         String type,
                         String summary,
                         String description) implements ApiType {
}
