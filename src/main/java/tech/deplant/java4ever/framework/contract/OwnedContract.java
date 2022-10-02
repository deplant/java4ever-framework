package tech.deplant.java4ever.framework.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractAbi;
import tech.deplant.java4ever.framework.template.type.AbiUint;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OwnedContract {

	private static Logger log = LoggerFactory.getLogger(OwnedContract.class);

	protected final Sdk sdk;

	protected final Address address;

	protected final ContractAbi abi;

	protected final Credentials tvmKey;

	public OwnedContract(Sdk sdk, Address address, ContractAbi abi, Credentials tvmKey) {
		this.sdk = sdk;
		this.address = address;
		this.abi = abi;
		this.tvmKey = tvmKey;
	}

	public Sdk sdk() {
		return this.sdk;
	}

	public Address address() {
		return this.address;
	}

	public ContractAbi abi() {
		return this.abi;
	}

	public BigInteger balance() throws EverSdkException {
		return AbiUint.deserialize(128, account().balance());
	}

	public Account account() throws EverSdkException {
		return Account.graphQLRequest(this.sdk, this.address);
	}

	public Credentials tvmKey() {
		return this.tvmKey;
	}

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
				address().makeAddrStd()
		).body();
	}

	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader,
	                                     Credentials credentials) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						sdk().context(),
						abi().ABI(),
						address().makeAddrStd(),
						null,
						new Abi.CallSet(
								functionName,
								null,
								abi().convertFunctionInputs(functionName, functionInputs)
						),
						credentials.signer(),
						null
				);

		return Optional.ofNullable(Tvm.runTvm(
				                              sdk().context(),
				                              msg.message(),
				                              account().boc(),
				                              null,
				                              this.abi.ABI(),
				                              null,
				                              false)
		                              .decoded()
		                              .output()).orElse(new HashMap<>());
	}

	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader,
	                                        Credentials credentials) throws EverSdkException {
		return Optional.ofNullable(Processing.processMessage(this.sdk.context(),
		                                                     abi().ABI(),
		                                                     address().makeAddrStd(),
		                                                     null,
		                                                     new Abi.CallSet(functionName,
		                                                                     functionHeader,
		                                                                     abi().convertFunctionInputs(functionName,
		                                                                                                 functionInputs)),
		                                                     credentials.signer(), null, false, null)
		                                     .decoded()
		                                     .output()).orElse(new HashMap<>());
	}

	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader) throws EverSdkException {
		return callExternal(functionName, functionInputs, functionHeader, this.tvmKey);
	}

	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader) throws EverSdkException {
		return runGetter(functionName, functionInputs, functionHeader, this.tvmKey);
	}
}
