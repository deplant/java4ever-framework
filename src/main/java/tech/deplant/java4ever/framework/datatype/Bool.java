package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.java4ever.binding.Abi;

public record Bool(Boolean value) implements AbiValue {

	public Bool(String stringValue) {
		this(Boolean.valueOf(stringValue);
	}

	public boolean toBoolean() {
		return value();
	}

	@JsonValue
	public Boolean jsonValue() {
		return value();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BOOL,0,false);
	}
}
