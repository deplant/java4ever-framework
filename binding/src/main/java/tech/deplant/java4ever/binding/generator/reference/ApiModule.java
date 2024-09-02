package tech.deplant.java4ever.binding.generator.reference;

public record ApiModule(String name,
                        String summary,
                        String description,
                        ApiType[] types,
                        ApiFunction[] functions) {
}
