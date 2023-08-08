package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.commons.Strings;

import java.nio.charset.StandardCharsets;

public record SolBytes(int size, byte[] value) implements AbiValue {

	public SolBytes(byte[] value) {
		this(0,value);
	}

	public SolBytes(int size, String hexValue) {
		this(size,Strings.hexStringToBytes(hexValue.toUpperCase()));
	}

	@JsonCreator
	public SolBytes(String hexValue) {
		this(0,hexValue);
	}

	public byte[] toBytes() {
		return value();
	}

	public String toHexString() {
		return Strings.toHexString(value());
	}

	@Override
	public String toString() {
		return new String(value(), StandardCharsets.UTF_8);
	}

	@JsonValue
	public String jsonValue() {
		return Strings.toHexString(value());
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BYTES,size(),false);
	}
}
