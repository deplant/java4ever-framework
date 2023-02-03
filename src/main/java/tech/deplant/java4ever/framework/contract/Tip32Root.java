package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.Tip31RootTemplate;

public class Tip32Root extends OwnedContract {

	public final static String CODE_HASH = "e8801282233f38ad17fead83aeac1820e40598de98675106b0fd91d9524e9141";

	public Tip32Root(Sdk sdk, String address, ContractAbi abi, Credentials owner) {
		super(sdk, address, abi, owner);
	}

	public Tip32Root(Sdk sdk, String address,
	                 Credentials owner) throws JsonProcessingException {
		this(sdk, address, Tip31RootTemplate.DEFAULT_ABI(), owner);
	}

	public Tip32Root(Sdk sdk, String address) throws JsonProcessingException {
		this(sdk, address, Credentials.NONE);
	}

	public Tip32Root(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.credentials());
	}

	public static Tip32Root QUBE_ROOT(Sdk sdk, Credentials credentials) throws JsonProcessingException {
		return new Tip32Root(sdk, "0:9f20666ce123602fd7a995508aeaa0ece4f92133503c0dfbd609b3239f3901e2",
		                     Tip31RootTemplate.DEFAULT_ABI(), credentials);
	}

	public static Tip32Root BRIDGE_ROOT(Sdk sdk, Credentials credentials) throws JsonProcessingException {
		return new Tip32Root(sdk, "0:f2679d80b682974e065e03bf42bbee285ce7c587eb153b41d761ebfd954c45e1",
		                     Tip31RootTemplate.DEFAULT_ABI(), credentials);
	}

	public static Tip32Root LEND_ROOT(Sdk sdk, Credentials credentials) throws JsonProcessingException {
		return new Tip32Root(sdk, "0:679a05316a324d0daa2724ab7d8e9768a2d1042863299323e969a174a8412a58",
		                     Tip31RootTemplate.DEFAULT_ABI(), credentials);
	}

	public static Tip32Root WETH_ROOT(Sdk sdk, Credentials credentials) throws JsonProcessingException {
		return new Tip32Root(sdk, "0:59b6b64ac6798aacf385ae9910008a525a84fc6dcf9f942ae81f8e8485fe160d",
		                     Tip31RootTemplate.DEFAULT_ABI(), credentials);
	}

}