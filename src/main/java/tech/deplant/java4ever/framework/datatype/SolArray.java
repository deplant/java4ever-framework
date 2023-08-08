package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;

public record SolArray(AbiValue[] values) implements AbiValue {

	@JsonValue
	@Override
	public Object jsonValue() {
		return values();
	}

	@Override
	public AbiType type() {
		return new AbiType(values[0].type().prefix(), 0, true);
	}
}
