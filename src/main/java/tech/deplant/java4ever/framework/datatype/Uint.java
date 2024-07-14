package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.commons.Numbers;
import tech.deplant.commons.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

/**
 * The type Uint.
 */
public record Uint(int size, BigInteger value) implements AbiValue<BigInteger> {

	/**
	 * Instantiates a new Uint.
	 *
	 * @param value the value
	 */
	@JsonCreator
	public Uint(Object value) {
		this(switch (value) {
			case Integer i -> BigInteger.valueOf(i).abs();
			case Long l -> BigInteger.valueOf(l).abs();
			case BigInteger bi -> bi.abs();
			case BigDecimal bd -> bd.toBigInteger().abs();
			case Instant inst -> BigInteger.valueOf(inst.getEpochSecond()).abs();
			case String str when Strings.isHexadecimal(str)/*str.length() >= 3 && ("-0x".equals(str.substring(0, 3)) || "0x".equals(str.substring(0, 2)))*/ ->
					Numbers.hexStringToBigInt(str);
			case String str -> new BigInteger(str);
			default -> throw new IllegalStateException(
					"Unexpected value: " + value + " class: " + value.getClass().getName());
		});
	}

	/**
	 * Instantiates a new Uint.
	 *
	 * @param value the value
	 */
	public Uint(BigInteger value) {
		this(0, value);
	}

	/**
	 * Of uint.
	 *
	 * @param size        the size
	 * @param objectValue the object value
	 * @return the uint
	 */
	public static Uint of(int size, Object objectValue) {
		return switch (objectValue) {
			case Integer i -> new Uint(size, BigInteger.valueOf(i).abs());
			case Long l -> new Uint(size, BigInteger.valueOf(l).abs());
			case BigInteger bi -> new Uint(size, bi.abs());
			case BigDecimal bd -> new Uint(size, bd.toBigInteger().abs());
			case Instant inst -> new Uint(size, BigInteger.valueOf(inst.getEpochSecond()).abs());
			case String str when str.length() >= 3 &&
			                     ("-0x".equals(str.substring(0, 3)) || "0x".equals(str.substring(0, 2))) ->
					new Uint(size, Numbers.hexStringToBigInt(str));
			case String str when !str.matches("[0-9]+") -> new Uint(size, Numbers.hexStringToBigInt(str));
			case String str -> new Uint(size, new BigInteger(str));
			default -> throw new IllegalStateException(
					"Unexpected value: " + objectValue + " class: " + objectValue.getClass().getName());
		};
	}

	/**
	 * To big integer big integer.
	 *
	 * @return the big integer
	 */
	public BigInteger toBigInteger() {
		return value();
	}

	@Override
	public String toString() {
		return "0x" + value().toString(16);
	}

	@Override
	public BigInteger toJava() {
		return toBigInteger();
	}

	@JsonValue
	public String toABI() {
		return toString();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.UINT, size(), false);
	}
}
