package tech.deplant.java4ever.binding.generator.reference;

public record NoneType(String type) implements ApiType {
	@Override
	public String name() {
		return null;
	}

	@Override
	public String summary() {
		return null;
	}

	@Override
	public String description() {
		return null;
	}
}
