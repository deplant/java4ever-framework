package tech.deplant.java4ever.framework.contract.multisig2;

import com.fasterxml.jackson.annotation.JsonCreator;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Optional;

/**
 * The type Multisig contract 2.
 */
public abstract class MultisigContract2 extends GiverContract {

	/**
	 * Instantiates a new Multisig contract 2.
	 *
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param abi         the abi
	 * @param credentials the credentials
	 */
	@JsonCreator
	public MultisigContract2(int sdk, String address, ContractAbi abi, Credentials credentials) {
		super(sdk, address, abi, credentials);
	}

	/**
	 * Send transaction function handle.
	 *
	 * @param dest    the dest
	 * @param value   the value
	 * @param bounce  the bounce
	 * @param flags   the flags
	 * @param payload the payload
	 * @return the function handle
	 */
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

	/**
	 * Submit transaction function handle.
	 *
	 * @param dest       the dest
	 * @param value      the value
	 * @param bounce     the bounce
	 * @param allBalance the all balance
	 * @param payload    the payload
	 * @param stateInit  the state init
	 * @return the function handle
	 */
	public abstract FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest,
	                                                                            BigInteger value,
	                                                                            Boolean bounce,
	                                                                            Boolean allBalance,
	                                                                            TvmCell payload,
	                                                                            Optional<TvmCell> stateInit);

	/**
	 * Confirm transaction function handle.
	 *
	 * @param transactionId the transaction id
	 * @return the function handle
	 */
	public abstract FunctionHandle<Void> confirmTransaction(BigInteger transactionId);

	/**
	 * Is confirmed function handle.
	 *
	 * @param mask  the mask
	 * @param index the index
	 * @return the function handle
	 */
	public abstract FunctionHandle<ResultOfIsConfirmed> isConfirmed(Long mask, Integer index);

	/**
	 * Gets parameters.
	 *
	 * @return the parameters
	 */
	public abstract FunctionHandle<ResultOfGetParameters> getParameters();

	/**
	 * Gets transaction.
	 *
	 * @param transactionId the transaction id
	 * @return the transaction
	 */
	public abstract FunctionHandle<ResultOfGetTransaction> getTransaction(BigInteger transactionId);

	/**
	 * Gets transactions.
	 *
	 * @return the transactions
	 */
	public abstract FunctionHandle<ResultOfGetTransactions> getTransactions();

	/**
	 * Gets custodians.
	 *
	 * @return the custodians
	 */
	public abstract FunctionHandle<ResultOfGetCustodians> getCustodians();

}
