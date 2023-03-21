package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.CustomContract;

import java.util.Map;

public record CustomTemplate(ContractAbi abi, Tvc tvc) implements Template {
	DeployHandle<CustomContract> prepareDeploy(Sdk sdk,
	                                           Credentials credentials,
	                                           Map<String, Object> initialDataFields,
	                                           Map<String, Object> constructorInputs,
	                                           Abi.FunctionHeader constructorHeader) {
		return new DeployHandle<CustomContract>(CustomContract.class,
		                                        sdk,
		                                        abi(),
		                                        tvc(),
		                                        sdk.clientConfig().abi().workchain(),
		                                        credentials,
		                                        initialDataFields,
		                                        constructorInputs,
		                                        constructorHeader);
	}
}
