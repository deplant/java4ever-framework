package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.util.Map;

public interface ContractHandle {
	Sdk sdk();

	String address();

	ContractAbi abi();

	Credentials credentials();

	default CallHandle customCall(String functionName,
	                              Map<String, Object> functionInputs,
	                              Abi.FunctionHeader functionHeader) {
		return new CallHandle<Map<String, Object>>(this,
		                                           functionName,
		                                           functionInputs,
		                                           functionHeader);
	}
}
