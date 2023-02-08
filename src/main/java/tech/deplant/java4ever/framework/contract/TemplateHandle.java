package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractTemplate;
import tech.deplant.java4ever.framework.template.ContractTvc;

import java.util.Map;

public interface TemplateHandle {

	System.Logger logger = System.getLogger(ContractTemplate.class.getName());

	ContractAbi abi();

	ContractTvc tvc();

	default String calculateAddress(Sdk sdk,
	                                Map<String, Object> initialData,
	                                Credentials credentials) throws EverSdkException {
		String address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
		logger.log(System.Logger.Level.INFO, () -> "Future address: " + address);
		return address;
	}
}
