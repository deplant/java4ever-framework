package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.commons.Strings;

import java.math.BigInteger;

/**
 * The type Address.
 */
public record Address(int wid, BigInteger value) implements AbiValue<String> {

	/**
	 * The constant ZERO.
	 */
	public static final Address ZERO = new Address(0, BigInteger.ZERO);

	/**
	 * Instantiates a new Address.
	 *
	 * @param address the address
	 */
	@JsonCreator
	public Address(String address) {
		this(Integer.parseInt(address.split(":")[0]), new BigInteger(address.split(":")[1], 16));
	}

	public Address(int wid, String hash) {
		this(wid, Uint.of(256,hash).toBigInteger());
	}

	/**
	 * Instantiates a new Address.
	 *
	 * @param value the value
	 */
	public Address(BigInteger value) {
		this(0, value);
	}

	/**
	 * From java address.
	 *
	 * @param input the input
	 * @return the address
	 */
	public static Address fromJava(Object input) {
		return switch (input) {
			case Address a -> a;
			case String s -> new Address(s);
			case BigInteger bi -> new Address(0, bi);
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	/**
	 * From abi address.
	 *
	 * @param output the output
	 * @return the address
	 */
	public static Address fromABI(Object output) {
		return new Address(output.toString());
	}

	/**
	 * Of nullable address.
	 *
	 * @param nullableObject the nullable object
	 * @return the address
	 */
	public static Address ofNullable(Object nullableObject) {
		return (null == nullableObject) ? Address.ZERO : new Address(nullableObject.toString());
	}

	/**
	 * Is zero address boolean.
	 *
	 * @return the boolean
	 */
	public boolean isZeroAddress() {
		return this.value.equals(BigInteger.ZERO);
	}

	/**
	 * Make addr std string.
	 *
	 * @return the string
	 */
	public String makeAddrStd() {
		return toString();
	}

	@Override
	public String toString() {
		return
				wid() +
				":" +
				Strings.padLeftZeros(value().toString(16), 64);
	}

	@Override
	public String toJava() {
		return toString();
	}

	@JsonValue
	public String toABI() {
		return toString();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.ADDRESS,0,false);
	}
}
