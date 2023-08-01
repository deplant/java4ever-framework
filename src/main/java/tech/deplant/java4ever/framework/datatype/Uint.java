package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.commons.Numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public record Uint(int size, BigInteger bigInteger) implements AbiType<BigInteger, String> {

	public static Uint fromJava(int size, Object input) {
		return switch (input) {
			case Uint u -> new Uint(size, u.bigInteger().abs());
			case Integer i -> new Uint(size, BigInteger.valueOf(i).abs());
			case Long l -> new Uint(size, BigInteger.valueOf(l).abs());
			case BigInteger bi -> new Uint(size, bi.abs());
			case BigDecimal bd -> new Uint(size, bd.toBigInteger().abs());
			case Instant inst -> new Uint(size, BigInteger.valueOf(inst.getEpochSecond()).abs());
			case String str when str.length() >= 3 && ("-0x".equals(str.substring(0, 3)) || "0x".equals(str.substring(0, 2))) ->
					new Uint(size, Numbers.hexStringToBigInt(str));
			case String str -> new Uint(size, new BigInteger(str));
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "uint" + size();
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
