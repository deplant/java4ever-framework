package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Convert;

public record ByteString(String text) implements AbiType<String, String> {

	public static ByteString fromJava(Object input) {
		return new ByteString(input.toString());
	}

	public static ByteString fromABI(String hexString) {
		return new ByteString(Convert.hexToStr(hexString.toUpperCase()));
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
		return Convert.strToHex(text());
	}
}
