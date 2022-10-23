package tech.deplant.java4ever.framework.abi;

import java.util.Map;

public record AbiMap<K extends AbiValue, V extends AbiValue>(Map<K, V> valuesMap) implements AbiValue {
	@Override
	public Object serialize() {
		return null;
	}
}