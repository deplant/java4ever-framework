package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Convert;
import tech.deplant.java4ever.framework.LogUtils;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Uint;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;
import static tech.deplant.java4ever.framework.LogUtils.*;

/**
 * Class that represends deployed contract in one of the networks. It holds info about
 * network (sdk), address and abi of contract. If you own this contract, initialize it
 * with correct credentials. If it's foreign contract, use Credentials.NONE.
 */
public class OwnedContract {

	private static System.Logger logger = System.getLogger(OwnedContract.class.getName());
	protected final Sdk sdk;
	protected final String address;
	protected final ContractAbi abi;
	protected final Credentials credentials;

	public OwnedContract(Sdk sdk, String address, ContractAbi abi, Credentials credentials) {
		this.sdk = sdk;
		this.address = address;
		this.abi = abi;
		this.credentials = credentials;
	}

	public OwnedContract(Sdk sdk, String address, ContractAbi abi) {
		this(sdk, address, abi, Credentials.NONE);
	}

	public Sdk sdk() {
		return this.sdk;
	}

	public String address() {
		return this.address;
	}

	public ContractAbi abi() {
		return this.abi;
	}

	/**
	 * Check actual EVER balance on contract's account.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	public BigInteger balance() throws EverSdkException {
		return Uint.fromJava(128, account().balance()).toJava();
	}

	/**
	 * Downloads actual account info, including boc.
	 * Use account().boc() to get it.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	public Account account() throws EverSdkException {
		return Account.ofAddress(this.sdk, this.address);
	}

	/**
	 * Credentials that were provided in object constructor. They can be different from real pubkey
	 * inside contract's inside contract's initialData. To check real pubkey in account, use
	 * tvmPubkey() method.
	 *
	 * @return
	 */
	public Credentials credentials() {
		return this.credentials;
	}

	/**
	 * Returns actual tvm.pubkey() of smart contract. If you want to get Credentials specified at
	 * OwnedContract constructor - use credentials() method.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	public String tvmPubkey() throws EverSdkException {
		return account().tvmPubkey(sdk(), abi());
	}

	/**
	 * Encodes internal message string. Result of this method can be used as a payload for internal transactions
	 * to pass function calls and inputs with transfer.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @return
	 * @throws EverSdkException
	 */
	public String encodeInternalPayload(String functionName,
	                                    Map<String, Object> functionInputs,
	                                    Abi.FunctionHeader functionHeader) throws EverSdkException {
		return Abi.encodeMessageBody(
				sdk().context(),
				abi().ABI(),
				new Abi.CallSet(functionName,
				                functionHeader,
				                abi().convertFunctionInputs(functionName, functionInputs)),
				true,
				Credentials.NONE.signer(),
				null,
				address()
		).body();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @param credentials
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader,
	                                     Credentials credentials) throws EverSdkException {
		return account().runGetter(sdk(),
		                           abi(),
		                           functionName,
		                           functionInputs,
		                           functionHeader,
		                           credentials);
	}


	private Processing.ResultOfProcessMessage processExternalCall(String functionName,
	                                                              Map<String, Object> functionInputs,
	                                                              Abi.FunctionHeader functionHeader,
	                                                              Credentials credentials) throws EverSdkException {
		return Processing.processMessage(this.sdk.context(),
		                                 abi().ABI(),
		                                 address(),
		                                 null,
		                                 new Abi.CallSet(functionName,
		                                                 functionHeader,
		                                                 abi().convertFunctionInputs(functionName,
		                                                                             functionInputs)),
		                                 requireNonNullElse(credentials, Credentials.NONE).signer(), null, false, null);
	}

	/**
	 * As with callExternal(), this method send ext message to Everscale
	 * but callExternalDebugTree() also queries message tree with "net.query_transaction_tree"
	 * returns a set of messages and transactions, logs everything and throws exceptions on errors
	 * encountered in a tree.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @param credentials
	 * @param debugQueryTimeout      Transaction tree query will fail if exceeds this timeout. Useful if you query large trees.
	 * @param debugThrowOnTreeErrors If 'true' method will throw on any internal non-0 exit_code encountered in tree.
	 * @param debugAbisForDecode     Method will try to decode each message against ABIs in this list. ABI of entering contract already included.
	 * @return
	 * @throws EverSdkException
	 */
	public ResultOfQueryTransactionTreeAndCallOutput callExternalDebugTree(String functionName,
	                                                                       Map<String, Object> functionInputs,
	                                                                       Abi.FunctionHeader functionHeader,
	                                                                       Credentials credentials,
	                                                                       Long debugQueryTimeout,
	                                                                       boolean debugThrowOnTreeErrors,
	                                                                       List<ContractAbi> debugAbisForDecode) throws EverSdkException {

		Abi.ABI[] abiArray = Stream
				.concat(Stream.of(abi()), debugAbisForDecode.stream()) // adding THIS contract abi to decode list
				.map(ContractAbi::ABI).
				toArray(Abi.ABI[]::new);
		long debugTimeout = Optional.ofNullable(debugQueryTimeout).orElse(30_000L); // default is 30 sec
		var resultOfProcess = processExternalCall(functionName,
		                                          functionInputs,
		                                          functionHeader,
		                                          credentials);
		var msgId = resultOfProcess.transaction().get("in_msg").toString();
		var debugOutResult = Net.queryTransactionTree(sdk().context(),
		                                              msgId,
		                                              abiArray,
		                                              debugTimeout);
		for (Net.TransactionNode tr : debugOutResult.transactions()) {
			var msg = Arrays.stream(debugOutResult.messages())
			                .filter(msgElem -> msgElem.id().equals(tr.inMsg()))
			                .findFirst()
			                .get();
			Supplier<String> lazyFormatLogMessage = () -> String.format(LogUtils.CALL_LOG_BLOCK,
			                                                            LogUtils.typeOfMessage(msg),
			                                                            LogUtils.nameOfMessage(msg),
			                                                            tr.id(),
			                                                            msg.id(),
			                                                            LogUtils.sourceOfMessage(msg),
			                                                            Convert.hexToDecOrZero(msg.value(), 9)
			                                                                   .toPlainString(),
			                                                            LogUtils.destOfMessage(msg),
			                                                            tr.exitCode().intValue(),
			                                                            LogUtils.nameOfMessage(msg),
			                                                            Convert.hexToDecOrZero(tr.totalFees(), 9)
			                                                                   .toPlainString(),
			                                                            LogUtils.enquotedListAgg(tr.outMsgs()));
			if (tr.aborted() && debugThrowOnTreeErrors) {
				error(logger, lazyFormatLogMessage);
				throw new EverSdkException(new EverSdkException.ErrorResult(tr.exitCode().intValue(),
				                                                            "One of the message tree transaction was aborted!"),
				                           new Exception());
			} else if (tr.aborted()) {
				warn(logger, lazyFormatLogMessage);
			} else {
				info(logger, lazyFormatLogMessage);
			}
		}
		return new ResultOfQueryTransactionTreeAndCallOutput(debugOutResult, resultOfProcess.decoded().output());
	}

	/**
	 * Calls smart contract with external message.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @param credentials
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader,
	                                        Credentials credentials) throws EverSdkException {
		var resultOfProcess = processExternalCall(functionName,
		                                          functionInputs,
		                                          functionHeader,
		                                          credentials);
		var balanceDeltaStr = Convert.hexToDec(resultOfProcess.transaction().get("balance_delta").toString(), 9);
		Supplier<String> lazyFormatLogMessage = () -> String.format(LogUtils.CALL_LOG_BLOCK,
		                                                            "EXTERNAL CALL",
		                                                            functionName,
		                                                            resultOfProcess.transaction().get("id").toString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("in_msg")
		                                                                           .toString(),
		                                                            "ext",
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalFwdFees())
		                                                                               .toJava(), 9)
				                                                            .toPlainString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("account_addr")
		                                                                           .toString(),
		                                                            0,
		                                                            functionName,
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalAccountFees())
		                                                                               .toJava(), 9)
				                                                            .toPlainString(),
		                                                            "");
		info(logger, lazyFormatLogMessage);
		return Optional.ofNullable(resultOfProcess
				                           .decoded()
				                           .output()).orElse(new HashMap<>());
	}

	/**
	 * Calls smart contract with external message using credentials provided
	 * on OwnedContract initialization.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader) throws EverSdkException {
		return callExternal(functionName, functionInputs, functionHeader, this.credentials);
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer
	 * using credentials provided at OwnedContract initialization.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader) throws EverSdkException {
		return runGetter(functionName, functionInputs, functionHeader, this.credentials);
	}

	public record ResultOfQueryTransactionTreeAndCallOutput(Net.ResultOfQueryTransactionTree queryTree,
	                                                        Map<String, Object> decodedOutuput) {
	}
}
