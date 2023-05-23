package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Convert;

public record SolBytes(String text) implements AbiType<String, String> {

	public static SolBytes fromJava(Object input) {
		return new SolBytes(input.toString());
	}

	public static SolBytes fromABI(String hexString) {
		return new SolBytes(Convert.hexToStr(hexString.toUpperCase()));
	}

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "bytes";
	}

	@Override
	public String toJava() {
		return text();
	}

	@Override
	public String toABI() {
		return Convert.strToHex(text());
	}
}
