package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractAbi;
import tech.deplant.java4ever.framework.template.type.AbiAddress;
import tech.deplant.java4ever.framework.template.type.AbiUint;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.HashMap;

public class Tip31Wallet extends OwnedContract {

	public Tip31Wallet(Sdk sdk, Address address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public Tip31Wallet(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.tvmKey());
	}

	public Address owner() {
		return AbiAddress.deserialize(runGetter("owner", new HashMap<>(), null).get("value0"));
	}

	public BigInteger tokenBalance() {
		return AbiUint.deserialize(128, runGetter("balance", new HashMap<>(), null).get("value0"));
	}

}
