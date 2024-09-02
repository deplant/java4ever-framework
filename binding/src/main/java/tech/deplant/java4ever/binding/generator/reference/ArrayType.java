package tech.deplant.java4ever.binding.generator.reference;

public record ArrayType(String name,
                        String type,
                        ApiType array_item,
                        String summary,
                        String description) implements ApiType {
}
