package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.datatype.Address;

import java.util.Map;

public interface Template {

	System.Logger logger = System.getLogger(Template.class.getName());

	ContractAbi abi();

	Tvc tvc();

//	default String calculateAddress(Sdk sdk,
//	                                Map<String, Object> initialData,
//	                                Credentials credentials) throws EverSdkException {
//		String address = Address.ofFutureDeploy(sdk, new AbstractTemplate(abi(), tvc()), 0, initialData, credentials);
//		logger.log(System.Logger.Level.INFO, () -> "Future address: " + address);
//		return address;
//	}


	default JsonNode decodeInitialData(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialData(sdk, abi());
	}

	default String decodeInitialPubkey(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialPubkey(sdk, abi());
	}

	default String addressFromEncodedTvc(Sdk sdk) throws EverSdkException {
		return String.format("0:%s", Boc.getBocHash(sdk.context(), tvc().base64String()).hash());
	}

	default boolean isDeployed(Sdk sdk) throws EverSdkException {
		return Account.ofAddress(sdk, addressFromEncodedTvc(sdk)).isActive();
	}

}
