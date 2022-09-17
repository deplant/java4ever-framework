package tech.deplant.java4ever.framework.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.abi.IAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.util.Map;

public class OwnedContract {

	private static Logger log = LoggerFactory.getLogger(OwnedContract.class);

	protected final Sdk sdk;

	protected final Address address;

	protected final IAbi abi;

	protected final Credentials tvmKey;

	public OwnedContract(Sdk sdk, Address address, IAbi abi, Credentials tvmKey) {
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

	public IAbi abi() {
		return this.abi;
	}

	public Account account() {
		return Account.graphQLRequest(this.sdk, this.address);
	}

	public Credentials tvmKey() {
		return this.tvmKey;
	}

	public String encodeInternalPayload(String functionName,
	                                    Map<String, Object> functionInputs,
	                                    Abi.FunctionHeader functionHeader) {
		return Abi.encodeMessageBody(
				sdk().context(),
				abi().ABI(),
				new Abi.CallSet(functionName, functionHeader, abi().convertInputs(functionName, functionInputs)),
				true,
				Credentials.NONE.signer(),
				null,
				address().makeAddrStd()
		).body();
	}

	protected Map<String, Object> processMessage(IAbi abi,
	                                             Address address,
	                                             Abi.DeploySet deploySet,
	                                             Credentials credentials,
	                                             String functionName,
	                                             Abi.FunctionHeader functionHeader,
	                                             Map<String, Object> functionInputs) {
		return Processing.processMessage(sdk().context(),
		                                 abi.ABI(),
		                                 address.makeAddrStd(),
		                                 deploySet,
		                                 new Abi.CallSet(functionName,
		                                                 functionHeader,
		                                                 abi().convertInputs(functionName, functionInputs)),
		                                 credentials.signer(), null, false, null)
		                 .decoded()
		                 .output();
	}

	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader,
	                                     Credentials credentials) {
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						sdk().context(),
						abi().ABI(),
						address().makeAddrStd(),
						null,
						new Abi.CallSet(
								functionName,
								null,
								abi().convertInputs(functionName, functionInputs)
						),
						credentials.signer(),
						null
				);

		return Tvm.runTvm(
				          sdk().context(),
				          msg.message(),
				          account().boc(),
				          null,
				          this.abi.ABI(),
				          null,
				          false)
		          .decoded()
		          .output();
	}

	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader,
	                                        Credentials credentials) {
		return Processing.processMessage(this.sdk.context(),
		                                 abi().ABI(),
		                                 address().makeAddrStd(),
		                                 null,
		                                 new Abi.CallSet(functionName,
		                                                 functionHeader,
		                                                 abi().convertInputs(functionName, functionInputs)),
		                                 credentials.signer(), null, false, null)
		                 .decoded()
		                 .output();
	}

	public Map<String, Object> callExternal(String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader) {
		return callExternal(functionName, functionInputs, functionHeader, this.tvmKey);
	}

	public Map<String, Object> runGetter(String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader) {
		return runGetter(functionName, functionInputs, functionHeader, this.tvmKey);
	}
}
