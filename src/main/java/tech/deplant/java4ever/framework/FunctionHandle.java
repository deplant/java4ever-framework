package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.utils.Numbers;
import tech.deplant.java4ever.utils.Objs;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

import static tech.deplant.java4ever.framework.LogUtils.*;

public record FunctionHandle<RETURN>(Class<RETURN> clazz,
                                     Contract contract,
                                     String functionName,
                                     Map<String, Object> functionInputs,
                                     Abi.FunctionHeader functionHeader) {

	private static System.Logger logger = System.getLogger(FunctionHandle.class.getName());

	public FunctionHandle(Class<RETURN> clazz,
	                      Sdk sdk,
	                      String address,
	                      ContractAbi abi,
	                      Credentials credentials,
	                      String functionName,
	                      Map<String, Object> functionInputs,
	                      Abi.FunctionHeader functionHeader) {
		this(clazz, new AbstractContract(sdk, address, abi, credentials), functionName, functionInputs, functionHeader);
	}

	public FunctionHandle(Sdk sdk,
	                      String address,
	                      ContractAbi abi,
	                      Credentials credentials,
	                      String functionName,
	                      Map<String, Object> functionInputs,
	                      Abi.FunctionHeader functionHeader) {
		this((Class<RETURN>) new HashMap<String, Object>().getClass(),
		     new AbstractContract(sdk, address, abi, credentials),
		     functionName,
		     functionInputs,
		     functionHeader);
	}

	public FunctionHandle<RETURN> withCredentials(Credentials credentials) {
		return new FunctionHandle<>(clazz(),
		                            new AbstractContract(contract().sdk(),
		                                                 contract().address(),
		                                                 contract().abi(),
		                                                 credentials),
		                            functionName(),
		                            functionInputs(),
		                            functionHeader());
	}

	public FunctionHandle<RETURN> withFunctionInputs(Map<String, Object> functionInputs) {
		return new FunctionHandle<>(clazz(), contract(), functionName(), functionInputs, functionHeader());
	}

	public FunctionHandle<RETURN> withFunctionHeader(Abi.FunctionHeader functionHeader) {
		return new FunctionHandle<>(clazz(), contract(), functionName(), functionInputs(), functionHeader);
	}

	public <T> FunctionHandle<T> withReturnClass(Class<T> returnClass) {
		return new FunctionHandle<>(returnClass, contract(), functionName(), functionInputs(), functionHeader());
	}

	public Abi.CallSet toCallSet() throws EverSdkException {
		Map<String, Object> converted = contract().abi().convertFunctionInputs(functionName(), functionInputs());
		return new Abi.CallSet(functionName(), functionHeader(), JsonContext.ABI_JSON_MAPPER().valueToTree(converted));
	}

	public RETURN toOutput(JsonNode outputMap) throws EverSdkException {
		Map<String, Object> converted = new HashMap<>();
		try {
			converted = contract().abi()
			                      .convertFunctionOutputs(functionName(),
			                                              JsonContext.readAsMap(JsonContext.ABI_JSON_MAPPER(),
			                                                                    outputMap));
			return JsonContext.ABI_JSON_MAPPER().convertValue(converted, clazz());
		} catch (IOException e) {
			try {
				error(logger,
				      String.format("Original: %s, Converted: %s",
				                    JsonContext.ABI_JSON_MAPPER().writeValueAsString(outputMap),
				                    JsonContext.ABI_JSON_MAPPER().writeValueAsString(converted)));
				throw new RuntimeException(e);
			} catch (JsonProcessingException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Encodes internal message string. Result of this method can be used as a payload for internal transactions
	 * to pass function calls and inputs with transfer.
	 *
	 * @return TvmCell of the internal call payload
	 * @throws EverSdkException
	 */
	public TvmCell toPayload() throws EverSdkException {
		return TvmCell.fromJava(Abi.encodeMessageBody(contract().sdk().context(),
		                                              contract().abi().ABI(),
		                                              toCallSet(),
		                                              true,
		                                              toSigner(),
		                                              null,
		                                              contract().address(),
		                                              null).body());
	}

	public Abi.Signer toSigner() {
		return Objs.notNullElse(contract().credentials(), Credentials.NONE).signer();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @throws EverSdkException
	 */
	public JsonNode getAsMap() throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new Account.GraphQLFilter.In(new String[]{contract().address()}));
		Net.ResultOfQueryCollection result = Net.queryCollection(contract().sdk().context(),
		                                                         "accounts",
		                                                         JsonContext.ABI_JSON_MAPPER().valueToTree(filter),
		                                                         "id boc",
		                                                         null,
		                                                         null);
		Abi.ResultOfEncodeMessage msg = Abi.encodeMessage(contract().sdk().context(),
		                                                  contract().abi().ABI(),
		                                                  contract().address(),
		                                                  null,
		                                                  toCallSet(),
		                                                  toSigner(),
		                                                  null,
		                                                  null);
		for (var map : result.result()) {
			String boc = map.get("boc").asText();
			return Optional.ofNullable(Tvm.runTvm(contract().sdk().context(),
			                                      msg.message(),
			                                      boc,
			                                      null,
			                                      contract().abi().ABI(),
			                                      null,
			                                      false).decoded().output()).orElse(JsonContext.EMPTY_NODE());
		}
		return JsonContext.EMPTY_NODE();
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
		return toOutput(getAsMap());
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method uses provided boc!
	 * There's no guarantee that it corresponds to current blockchain state.
	 *
	 * @throws EverSdkException
	 */
	public RETURN getLocal(String boc) throws EverSdkException {
		return toOutput(getLocalAsMap(boc));
	}

	/**
	 * Encodes inputs and runs getter method on account's boc then decodes answer.
	 * Important! When you run getter locally, directly on Account boc,
	 * there's no guarantee that it corresponds to current blockchain state.
	 */
	public JsonNode getLocalAsMap(String boc) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg = Abi.encodeMessage(contract().sdk().context(),
		                                                  contract().abi().ABI(),
		                                                  contract().address(),
		                                                  null,
		                                                  toCallSet(),
		                                                  toSigner(),
		                                                  null,
		                                                  null);
		return Optional.ofNullable(Tvm.runTvm(contract().sdk().context(),
		                                      msg.message(),
		                                      boc,
		                                      null,
		                                      contract().abi().ABI(),
		                                      null,
		                                      false).decoded().output()).orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Encodes inputs and runs external call method on account's boc then decodes answer.
	 * Important! When you run external call locally, directly on Account boc,
	 * your blockchain real info remains unchanged.
	 */
	public JsonNode callLocalAsMap(String boc,
	                               Tvm.ExecutionOptions options,
	                               boolean unlimitedBalance) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg = Abi.encodeMessage(contract().sdk().context(),
		                                                  contract().abi().ABI(),
		                                                  contract().address(),
		                                                  null,
		                                                  toCallSet(),
		                                                  toSigner(),
		                                                  null,
		                                                  null);
		return Optional.ofNullable(Tvm.runExecutor(contract().sdk().context(),
		                                           msg.message(),
		                                           new Tvm.AccountForExecutor.Account(boc, unlimitedBalance),
		                                           options,
		                                           contract().abi().ABI(),
		                                           false,
		                                           null,
		                                           true).decoded().output()).orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Calls smart contract with external message using credentials provided
	 * on initialization.
	 *
	 * @throws EverSdkException
	 */
	public RETURN callLocal(String boc,
	                        Tvm.ExecutionOptions options,
	                        boolean unlimitedBalance) throws EverSdkException {
		return toOutput(callLocalAsMap(boc, options, unlimitedBalance));
	}

	public JsonNode callAsMap() throws EverSdkException {
		var resultOfProcess = processExternalCall();
		var balanceDeltaStr = Numbers.hexStringToBigDec(resultOfProcess.transaction().get("balance_delta").asText(), 9);
		Supplier<String> lazyFormatLogMessage = () -> String.format(LogUtils.CALL_LOG_BLOCK,
		                                                            "EXTERNAL CALL",
		                                                            this.functionName,
		                                                            resultOfProcess.transaction().get("id").asText(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("in_msg")
		                                                                           .asText(),
		                                                            "ext",
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalFwdFees())
		                                                                               .toJava(), 9).toPlainString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("account_addr")
		                                                                           .asText(),
		                                                            0,
		                                                            this.functionName,
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalAccountFees())
		                                                                               .toJava(), 9).toPlainString(),
		                                                            "");
		info(logger, lazyFormatLogMessage);
		return Optional.ofNullable(resultOfProcess.decoded().output()).orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Calls smart contract with external message using credentials provided
	 * on initialization.
	 *
	 * @throws EverSdkException
	 */
	public RETURN call() throws EverSdkException {
		return toOutput(callAsMap());
	}

	/**
	 * As with call(), this method send ext message to Everscale
	 * but callTree() also queries message tree with "net.query_transaction_tree"
	 * returns a set of messages and transactions, logs everything and throws exceptions on errors
	 * encountered in a tree.
	 *
	 * @param throwOnTreeError   If 'true' method will throw on any internal non-0 exit_code encountered in tree.
	 * @param otherAbisForDecode Method will try to decode each message against ABIs in this list. ABI of entering contract already included.
	 * @return
	 * @throws EverSdkException
	 */
	public ResultOfTree<JsonNode> callTreeAsMap(boolean throwOnTreeError,
	                                            ContractAbi... otherAbisForDecode) throws EverSdkException {
		Abi.ABI[] finalABIArray = Arrays.stream(concatAbiSet(otherAbisForDecode, contract().abi()))
		                                .map(ContractAbi::ABI)
		                                .toArray(Abi.ABI[]::new);
		var resultOfProcess = processExternalCall();
		var msgId = resultOfProcess.transaction().get("in_msg").toString();
		var debugOutResult = Net.queryTransactionTree(contract().sdk().context(),
		                                              msgId,
		                                              finalABIArray,
		                                              contract().sdk().debugTreeTimeout(),
		                                              0L);
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
			                                                            Numbers.hexStringToBigDec(msg.value(), 9)
			                                                                   .toPlainString(),
			                                                            LogUtils.destOfMessage(msg),
			                                                            tr.exitCode(),
			                                                            LogUtils.nameOfMessage(msg),
			                                                            Numbers.hexStringToBigDec(tr.totalFees(), 9)
			                                                                   .toPlainString(),
			                                                            LogUtils.enquotedListAgg(tr.outMsgs()));
			if (tr.aborted() && throwOnTreeError) {
				error(logger, lazyFormatLogMessage);
				if (Objs.isNull(tr.exitCode())) {
					throw new EverSdkException(new EverSdkException.ErrorResult(-404, tr.toString()),
					                           "ABORTED! Exit code: -404. Target contract " +
					                           LogUtils.destOfMessage(msg) + " is not deployed!");
				} else {
					throw new EverSdkException(new EverSdkException.ErrorResult(tr.exitCode(), tr.toString()),
					                           "ABORTED! Exit code: %s. Transaction: %s".formatted(tr.exitCode()
					                                                                                 .toString(),
					                                                                               tr.id()));
				}
			} else if (tr.aborted()) {
				warn(logger, lazyFormatLogMessage);
			} else {
				info(logger, lazyFormatLogMessage);
			}
		}
		var map = Optional.ofNullable(resultOfProcess.decoded().output()).orElse(JsonContext.EMPTY_NODE());
		return new ResultOfTree<>(debugOutResult, map);
	}

	/**
	 * As with call(), this method send ext message to Everscale
	 * but callTree() also queries message tree with "net.query_transaction_tree"
	 * returns a set of messages and transactions, logs everything and throws exceptions on errors
	 * encountered in a tree.
	 *
	 * @param throwOnTreeError   If 'true' method will throw on any internal non-0 exit_code encountered in tree.
	 * @param otherAbisForDecode Method will try to decode each message against ABIs in this list. ABI of entering contract already included.
	 * @return
	 * @throws EverSdkException
	 */
	public ResultOfTree<RETURN> callTree(boolean throwOnTreeError,
	                                     ContractAbi... otherAbisForDecode) throws EverSdkException {
		var result = callTreeAsMap(throwOnTreeError, otherAbisForDecode);
		return new ResultOfTree<>(result.queryTree(), toOutput(result.decodedOutput()));
	}

	public JsonNode sendFromAsMap(MultisigWallet sender,
	                              BigInteger value,
	                              boolean bounce,
	                              MessageFlag flag) throws EverSdkException, JsonProcessingException {
		return sender.sendTransaction(new Address(contract().address()), value, bounce, flag.flag(), toPayload())
		             .callAsMap();
	}

	public JsonNode sendFromAsMap(MultisigWallet sender,
	                              BigInteger value) throws EverSdkException, JsonProcessingException {
		return sendFromAsMap(sender, value, true, MessageFlag.EXACT_VALUE_GAS);
	}

	public RETURN sendFrom(MultisigWallet sender,
	                       BigInteger value,
	                       boolean bounce,
	                       MessageFlag flag) throws EverSdkException, JsonProcessingException {
		return toOutput(sendFromAsMap(sender, value, bounce, flag));
	}

	public RETURN sendFrom(MultisigWallet sender, BigInteger value) throws EverSdkException, JsonProcessingException {
		return toOutput(sendFromAsMap(sender, value));
	}

	public ResultOfTree<JsonNode> sendFromTreeAsMap(MultisigWallet sender,
	                                                BigInteger value,
	                                                boolean bounce,
	                                                MessageFlag flag,
	                                                boolean throwOnTreeError,
	                                                ContractAbi... otherAbisForDecode) throws EverSdkException, JsonProcessingException {
		return sender.sendTransaction(new Address(contract().address()), value, bounce, flag.flag(), toPayload())
		             .callTreeAsMap(throwOnTreeError,
		                            concatAbiSet(otherAbisForDecode,
		                                         contract().abi(),
		                                         SafeMultisigWalletTemplate.DEFAULT_ABI()));
	}

	public ResultOfTree<RETURN> sendFromTree(MultisigWallet sender,
	                                         BigInteger value,
	                                         boolean bounce,
	                                         MessageFlag flag,
	                                         boolean throwOnTreeError,
	                                         ContractAbi... otherAbisForDecode) throws EverSdkException, JsonProcessingException {
		var result = sendFromTreeAsMap(sender, value, bounce, flag, throwOnTreeError, otherAbisForDecode);
		return new ResultOfTree<>(result.queryTree(), toOutput(result.decodedOutput()));
	}

	private ContractAbi[] concatAbiSet(ContractAbi[] currentAbis, ContractAbi... moreAbis) {
		Set<ContractAbi> abiSet = new HashSet<>();
		for (var contractAbi : currentAbis) {
			abiSet.add(contractAbi);
		}
		for (var contractAbi : moreAbis) {
			abiSet.add(contractAbi);
		}
		return abiSet.toArray(ContractAbi[]::new);
	}

	private Processing.ResultOfProcessMessage processExternalCall() throws EverSdkException {
		return Processing.processMessage(contract().sdk().context(),
		                                 contract().abi().ABI(),
		                                 contract().address(),
		                                 null,
		                                 toCallSet(),
		                                 toSigner(),
		                                 null,
		                                 null,
		                                 false);
	}

}
