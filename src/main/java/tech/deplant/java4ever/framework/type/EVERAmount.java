package tech.deplant.java4ever.framework.type;

import java.math.BigInteger;

public record EVERAmount(BigInteger amount) implements FungibleCurrency {
    public static final EVERAmount NANOTON = new EVERAmount(new BigInteger("1")); // A1 == 1
    public static final EVERAmount NANOEVER = new EVERAmount(new BigInteger("1"));// A1 == 1_== 1E-9 EVER
    public static final EVERAmount MICROTON = new EVERAmount(new BigInteger("1000")); // A6 == 1_000
    public static final EVERAmount MICROEVER = new EVERAmount(new BigInteger("1000")); // A6 == 1_000 == 1E-6 EVER
    public static final EVERAmount MILLITON = new EVERAmount(new BigInteger("1000000")); // A8 == 1_000 000
    public static final EVERAmount MILLIEVER = new EVERAmount(new BigInteger("1000000")); // A8 == 1_000 000 == 1E-3 EVER
    public static final EVERAmount TON = new EVERAmount(new BigInteger("1000000000")); // A3 == 1_000 000 000 (1E9)
    public static final EVERAmount EVER = new EVERAmount(new BigInteger("1000000000")); // A4 == 1_000 000 000 (1E9)
    public static final EVERAmount KILOTON = new EVERAmount(new BigInteger("1000000000000")); // A9 == 1_000 000 000 000 (1E12)
    public static final EVERAmount KILOEVER = new EVERAmount(new BigInteger("1000000000000")); // A9 == 1_000 000 000 000 (1E12) == 1E3 EVER
    public static final EVERAmount MEGATON = new EVERAmount(new BigInteger("1000000000000000")); // A11 == 1_000 000 000 000 000 (1E15)
    public static final EVERAmount MEGAEVER = new EVERAmount(new BigInteger("1000000000000000")); // A11 == 1_000 000 000 000 000 (1E15) == 1E6 EVER
    public static final EVERAmount GIGATON = new EVERAmount(new BigInteger("1000000000000000000")); // A13 == 1_000 000 000 000 000 000 (1E18)
    public static final EVERAmount GIGAEVER = new EVERAmount(new BigInteger("1000000000000000000")); // A13 == 1_000 000 000 000 000 000 (1E18) == 1E9 EVER

    @Override
    public int compareTo(FungibleCurrency o) {
        return this.amount().compareTo(o.amount());
    }

    @Override
    public Integer decimals() {
        return 9;
    }
}
