package tech.deplant.java4ever.framework.datatype;

public record Bool(Boolean value) implements AbiType<Boolean, Boolean> {

	public static Bool fromJava(Object input) {
		return switch (input) {
			case Bool u -> u;
			case String s -> new Bool(Boolean.valueOf(s));
			case Boolean b -> new Bool(b);
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	@Override
	public Boolean toJava() {
		return value();
	}

	@Override
	public Boolean toABI() {
		return value();
	}
}
