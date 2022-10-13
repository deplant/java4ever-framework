package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

/**
 * Implementation of GiverV2 contract that is included in EverNodeSE image.
 */
public class EverOSGiver extends OwnedContract implements Giver {

	public static Credentials KEYS = new Credentials(
			"2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16",
			"172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3");
	public static Address ADDRESS = new Address("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");

	public EverOSGiver(Sdk sdk) throws JsonProcessingException {
		super(sdk, ADDRESS, ABI(), KEYS);
	}

	public EverOSGiver(Sdk sdk, Address address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public EverOSGiver(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.credentials());
	}

	public static ContractAbi ABI() throws JsonProcessingException {
		return ContractAbi.ofResource("artifacts/giver/GiverV2.abi.json");
	}

	@Override
	public void give(Address to, BigInteger amount) throws EverSdkException {
		Map<String, Object> params = Map.of(
				"dest", to,
				"value", amount,
				"bounce", false
		);
		callExternal("sendTransaction", params, null);
	}
}
