package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.commons.Strings;

import java.math.BigInteger;

public record Address(int wid, BigInteger value) implements AbiValue<String> {

	public static final Address ZERO = new Address(0, BigInteger.ZERO);

	@JsonCreator
	public Address(String address) {
		this(Integer.parseInt(address.split(":")[0]), new BigInteger(address.split(":")[1], 16));
	}

	public Address(BigInteger value) {
		this(0, value);
	}

	public static Address fromJava(Object input) {
		return switch (input) {
			case Address a -> a;
			case String s -> new Address(s);
			case BigInteger bi -> new Address(0, bi);
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	public static Address fromABI(Object output) {
		return new Address(output.toString());
	}

	public static Address ofNullable(Object nullableObject) {
		return (null == nullableObject) ? Address.ZERO : new Address(nullableObject.toString());
	}

	public boolean isZeroAddress() {
		return this.value.equals(BigInteger.ZERO);
	}

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
