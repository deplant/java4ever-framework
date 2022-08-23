package tech.deplant.java4ever.framework.abi.type;

import java.util.Map;

public record AbiMap<K extends AbiValue, V extends AbiValue>(Map<K, V> valuesMap) implements AbiValue {
}
