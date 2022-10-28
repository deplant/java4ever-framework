package tech.deplant.java4ever.framework.abi.datatype;

import tech.deplant.java4ever.framework.abi.AbiValue;

import java.util.Map;

public record Mapping<K extends AbiValue, V extends AbiValue>(Map<K, V> valuesMap) implements AbiValue {
	@Override
	public Object serialize() {
		return null;
	}
}
