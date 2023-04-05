package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;

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
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "bool";
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
