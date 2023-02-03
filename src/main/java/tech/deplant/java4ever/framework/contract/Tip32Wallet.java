package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Uint;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.HashMap;

public class Tip32Wallet extends OwnedContract {

	public Tip32Wallet(Sdk sdk, String address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public Tip32Wallet(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.credentials());
	}

	public String owner() throws EverSdkException {
		return runGetter("owner", new HashMap<>(), null).get("value0").toString();
	}

	public BigInteger tokenBalance() throws EverSdkException {
		return Uint.fromJava(128, runGetter("balance", new HashMap<>(), null).get("value0")).toJava();
	}

}
