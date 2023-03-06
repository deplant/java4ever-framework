package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;

public interface MultisigWallet extends Giver {
	@Override
	default FunctionHandle<Void> give(String to, BigInteger value) {
		return sendTransaction(new Address(to), value, false,
		                       1, TvmCell.EMPTY());
	}

	FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                     Integer flags, TvmCell payload);
}
