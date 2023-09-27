package tech.deplant.java4ever.framework.contract.multisig2;

import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.multisig.MultisigWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

public interface MultisigWallet2 extends Giver {

	@Override
	default FunctionHandle<Void> give(String to, BigInteger value) {
		return sendTransaction(new Address(to), value, false,
		                       1, TvmCell.EMPTY);
	}

	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                            Integer flags, TvmCell payload);

	public FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
	                                                                   Boolean bounce, Boolean allBalance, TvmCell payload, Optional<TvmCell> stateInit);

	public FunctionHandle<Void> confirmTransaction(BigInteger transactionId);

	public FunctionHandle<ResultOfIsConfirmed> isConfirmed(Long mask, Integer index);

	public FunctionHandle<ResultOfGetParameters> getParameters();

	public FunctionHandle<ResultOfGetTransaction> getTransaction(BigInteger transactionId);

	public FunctionHandle<ResultOfGetTransactions> getTransactions();

	public FunctionHandle<ResultOfGetCustodians> getCustodians();

}
