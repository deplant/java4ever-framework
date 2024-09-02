package tech.deplant.java4ever.binding.generator.reference;

public record StructType(String name,
                         String type,
                         ApiType[] struct_fields,
                         String summary,
                         String description) implements ApiType {
}
