package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Numbers;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.contract.multisig2.MultisigContract2;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

import static tech.deplant.java4ever.framework.LogUtils.*;

/**
 * The type Function handle.
 *
 * @param <RETURN> the type parameter
 */
public record FunctionHandle<RETURN>(Class<RETURN> clazz,
                                     Contract contract,
                                     String functionName,
                                     Map<String, Object> functionInputs,
                                     Abi.FunctionHeader functionHeader,
                                     DebugOptions debugOptions,
                                     Abi.DeploySet deploySet) {

	private static System.Logger logger = System.getLogger(FunctionHandle.class.getName());

	/**
	 * Instantiates a new Function handle.
	 *
	 * @param clazz          the clazz
	 * @param contract       the contract
	 * @param functionName   the function name
	 * @param functionInputs the function inputs
	 * @param functionHeader the function header
	 */
	public FunctionHandle(Class<RETURN> clazz,
	                      Contract contract,
	                      String functionName,
	                      Map<String, Object> functionInputs,
	                      Abi.FunctionHeader functionHeader) {
		this(clazz,
		     contract,
		     functionName,
		     functionInputs,
		     functionHeader,
		     new DebugOptions(false, 60000L, false, 50L),
		     null);
	}

	/**
	 * Instantiates a new Function handle.
	 *
	 * @param clazz          the clazz
	 * @param sdk            the sdk
	 * @param address        the address
	 * @param abi            the abi
	 * @param credentials    the credentials
	 * @param functionName   the function name
	 * @param functionInputs the function inputs
	 * @param functionHeader the function header
	 */
	public FunctionHandle(Class<RETURN> clazz,
	                      int sdk,
	                      Address address,
	                      ContractAbi abi,
	                      Credentials credentials,
	                      String functionName,
	                      Map<String, Object> functionInputs,
	                      Abi.FunctionHeader functionHeader) {
		this(clazz, new AbstractContract(sdk, address, abi, credentials), functionName, functionInputs, functionHeader);
	}

	/**
	 * Instantiates a new Function handle.
	 *
	 * @param sdk            the sdk
	 * @param address        the address
	 * @param abi            the abi
	 * @param credentials    the credentials
	 * @param functionName   the function name
	 * @param functionInputs the function inputs
	 * @param functionHeader the function header
	 */
	public FunctionHandle(int sdk,
	                      Address address,
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

	/**
	 * With debug tree function handle.
	 *
	 * @param enabled             the enabled
	 * @param timeout             the timeout
	 * @param throwErrors         the throw errors
	 * @param transactionMaxCount the transaction max count
	 * @param treeAbis            the tree abis
	 * @return the function handle
	 */
	public FunctionHandle<RETURN> withDebugTree(boolean enabled,
	                                            long timeout,
	                                            boolean throwErrors,
	                                            long transactionMaxCount,
	                                            ContractAbi... treeAbis) {
		return new FunctionHandle<>(clazz(),
		                            contract(),
		                            functionName(),
		                            functionInputs(),
		                            functionHeader(),
		                            new DebugOptions(enabled, timeout, throwErrors, transactionMaxCount, treeAbis),
		                            deploySet());
	}

	/**
	 * With debug tree function handle.
	 *
	 * @param debugOptions the debug options
	 * @return the function handle
	 */
	public FunctionHandle<RETURN> withDebugTree(DebugOptions debugOptions) {
		return new FunctionHandle<>(clazz(),
		                            contract(),
		                            functionName(),
		                            functionInputs(),
		                            functionHeader(),
		                            debugOptions,
		                            deploySet());
	}

	/**
	 * With credentials function handle.
	 *
	 * @param credentials the credentials
	 * @return the function handle
	 */
	public FunctionHandle<RETURN> withCredentials(Credentials credentials) {
		return new FunctionHandle<>(clazz(),
		                            new AbstractContract(contract().contextId(),
		                                                 contract().address(),
		                                                 contract().abi(),
		                                                 credentials),
		                            functionName(),
		                            functionInputs(),
		                            functionHeader());
	}

	/**
	 * With function inputs function handle.
	 *
	 * @param functionInputs the function inputs
	 * @return the function handle
	 */
	public FunctionHandle<RETURN> withFunctionInputs(Map<String, Object> functionInputs) {
		return new FunctionHandle<>(clazz(), contract(), functionName(), functionInputs, functionHeader());
	}

	/**
	 * With function header function handle.
	 *
	 * @param functionHeader the function header
	 * @return the function handle
	 */
	public FunctionHandle<RETURN> withFunctionHeader(Abi.FunctionHeader functionHeader) {
		return new FunctionHandle<>(clazz(), contract(), functionName(), functionInputs(), functionHeader);
	}

	/**
	 * With return class function handle.
	 *
	 * @param <T>         the type parameter
	 * @param returnClass the return class
	 * @return the function handle
	 */
	public <T> FunctionHandle<T> withReturnClass(Class<T> returnClass) {
		return new FunctionHandle<>(returnClass, contract(), functionName(), functionInputs(), functionHeader());
	}

	/**
	 * To call set abi . call set.
	 *
	 * @return the abi . call set
	 * @throws EverSdkException the ever sdk exception
	 */
	public Abi.CallSet toCallSet() throws EverSdkException {
		Map<String, Object> converted = contract().abi().convertFunctionInputs(functionName(), functionInputs());
		return new Abi.CallSet(functionName(), functionHeader(), JsonContext.ABI_JSON_MAPPER().valueToTree(converted));
	}

	/**
	 * To output return.
	 *
	 * @param outputMap the output map
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
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
	 * @throws EverSdkException the ever sdk exception
	 */
	public TvmCell toPayload() throws EverSdkException {
		return new TvmCell(EverSdk.await(Abi.encodeMessageBody(contract().contextId(),
		                                                       contract().abi().functionCallABI(functionName()),
		                                                       toCallSet(),
		                                                       true,
		                                                       toSigner(),
		                                                       null,
		                                                       contract().address().makeAddrStd(),
		                                                       null)).body());
	}

	/**
	 * To signer abi . signer.
	 *
	 * @return the abi . signer
	 */
	public Abi.Signer toSigner() {
		return Objs.notNullElse(contract().credentials(), Credentials.NONE).signer();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @return the as map
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode getAsMap() throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new Account.GraphQLFilter.In(new String[]{contract().address().makeAddrStd()}));
		Net.ResultOfQueryCollection result = EverSdk.await(Net.queryCollection(contract().contextId(),
		                                                                       "accounts",
		                                                                       JsonContext.ABI_JSON_MAPPER()
		                                                                                  .valueToTree(filter),
		                                                                       "id boc",
		                                                                       null,
		                                                                       null));
		Abi.ResultOfEncodeMessage msg = EverSdk.await(Abi.encodeMessage(contract().contextId(),
		                                                                contract().abi()
		                                                                          .functionCallABI(functionName()),
		                                                                contract().address().makeAddrStd(),
		                                                                null,
		                                                                toCallSet(),
		                                                                toSigner(),
		                                                                null,
		                                                                null));
		for (var map : result.result()) {
			String boc = map.get("boc").asText();
			return Optional.ofNullable(EverSdk.await(Tvm.runTvm(contract().contextId(),
			                                                    msg.message(),
			                                                    boc,
			                                                    null,
			                                                    contract().abi().ABI(),
			                                                    null,
			                                                    false)).decoded().output())
			               .orElse(JsonContext.EMPTY_NODE());
		}
		return JsonContext.EMPTY_NODE();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method always downloads new boc before running getter on it.
	 * If you need to cache boc and run multiple getters cheaply, you need to get
	 * Account object via OwnedContract.account() method and then run Account.runGetter() method.
	 *
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN get() throws EverSdkException {
		return toOutput(getAsMap());
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! This method uses provided boc!
	 * There's no guarantee that it corresponds to current blockchain state.
	 *
	 * @param boc the boc
	 * @return the local
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN getLocal(String boc) throws EverSdkException {
		return toOutput(getLocalAsMap(boc));
	}

	/**
	 * Encodes inputs and runs getter method on account's boc then decodes answer.
	 * Important! When you run getter locally, directly on Account boc,
	 * there's no guarantee that it corresponds to current blockchain state.
	 *
	 * @param boc the boc
	 * @return the local as map
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode getLocalAsMap(String boc) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg = EverSdk.await(Abi.encodeMessage(contract().contextId(),
		                                                                contract().abi()
		                                                                          .functionCallABI(functionName()),
		                                                                contract().address().makeAddrStd(),
		                                                                null,
		                                                                toCallSet(),
		                                                                toSigner(),
		                                                                null,
		                                                                null));
		return Optional.ofNullable(EverSdk.await(Tvm.runTvm(contract().contextId(),
		                                                    msg.message(),
		                                                    boc,
		                                                    null,
		                                                    contract().abi().ABI(),
		                                                    null,
		                                                    false)).decoded().output())
		               .orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Encodes inputs and runs external call method on account's boc then decodes answer.
	 * Important! When you run external call locally, directly on Account boc,
	 * your blockchain real info remains unchanged.
	 *
	 * @param boc              the boc
	 * @param options          the options
	 * @param unlimitedBalance the unlimited balance
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode callLocalAsMap(String boc,
	                               Tvm.ExecutionOptions options,
	                               boolean unlimitedBalance) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg = EverSdk.await(Abi.encodeMessage(contract().contextId(),
		                                                                contract().abi()
		                                                                          .functionCallABI(functionName()),
		                                                                contract().address().makeAddrStd(),
		                                                                null,
		                                                                toCallSet(),
		                                                                toSigner(),
		                                                                null,
		                                                                null));
		return Optional.ofNullable(EverSdk.await(Tvm.runExecutor(contract().contextId(),
		                                                         msg.message(),
		                                                         new Tvm.AccountForExecutor.Account(boc,
		                                                                                            unlimitedBalance),
		                                                         options,
		                                                         contract().abi().ABI(),
		                                                         false,
		                                                         null,
		                                                         true)).decoded().output())
		               .orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Calls smart contract with external message using credentials provided
	 * on initialization.
	 *
	 * @param boc              the boc
	 * @param options          the options
	 * @param unlimitedBalance the unlimited balance
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN callLocal(String boc,
	                        Tvm.ExecutionOptions options,
	                        boolean unlimitedBalance) throws EverSdkException {
		return toOutput(callLocalAsMap(boc, options, unlimitedBalance));
	}

	/**
	 * Call as map json node.
	 *
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
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
		                                                            new BigDecimal(resultOfProcess.fees()
		                                                                                          .totalFwdFees(),
		                                                                           9).toPlainString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("account_addr")
		                                                                           .asText(),
		                                                            0,
		                                                            this.functionName,
		                                                            new BigDecimal(resultOfProcess.fees()
		                                                                                          .totalAccountFees(),
		                                                                           9).toPlainString(),
		                                                            "");
		info(logger, lazyFormatLogMessage);
		return Optional.ofNullable(resultOfProcess.decoded().output()).orElse(JsonContext.EMPTY_NODE());
	}

	/**
	 * Calls smart contract with external message using credentials provided
	 * on initialization.
	 *
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
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
	 * @return result of tree
	 * @throws EverSdkException the ever sdk exception
	 */
	public ResultOfTree<JsonNode> callTreeAsMap(boolean throwOnTreeError,
	                                            ContractAbi... otherAbisForDecode) throws EverSdkException {
		Abi.ABI[] finalABIArray = Arrays.stream(concatAbiSet(otherAbisForDecode, contract().abi()))
		                                .map(ContractAbi::ABI)
		                                .toArray(Abi.ABI[]::new);
		var resultOfProcess = processExternalCall();
		var msgId = resultOfProcess.transaction().get("in_msg").asText();
		var debugOutResult = EverSdk.await(Net.queryTransactionTree(contract().contextId(),
		                                                            msgId,
		                                                            finalABIArray,
		                                                            debugOptions().timeout(),
		                                                            debugOptions().transactionMaxCount()));
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
	 * @return result of tree
	 * @throws EverSdkException the ever sdk exception
	 */
	public ResultOfTree<RETURN> callTree(boolean throwOnTreeError,
	                                     ContractAbi... otherAbisForDecode) throws EverSdkException {
		var result = callTreeAsMap(throwOnTreeError, otherAbisForDecode);
		return new ResultOfTree<>(result.queryTree(), toOutput(result.decodedOutput()));
	}

	/**
	 * Send from as map json node.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @param bounce the bounce
	 * @param flag   the flag
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode sendFromAsMap(MultisigContract2 sender,
	                              BigInteger value,
	                              boolean bounce,
	                              MessageFlag flag) throws EverSdkException {
		return sender.sendTransaction(contract().address(), value, bounce, flag.flag(), toPayload()).callAsMap();
	}

	/**
	 * Send from as map json node.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @param bounce the bounce
	 * @param flag   the flag
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode sendFromAsMap(MultisigContract sender,
	                              BigInteger value,
	                              boolean bounce,
	                              MessageFlag flag) throws EverSdkException {
		return sender.sendTransaction(contract().address(), value, bounce, flag.flag(), toPayload()).callAsMap();
	}

	/**
	 * Send from as map json node.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode sendFromAsMap(MultisigContract sender, BigInteger value) throws EverSdkException {
		return sendFromAsMap(sender, value, true, MessageFlag.EXACT_VALUE_GAS);
	}

	/**
	 * Send from return.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @param bounce the bounce
	 * @param flag   the flag
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN sendFrom(MultisigContract sender,
	                       BigInteger value,
	                       boolean bounce,
	                       MessageFlag flag) throws EverSdkException {
		return toOutput(sendFromAsMap(sender, value, bounce, flag));
	}

	/**
	 * Send from return.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @param bounce the bounce
	 * @param flag   the flag
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN sendFrom(MultisigContract2 sender,
	                       BigInteger value,
	                       boolean bounce,
	                       MessageFlag flag) throws EverSdkException {
		return toOutput(sendFromAsMap(sender, value, bounce, flag));
	}

	/**
	 * Send from return.
	 *
	 * @param sender the sender
	 * @param value  the value
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN sendFrom(MultisigContract sender, BigInteger value) throws EverSdkException {
		return toOutput(sendFromAsMap(sender, value));
	}

	/**
	 * Send from tree as map result of tree.
	 *
	 * @param sender             the sender
	 * @param value              the value
	 * @param bounce             the bounce
	 * @param flag               the flag
	 * @param throwOnTreeError   the throw on tree error
	 * @param otherAbisForDecode the other abis for decode
	 * @return the result of tree
	 * @throws EverSdkException the ever sdk exception
	 */
	public ResultOfTree<JsonNode> sendFromTreeAsMap(MultisigContract sender,
	                                                BigInteger value,
	                                                boolean bounce,
	                                                MessageFlag flag,
	                                                boolean throwOnTreeError,
	                                                ContractAbi... otherAbisForDecode) throws EverSdkException {
		try {
			return sender.sendTransaction(contract().address(), value, bounce, flag.flag(), toPayload())
			             .callTreeAsMap(throwOnTreeError,
			                            concatAbiSet(otherAbisForDecode,
			                                         contract().abi(),
			                                         SafeMultisigWalletTemplate.DEFAULT_ABI()));
		} catch (JsonProcessingException e) {
			throw new EverSdkException(new EverSdkException.ErrorResult(-500, e.getMessage()));
		}
	}

	/**
	 * Send from tree result of tree.
	 *
	 * @param sender             the sender
	 * @param value              the value
	 * @param bounce             the bounce
	 * @param flag               the flag
	 * @param throwOnTreeError   the throw on tree error
	 * @param otherAbisForDecode the other abis for decode
	 * @return the result of tree
	 * @throws EverSdkException the ever sdk exception
	 */
	public ResultOfTree<RETURN> sendFromTree(MultisigContract sender,
	                                         BigInteger value,
	                                         boolean bounce,
	                                         MessageFlag flag,
	                                         boolean throwOnTreeError,
	                                         ContractAbi... otherAbisForDecode) throws EverSdkException {
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
		return EverSdk.await(Processing.processMessage(contract().contextId(),
		                                               contract().abi().functionCallABI(functionName()),
		                                               contract().address().makeAddrStd(),
		                                               null,
		                                               toCallSet(),
		                                               toSigner(),
		                                               null,
		                                               null,
		                                               false));
	}

	/**
	 * To builder builder.
	 *
	 * @return the builder
	 */
	public Builder toBuilder() {
		var builder = new Builder(clazz());
		builder.clazz = clazz();
		builder.contract = contract();
		builder.functionName = functionName();
		builder.functionInputs = functionInputs();
		builder.functionHeader = functionHeader();
		builder.debugOptions = debugOptions();
		return builder;
	}

	/**
	 * The type Builder.
	 */
	public static class Builder {

		/**
		 * The Clazz.
		 */
		public Class clazz;
		/**
		 * The Contract.
		 */
		public Contract contract;
		/**
		 * The Function name.
		 */
		public String functionName;
		/**
		 * The Function inputs.
		 */
		public Map<String, Object> functionInputs;
		/**
		 * The Function header.
		 */
		public Abi.FunctionHeader functionHeader;
		/**
		 * The Debug options.
		 */
		public DebugOptions debugOptions;

		public Abi.DeploySet deploySet;

		/**
		 * Instantiates a new Builder.
		 *
		 * @param clazz the clazz
		 */
		public Builder(Class clazz) {
			this.clazz = clazz;
		}

		/**
		 * Sets return class.
		 *
		 * @param clazz the clazz
		 * @return the return class
		 */
		public Builder setReturnClass(Class clazz) {
			this.clazz = clazz;
			return this;
		}

		/**
		 * Sets contract.
		 *
		 * @param contract the contract
		 * @return the contract
		 */
		public Builder setContract(Contract contract) {
			this.contract = contract;
			return this;
		}

		/**
		 * Sets function name.
		 *
		 * @param functionName the function name
		 * @return the function name
		 */
		public Builder setFunctionName(String functionName) {
			this.functionName = functionName;
			return this;
		}

		/**
		 * Sets function inputs.
		 *
		 * @param functionInputs the function inputs
		 * @return the function inputs
		 */
		public Builder setFunctionInputs(Map<String, Object> functionInputs) {
			this.functionInputs = functionInputs;
			return this;
		}

		/**
		 * Sets function header.
		 *
		 * @param functionHeader the function header
		 * @return the function header
		 */
		public Builder setFunctionHeader(Abi.FunctionHeader functionHeader) {
			this.functionHeader = functionHeader;
			return this;
		}

		/**
		 * Sets debug options.
		 *
		 * @param debugOptions the debug options
		 * @return the debug options
		 */
		public Builder setDebugOptions(DebugOptions debugOptions) {
			this.debugOptions = debugOptions;
			return this;
		}

		/**
		 * Sets deploy set to send with a call
		 *
		 * @param deploySet the deploy set to send with a call
		 * @return the function inputs
		 */
		public Builder setDeploySet(Abi.DeploySet deploySet) {
			this.deploySet = deploySet;
			return this;
		}

		/**
		 * Build function handle.
		 *
		 * @return the function handle
		 */
		public FunctionHandle build() {
			return new FunctionHandle(this.clazz,
			                          this.contract,
			                          this.functionName,
			                          this.functionInputs,
			                          this.functionHeader,
			                          this.debugOptions,
			                          this.deploySet);
		}

	}

}
