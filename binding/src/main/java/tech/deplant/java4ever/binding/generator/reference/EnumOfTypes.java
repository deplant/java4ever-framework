package tech.deplant.java4ever.binding.generator.reference;

public record EnumOfTypes(String name,
                          String type,
                          ApiType[] enum_types,
                          String summary,
                          String description) implements ApiType {
}
