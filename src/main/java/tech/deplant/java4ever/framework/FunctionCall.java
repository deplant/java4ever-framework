package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.type.TypeReference;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.datatype.Uint;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;
import static tech.deplant.java4ever.framework.LogUtils.*;

public record FunctionCall<RETURN>(Sdk sdk,
                                   String address,
                                   ContractAbi abi,
                                   Credentials credentials,
                                   String functionName,
                                   Map<String, Object> functionInputs,
                                   Abi.FunctionHeader functionHeader) {

	private static System.Logger logger = System.getLogger(FunctionCall.class.getName());

	public FunctionCall<RETURN> withSdk(Sdk sdk) {
		return new FunctionCall<>(sdk,
		                          address(),
		                          abi(),
		                          credentials(),
		                          functionName(),
		                          functionInputs(),
		                          functionHeader());
	}

	public FunctionCall<RETURN> withCredentials(Credentials credentials) {
		return new FunctionCall<>(sdk(),
		                          address(),
		                          abi(),
		                          credentials,
		                          functionName(),
		                          functionInputs(),
		                          functionHeader());
	}

	public FunctionCall<RETURN> withFunctionInputs(Map<String, Object> functionInputs) {
		return new FunctionCall<>(sdk(),
		                          address(),
		                          abi(),
		                          credentials(),
		                          functionName(),
		                          functionInputs,
		                          functionHeader());
	}

	public FunctionCall<RETURN> withFunctionHeader(Abi.FunctionHeader functionHeader) {
		return new FunctionCall<>(sdk(),
		                          address(),
		                          abi(),
		                          credentials(),
		                          functionName(),
		                          functionInputs(),
		                          functionHeader);
	}

	public <T> FunctionCall<T> withReturnClass(Class<T> returnClass) {
		return new FunctionCall<>(sdk(),
		                          address(),
		                          abi(),
		                          credentials(),
		                          functionName(),
		                          functionInputs(),
		                          functionHeader());
	}

	public Abi.CallSet toCallSet() throws EverSdkException {
		return new Abi.CallSet(functionName(),
		                       functionHeader(),
		                       abi().convertFunctionInputs(functionName(), functionInputs()));
	}

	public Abi.Signer toSigner() {
		return requireNonNullElse(credentials(), Credentials.NONE).signer();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @throws EverSdkException
	 */
	public Map<String, Object> getAsMap() throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new Account.GraphQLFilter.In(new String[]{address()}));
		Net.ResultOfQueryCollection result = Net.queryCollection(sdk().context(),
		                                                         "accounts",
		                                                         filter,
		                                                         "id acc_type balance boc data data_hash code code_hash init_code_hash last_paid",
		                                                         null,
		                                                         null);
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						sdk().context(),
						abi().ABI(),
						address(),
						null,
						toCallSet(),
						toSigner(),
						null
				);
		String boc = result.result()[0].get("boc").toString();
		return Optional.ofNullable(Tvm.runTvm(
				                              sdk().context(),
				                              msg.message(),
				                              boc,
				                              null,
				                              abi().ABI(),
				                              null,
				                              false).decoded()
		                              .output()).orElse(new HashMap<>());
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @throws EverSdkException
	 */
	public RETURN get() throws EverSdkException {
		return sdk().convertMap(getAsMap(), new TypeReference<>() {
		});
	}

	public Map<String, Object> callAsMap() throws EverSdkException {
		var resultOfProcess = processExternalCall();
		var balanceDeltaStr = Convert.hexToDec(resultOfProcess.transaction().get("balance_delta").toString(), 9);
		Supplier<String> lazyFormatLogMessage = () -> String.format(LogUtils.CALL_LOG_BLOCK,
		                                                            "EXTERNAL CALL",
		                                                            this.functionName,
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
		                                                            this.functionName,
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
	 * on initialization.
	 *
	 * @throws EverSdkException
	 */
	public RETURN call() throws EverSdkException {
		return sdk().convertMap(callAsMap(), new TypeReference<>() {
		});
	}

	/**
	 * As with call(), this method send ext message to Everscale
	 * but callTree() also queries message tree with "net.query_transaction_tree"
	 * returns a set of messages and transactions, logs everything and throws exceptions on errors
	 * encountered in a tree.
	 *
	 * @param debugThrowOnTreeErrors If 'true' method will throw on any internal non-0 exit_code encountered in tree.
	 * @param otherAbisForDecode     Method will try to decode each message against ABIs in this list. ABI of entering contract already included.
	 * @return
	 * @throws EverSdkException
	 */
	public ResultOfTree<Map<String, Object>> callTreeAsMap(boolean debugThrowOnTreeErrors,
	                                                       List<ContractAbi> otherAbisForDecode) throws EverSdkException {
		Abi.ABI[] abiArray = Stream
				.concat(Stream.of(abi()),
				        otherAbisForDecode.stream()) // adding THIS contract abi to decode list
				.map(ContractAbi::ABI).
				toArray(Abi.ABI[]::new);
		var resultOfProcess = processExternalCall();
		var msgId = resultOfProcess.transaction().get("in_msg").toString();
		var debugOutResult = Net.queryTransactionTree(sdk().context(),
		                                              msgId,
		                                              abiArray,
		                                              sdk().debugTreeTimeout(),
		                                              0);
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
		var map = Optional.ofNullable(resultOfProcess
				                              .decoded()
				                              .output()).orElse(new HashMap<>());
		return new ResultOfTree<>(debugOutResult, map);
	}

	/**
	 * As with call(), this method send ext message to Everscale
	 * but callTree() also queries message tree with "net.query_transaction_tree"
	 * returns a set of messages and transactions, logs everything and throws exceptions on errors
	 * encountered in a tree.
	 *
	 * @param debugThrowOnTreeErrors If 'true' method will throw on any internal non-0 exit_code encountered in tree.
	 * @param otherAbisForDecode     Method will try to decode each message against ABIs in this list. ABI of entering contract already included.
	 * @return
	 * @throws EverSdkException
	 */
	public ResultOfTree<RETURN> callTree(boolean debugThrowOnTreeErrors,
	                                     List<ContractAbi> otherAbisForDecode) throws EverSdkException {
		var result = callTreeAsMap(debugThrowOnTreeErrors, otherAbisForDecode);
		return new ResultOfTree<>(result.queryTree(),
		                          sdk().convertMap(result.decodedOutuput(), new TypeReference<>() {
		                          }));
	}

	private Processing.ResultOfProcessMessage processExternalCall() throws EverSdkException {
		return Processing.processMessage(sdk().context(),
		                                 abi().ABI(),
		                                 address(),
		                                 null,
		                                 toCallSet(),
		                                 toSigner(),
		                                 null,
		                                 false);
	}

}
