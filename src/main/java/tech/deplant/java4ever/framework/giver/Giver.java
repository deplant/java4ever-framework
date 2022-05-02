package tech.deplant.java4ever.framework.giver;

import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;

import java.math.BigInteger;

public interface Giver {
    public void give(Address to, BigInteger amount) throws Sdk.SdkException;
}
