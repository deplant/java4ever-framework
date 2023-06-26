package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.AbstractContract;

import java.util.Map;

public record AbstractTemplate(ContractAbi abi, Tvc tvc) implements Template {
	DeployHandle<AbstractContract> prepareDeploy(Sdk sdk,
	                                             Credentials credentials,
	                                             Map<String, Object> initialDataFields,
	                                             Map<String, Object> constructorInputs,
	                                             Abi.FunctionHeader constructorHeader) {
		return new DeployHandle<AbstractContract>(AbstractContract.class,
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
