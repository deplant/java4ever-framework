package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractAbi;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Msig extends OwnedContract implements Giver {

	public Msig(Sdk sdk, Address address, Credentials owner, ContractAbi abi) {
		super(sdk, address, abi, owner);
	}

	public Msig(OwnedContract contract) {
		super(contract.sdk(), contract.address(), contract.abi(), contract.tvmKey());
	}

	public static Msig ofSafe(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
		return new Msig(sdk, address, owner, MsigTemplate.SAFE_MULTISIG_ABI());
	}

	public static Msig ofSetcode(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
		return new Msig(sdk, address, owner, MsigTemplate.SETCODE_MULTISIG_ABI());
	}

	public static Msig ofSurf(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
		return new Msig(sdk, address, owner, MsigTemplate.SURF_MULTISIG_ABI());
	}

	public void send(Address to,
	                 BigInteger amount,
	                 boolean sendBounce,
	                 int flags,
	                 String payload) throws EverSdkException {
		Map<String, Object> params = Map.of(
				"dest", to.makeAddrStd(),
				"value", amount,
				"bounce", sendBounce,
				"flags", flags,
				"payload", payload);
		super.callExternal("sendTransaction", params, null);
	}

	public Net.ResultOfQueryTransactionTree sendDebugTree(Address to,
	                                                      BigInteger amount,
	                                                      boolean sendBounce,
	                                                      int flags,
	                                                      String payload,
	                                                      Long debugQueryTimeout,
	                                                      boolean debugThrowOnInternalError,
	                                                      Net.ResultOfQueryTransactionTree debugOutResult,
	                                                      List<ContractAbi> debugAbisForDecode) throws EverSdkException {
		Map<String, Object> params = Map.of(
				"dest", to.makeAddrStd(),
				"value", amount,
				"bounce", sendBounce,
				"flags", flags,
				"payload", payload);
		super.callExternalDebugTree("sendTransaction",
		                            params,
		                            null,
		                            tvmKey(),
		                            debugQueryTimeout,
		                            debugThrowOnInternalError,
		                            debugOutResult,
		                            debugAbisForDecode);
		return debugOutResult;
	}

	@Override
	public void give(Address to, BigInteger amount) throws EverSdkException {
		send(to, amount, false, 1, "");
	}
}