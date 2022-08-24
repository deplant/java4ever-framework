package tech.deplant.java4ever.framework.abi.type;

import java.math.BigInteger;
import java.time.Instant;

public record AbiUint(BigInteger bigInteger) implements AbiValue {

    public AbiUint(Long longVal) {
        this(BigInteger.valueOf(longVal));
    }

    public AbiUint(Instant instant) {
        this(instant.getEpochSecond());
    }

    @Override
    public Object serialize() {
        return "0x" + bigInteger().toString(16);
    }
}
