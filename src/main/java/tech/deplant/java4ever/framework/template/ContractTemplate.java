package tech.deplant.java4ever.framework.template;

import jdk.incubator.concurrent.StructuredTaskScope;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.Future;

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

	public ContractTvc tvc() {
		return this.tvc;
	}

	protected OwnedContract doDeploy(Sdk sdk,
	                                 int workchainId,
	                                 final String address,
	                                 final Map<String, Object> initialData,
	                                 final Credentials
			                                 credentials,
	                                 final Map<String, Object> constructorInputs) throws EverSdkException {

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			final Future<Map<String, Object>> initDataConverted = scope.fork(() -> abi().convertInitDataInputs(
					initialData));
			final Future<Map<String, Object>> constructorParamsConverted = scope.fork(() -> abi().convertFunctionInputs(
					"constructor",
					constructorInputs));
			scope.join();
			Processing.processMessage(
					sdk.context(),
					abi().ABI(),
					address,
					new Abi.DeploySet(
							this.tvc.base64String(),
							workchainId,
							initDataConverted.resultNow(),
							credentials.publicKey()),
					new Abi.CallSet(
							"constructor",
							null,
							constructorParamsConverted.resultNow()),
					credentials.signer(),
					null,
					false,
					null
			);
			return new OwnedContract(sdk, address, this.abi, credentials);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	public OwnedContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
			credentials, Map<String, Object> constructorInputs) throws EverSdkException {
		return doDeploy(sdk,
		                workchainId,
		                calculateAddress(sdk, initialData, credentials),
		                initialData,
		                credentials,
		                constructorInputs);
	}

	public String calculateAddress(Sdk sdk,
	                               Map<String, Object> initialData,
	                               Credentials credentials) throws EverSdkException {
		String address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
		logger.log(System.Logger.Level.INFO, () -> "Future address: " + address);
		return address;
	}

	public OwnedContract deployWithGiver(Sdk sdk,
	                                     Giver giver,
	                                     BigInteger value,
	                                     int workchainId,
	                                     Map<String, Object> initialData,
	                                     Credentials credentials,
	                                     Map<String, Object> constructorInputs) throws EverSdkException {
		var address = calculateAddress(sdk, initialData, credentials);
		giver.give(address, value);
		return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
	}

	public Map<String, Object> decodeInitialData(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialData(sdk, abi());
	}

	public String decodeInitialPubkey(Sdk sdk) throws EverSdkException {
		return tvc().decodeInitialPubkey(sdk, abi());
	}

	public String addressFromEncodedTvc(Sdk sdk) throws EverSdkException {
		return String.format("0:%s", Boc.getBocHash(sdk.context(), tvc().base64String()).hash());
	}

	public boolean isDeployed(Sdk sdk) throws EverSdkException {
		return Account.ofAddress(sdk, addressFromEncodedTvc(sdk)).isActive();
	}

	public ContractTemplate withUpdatedInitialData(Sdk sdk,
	                                               Map<String, Object> initialData,
	                                               String publicKey) throws EverSdkException {
		return new ContractTemplate(abi(),
		                            tvc().withUpdatedInitialData(sdk, abi(), initialData, publicKey));
	}

}
