package tech.deplant.java4ever.framework.contract.multisig2;

import com.fasterxml.jackson.annotation.JsonCreator;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Optional;

public abstract class MultisigContract2 extends GiverContract {

	@JsonCreator
	public MultisigContract2(Sdk sdk, String address, ContractAbi abi, Credentials credentials) {
		super(sdk,address,abi,credentials);
	}

	@Override
	public FunctionHandle<Void> give(String to, BigInteger value) {
		return sendTransaction(new Address(to), value, false,
		                       1, TvmCell.EMPTY);
	}


	public abstract FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                            Integer flags, TvmCell payload);

	@Override
	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		return sendTransaction(dest, value, bounce, MessageFlag.FEE_EXTRA.flag(), TvmCell.EMPTY);
	}

	public abstract FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest,
	                                                                   BigInteger value,
	                                                                   Boolean bounce,
	                                                                   Boolean allBalance,
	                                                                   TvmCell payload,
	                                                                   Optional<TvmCell> stateInit);

	public abstract FunctionHandle<Void> confirmTransaction(BigInteger transactionId);

	public abstract FunctionHandle<ResultOfIsConfirmed> isConfirmed(Long mask, Integer index);

	public abstract FunctionHandle<ResultOfGetParameters> getParameters();

	public abstract FunctionHandle<ResultOfGetTransaction> getTransaction(BigInteger transactionId);

	public abstract FunctionHandle<ResultOfGetTransactions> getTransactions();

	public abstract FunctionHandle<ResultOfGetCustodians> getCustodians();

}
