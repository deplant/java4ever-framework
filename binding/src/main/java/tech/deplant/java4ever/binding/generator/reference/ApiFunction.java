package tech.deplant.java4ever.binding.generator.reference;

public record ApiFunction(String name,
                          String summary,
                          String description,
                          ApiType[] params,
                          ApiType result,
                          String errors) {
}
