package tech.deplant.java4ever.framework.contract.multisig;

import com.fasterxml.jackson.annotation.JsonCreator;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;

public abstract class MultisigContract extends GiverContract {

	@JsonCreator
	public MultisigContract(int sdk, String address, ContractAbi abi, Credentials credentials) {
		super(sdk, address, abi, credentials);
	}

	public abstract FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                                     Integer flags, TvmCell payload);

	@Override
	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		return sendTransaction(dest, value, bounce, MessageFlag.FEE_EXTRA.flag(), TvmCell.EMPTY);
	}

	@Override
	public FunctionHandle<Void> give(Address to, BigInteger value) {
		return sendTransaction(to, value, false,
		                       1, TvmCell.EMPTY);
	}

	public abstract FunctionHandle<Void> confirmTransaction(BigInteger transactionId);

	public abstract FunctionHandle<ResultOfIsConfirmed> isConfirmed(Long mask, Integer index);

	public abstract FunctionHandle<ResultOfGetParameters> getParameters();

	public abstract FunctionHandle<ResultOfGetCustodians> getCustodians();

	public abstract FunctionHandle<Void> acceptTransfer(Byte[] payload);

	public abstract FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest,
	                                                                            BigInteger value,
	                                                                            Boolean bounce,
	                                                                            Boolean allBalance,
	                                                                            TvmCell payload);

	public abstract FunctionHandle<ResultOfGetTransaction> getTransaction(BigInteger transactionId);

	public abstract FunctionHandle<ResultOfGetTransactions> getTransactions();

	public abstract FunctionHandle<ResultOfGetTransactionIds> getTransactionIds();

	public enum Type {
		SURF,
		SAFE,
		SETCODE;
	}

}
