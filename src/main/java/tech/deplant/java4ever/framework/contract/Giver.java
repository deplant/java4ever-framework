package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.FunctionCall;

import java.math.BigInteger;

public interface Giver extends Contract {

	FunctionCall<Void> give(String to, BigInteger value);

}
