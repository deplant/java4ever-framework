package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;

public record Bool(Boolean value) implements AbiValue<Boolean> {

	public Bool(String stringValue) {
		this(Boolean.valueOf(stringValue));
	}

	public boolean toBoolean() {
		return value();
	}

	@Override
	public Boolean toJava() {
		return toBoolean();
	}

	@JsonValue
	public Boolean toABI() {
		return value();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BOOL,0,false);
	}
}
