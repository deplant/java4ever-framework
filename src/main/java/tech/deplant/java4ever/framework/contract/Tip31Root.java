package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;

public class Tip31Root extends OwnedContract {

	public Tip31Root(Sdk sdk, Address address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public Tip31Root(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.tvmKey());
	}

}