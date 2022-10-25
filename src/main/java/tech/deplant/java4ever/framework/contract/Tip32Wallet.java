package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.AbiAddress;
import tech.deplant.java4ever.framework.abi.AbiUint;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.HashMap;

public class Tip32Wallet extends OwnedContract {

	public Tip32Wallet(Sdk sdk, Address address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public Tip32Wallet(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.credentials());
	}

	public Address owner() throws EverSdkException {
		return AbiAddress.deserialize(runGetter("owner", new HashMap<>(), null).get("value0"));
	}

	public BigInteger tokenBalance() throws EverSdkException {
		return AbiUint.deserialize(128, runGetter("balance", new HashMap<>(), null).get("value0"));
	}

}
