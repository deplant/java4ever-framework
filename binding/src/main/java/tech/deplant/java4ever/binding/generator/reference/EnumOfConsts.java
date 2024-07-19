package tech.deplant.java4ever.binding.generator.reference;

public record EnumOfConsts(String name,
                           String type,
                           Const[] enum_consts,
                           String summary,
                           String description) implements ApiType {
	public record Const(String name,
	                    String type,
	                    String value,
	                    String summary,
	                    String description) {
	}
}
