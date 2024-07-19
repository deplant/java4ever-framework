package tech.deplant.java4ever.binding.generator.reference;

public record GenericType(String name,
                          String type,
                          String generic_name,
                          ApiType[] generic_args,
                          String summary,
                          String description) implements ApiType {
}
