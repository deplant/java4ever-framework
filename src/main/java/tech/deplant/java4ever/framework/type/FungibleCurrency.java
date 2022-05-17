package tech.deplant.java4ever.framework.type;

import java.math.BigInteger;

public interface FungibleCurrency extends Comparable<FungibleCurrency> {
    BigInteger amount();

    Integer decimals();
}
