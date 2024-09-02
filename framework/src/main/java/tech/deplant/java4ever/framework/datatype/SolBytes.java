package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.commons.Strings;

import java.nio.charset.StandardCharsets;

/**
 * The type Sol bytes.
 */
public record SolBytes(int size, byte[] value) implements AbiValue {

	/**
	 * Instantiates a new Sol bytes.
	 *
	 * @param value the value
	 */
	public SolBytes(byte[] value) {
		this(0,value);
	}

	/**
	 * Instantiates a new Sol bytes.
	 *
	 * @param size     the size
	 * @param hexValue the hex value
	 */
	public SolBytes(int size, String hexValue) {
		this(size,Strings.hexStringToBytes(hexValue.toUpperCase()));
	}

	/**
	 * Instantiates a new Sol bytes.
	 *
	 * @param hexValue the hex value
	 */
	@JsonCreator
	public SolBytes(String hexValue) {
		this(0,hexValue);
	}

	/**
	 * To bytes byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public byte[] toBytes() {
		return value();
	}

	/**
	 * To hex string string.
	 *
	 * @return the string
	 */
	public String toHexString() {
		return Strings.toHexString(value());
	}

	@Override
	public String toString() {
		return new String(value(), StandardCharsets.UTF_8);
	}

	@Override
	public Object toJava() {
		return toBytes();
	}

	@JsonValue
	public String toABI() {
		return Strings.toHexString(value());
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BYTES,size(),false);
	}
}
