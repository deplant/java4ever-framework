package tech.deplant.java4ever.framework.abi;

import tech.deplant.java4ever.framework.Convert;

public record AbiString(String text) implements AbiValue {

	@Override
	public Object serialize() {
		return Convert.strToHex(this.text);
	}
}
