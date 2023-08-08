package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.java4ever.binding.Abi;

import java.nio.charset.StandardCharsets;

public record SolString(String value) implements AbiValue {

	@Override
	public String toString() {
		return value();
	}

	@JsonValue
	public String jsonValue() {
		return value();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.STRING,0,false);
	}
}
