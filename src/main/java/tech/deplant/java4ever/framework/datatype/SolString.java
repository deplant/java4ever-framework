package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Convert;

public record SolString(String text) implements AbiType<String, String> {

	public static SolString fromJava(Object input) {
		return new SolString(input.toString());
	}

	public static SolString fromABI(String str) {
		return new SolString(str);
	}

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "string";
	}

	@Override
	public String toJava() {
		return text();
	}

	@Override
	public String toABI() {
		return text();
	}
}
