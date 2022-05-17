package tech.deplant.java4ever.framework.type;

import java.math.BigInteger;

public record TokenAmount(BigInteger amount, Integer decimals) implements FungibleCurrency {
    @Override
    public int compareTo(FungibleCurrency o) {
        return this.amount().compareTo(o.amount());
    }
}
