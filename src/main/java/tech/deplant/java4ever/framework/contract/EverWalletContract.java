package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.EverWalletTemplate;

import java.math.BigInteger;

public class EverWalletContract extends GiverContract {

	/**
	 * Instantiates a new EVER WAllet contract.
	 *
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param credentials the credentials
	 */
	public EverWalletContract(int sdk, String address, Credentials credentials) throws JsonProcessingException {
		super(sdk, address, EverWalletTemplate.DEFAULT_ABI(), credentials);
	}

	@Override
	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		return null;
	}
}
