package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class ContractTemplate {

	private static System.Logger logger = System.getLogger(ContractTemplate.class.getName());
	private final ContractAbi abi;
	private final ContractTvc tvc;

	public ContractTemplate(ContractAbi abi, ContractTvc tvc) {
		this.abi = abi;
		this.tvc = tvc;
	}


	public ContractAbi abi() {
		return this.abi;
	}

	//TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

	public ContractTvc tvc() {
		return this.tvc;
	}

	protected OwnedContract doDeploy(Sdk sdk,
	                                 int workchainId,
	                                 Address address,
	                                 Map<String, Object> initialData,
	                                 Credentials
			                                 credentials,
	                                 Map<String, Object> constructorInputs) throws EverSdkException {
		Processing.processMessage(
				sdk.context(),
				abi().ABI(),
				null,
				new Abi.DeploySet(
						this.tvc.base64String(),
						workchainId,
						abi().convertInitDataInputs(initialData),
						credentials.publicKey()),
				new Abi.CallSet(
						"constructor",
						null,
						abi().convertFunctionInputs("constructor", constructorInputs)),
				credentials.signer(),
				null,
				false,
				null
		);
		return new OwnedContract(sdk, address, this.abi, credentials);
	}

	public OwnedContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
			credentials, Map<String, Object> constructorInputs) throws EverSdkException {
		var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
		logger.log(System.Logger.Level.INFO, () -> "Future address: " + address.makeAddrStd());
		return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
	}

	public OwnedContract deployWithGiver(Sdk sdk,
	                                     Giver giver,
	                                     BigInteger value,
	                                     int workchainId,
	                                     Map<
			                                     String, Object> initialData,
	                                     Credentials credentials,
	                                     Map<String, Object> constructorInputs) throws EverSdkException {
		var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
		logger.log(System.Logger.Level.INFO, () -> "Future address: " + address.makeAddrStd());
		giver.give(address, value);
		return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
	}

	public Map<String, Object> decodeInitialData(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialData(sdk, abi());
	}

	public String decodeInitialPubkey(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialPubkey(sdk, abi());
	}

	public Address calculateAddress(Sdk sdk) throws EverSdkException {
		return new Address("0:" + Boc.getBocHash(sdk.context(), tvc().base64String()).hash());
	}

	public boolean isDeployed(Sdk sdk) throws EverSdkException {
		return Account.ofAddress(sdk, calculateAddress(sdk)).isActive();
	}

	public ContractTemplate withUpdatedInitialData(Sdk sdk,
	                                               Map<String, Object> initialData,
	                                               String publicKey) throws EverSdkException {
		return new ContractTemplate(abi(),
		                            tvc().withUpdatedInitialData(sdk, abi(), initialData, publicKey));
	}

}
