package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;

/**
 * Class that represents deployed contract in one of the networks. It holds info about
 * network (sdk), address and abi of contract. If you own this contract, initialize it
 * with correct credentials.
 * If it's foreign contract, use shorter constructor oe explicit Credentials.NONE.
 * You can make calls to contract with prepareCall() method.
 */
public record CustomContract(Sdk sdk, String address, ContractAbi abi, Credentials credentials) implements Contract {
	public CustomContract(Sdk sdk, String address, ContractAbi abi) {
		this(sdk, address, abi, Credentials.NONE);
	}
}
