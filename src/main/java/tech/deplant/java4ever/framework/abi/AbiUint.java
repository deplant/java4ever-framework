package tech.deplant.java4ever.framework.abi;

import java.math.BigInteger;
import java.time.Instant;

public record AbiUint(int size, BigInteger bigInteger) implements AbiValue {

	public AbiUint(BigInteger bigValue) {
		this(256, bigValue);
	}

	public AbiUint(Integer intValue) {
		this(256, BigInteger.valueOf(intValue));
	}

	public AbiUint(Long longVal) {
		this(256, BigInteger.valueOf(longVal));
	}

	public AbiUint(Instant instant) {
		this(instant.getEpochSecond());
	}

	public static BigInteger deserialize(int size, Object obj) {
		var str = obj.toString();
		if (str.startsWith("0x")) {
			str = str.substring(2);
		}
		return new BigInteger(str, 16);
	}

	@Override
	public Object serialize() {
		return "0x" + bigInteger().toString(16);
	}


}
