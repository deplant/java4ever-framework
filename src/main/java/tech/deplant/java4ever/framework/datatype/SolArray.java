package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public record SolArray(AbiValue[] values) implements AbiValue {

	@Override
	public Object toJava() {
		return Arrays.stream(values()).map(AbiValue::toJava).toArray();
	}

	@JsonValue
	@Override
	public Object toABI() {
		return Arrays.stream(values()).map(AbiValue::toABI).toArray();
	}

	@Override
	public AbiType type() {
		return new AbiType(values[0].type().prefix(), 0, true);
	}
}
