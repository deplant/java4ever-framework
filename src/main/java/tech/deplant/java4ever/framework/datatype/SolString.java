package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.java4ever.binding.Abi;

import java.nio.charset.StandardCharsets;

public record SolString(String value) implements AbiValue<String> {

	@Override
	public String toString() {
		return value();
	}

	@Override
	public String toJava() {
		return toString();
	}

	@JsonValue
	public String toABI() {
		return value();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.STRING,0,false);
	}
}
