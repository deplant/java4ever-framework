package tech.deplant.java4ever.framework.template.abi.type;

import tech.deplant.java4ever.framework.Data;

public record AbiString(String text) implements AbiValue {

	@Override
	public Object serialize() {
		return Data.strToHex(this.text);
	}
}
