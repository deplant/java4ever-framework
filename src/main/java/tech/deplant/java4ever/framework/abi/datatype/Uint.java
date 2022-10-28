package tech.deplant.java4ever.framework.abi.datatype;

import java.math.BigInteger;
import java.time.Instant;

public record Uint(int size, BigInteger bigInteger) implements AbiType<BigInteger, String> {

	public static Uint fromJava(int size, Object input) {
		return switch (input) {
			case Uint u -> new Uint(size, u.bigInteger());
			case Integer i -> new Uint(size, BigInteger.valueOf(i));
			case Long l -> new Uint(size, BigInteger.valueOf(l));
			case BigInteger b -> new Uint(size, b);
			case Instant inst -> new Uint(size, BigInteger.valueOf(inst.getEpochSecond()));
			case String strPrefixed
					when strPrefixed.length() >= 2 && "0x".equals(strPrefixed.substring(0, 2)) ->
					new Uint(size, new BigInteger(strPrefixed.substring(2), 16));
			case String str -> new Uint(size, new BigInteger(str, 16));
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	@Override
	public BigInteger toJava() {
		return bigInteger();
	}

	@Override
	public String toABI() {
		return "0x" + bigInteger().toString(16);
	}
}
