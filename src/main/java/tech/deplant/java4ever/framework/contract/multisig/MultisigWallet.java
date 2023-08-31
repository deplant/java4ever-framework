package tech.deplant.java4ever.framework.contract.multisig;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.multisig.*;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.Map;

public interface MultisigWallet extends Giver {

	enum Type {
		SURF,
		SAFE,
		SETCODE;
	}

	@Override
	default FunctionHandle<Void> give(String to, BigInteger value) {
		return sendTransaction(new Address(to), value, false,
		                       1, TvmCell.EMPTY);
	}



	FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                     Integer flags, TvmCell payload);

	FunctionHandle<Void> confirmTransaction(Long transactionId);

	FunctionHandle<ResultOfIsConfirmed> isConfirmed(Integer mask, Integer index);

	FunctionHandle<ResultOfGetParameters> getParameters();

	FunctionHandle<ResultOfGetCustodians> getCustodians();


	FunctionHandle<Void> acceptTransfer(String payload);

	FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
	                                                            Boolean bounce, Boolean allBalance, TvmCell payload);

	FunctionHandle<ResultOfGetTransaction> getTransaction(Long transactionId);

	FunctionHandle<ResultOfGetTransactions> getTransactions() ;

	FunctionHandle<ResultOfGetTransactionIds> getTransactionIds();

}
