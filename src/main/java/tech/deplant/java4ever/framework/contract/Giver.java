package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.FunctionHandle;

import java.math.BigInteger;

public interface Giver extends Contract {

	FunctionHandle<Void> give(String to, BigInteger value);

}
