package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;

import java.math.BigInteger;
import java.util.Map;

public abstract class GiverContract extends AbstractContract {

	System.Logger logger = System.getLogger(GiverContract.class.getName());

	@JsonCreator
	public GiverContract(Sdk sdk, String address, ContractAbi abi, Credentials credentials) {
		super(sdk,address,abi,credentials);
	}

	public abstract FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce);

	public FunctionHandle<Void> give(String to, BigInteger value) {
		logger.log(System.Logger.Level.INFO, "Giver called!");
		return sendTransaction(new Address(to), value, false);
	}

}
