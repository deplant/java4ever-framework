package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;

public interface Giver {
    public void give(Address to, BigInteger amount) throws Sdk.SdkException;
}
