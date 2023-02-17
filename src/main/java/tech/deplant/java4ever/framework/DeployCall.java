package tech.deplant.java4ever.framework;

import jdk.incubator.concurrent.StructuredTaskScope;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.Giver;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNullElse;

public record DeployCall<RETURN>(Class<RETURN> clazz,
                                 Sdk sdk,
                                 ContractAbi abi,
                                 Tvc tvc,
                                 int workchainId,
                                 Credentials credentials,
                                 Map<String, Object> initialDataFields,
                                 Map<String, Object> constructorInputs,
                                 Abi.FunctionHeader constructorHeader) {

	//TODO Add DeployHandle.Builder and method toBuilder()

	private static System.Logger logger = System.getLogger(FunctionCall.class.getName());

	public <T> DeployCall<T> withReturnClass(Class<T> returnClass) {
		return new DeployCall<>(returnClass,
		                        sdk(),
		                        abi(),
		                        tvc(),
		                        workchainId(),
		                        credentials(),
		                        initialDataFields(),
		                        constructorInputs(),
		                        constructorHeader());
	}

	public DeployCall<RETURN> withConstructorHeader(Abi.FunctionHeader constructorHeader) {
		return new DeployCall<>(clazz(),
		                        sdk(),
		                        abi(),
		                        tvc(),
		                        workchainId(),
		                        credentials(),
		                        initialDataFields(),
		                        constructorInputs(),
		                        constructorHeader);
	}

	public DeployCall<RETURN> withConstructorInputs(Map<String, Object> constructorInputs) {
		return new DeployCall<>(clazz(),
		                        sdk(),
		                        abi(),
		                        tvc(),
		                        workchainId(),
		                        credentials(),
		                        initialDataFields(),
		                        constructorInputs,
		                        constructorHeader());
	}

	public DeployCall<RETURN> withInitDataFields(Map<String, Object> initialDataFields) {
		return new DeployCall<>(clazz(),
		                        sdk(),
		                        abi(),
		                        tvc(),
		                        workchainId(),
		                        credentials(),
		                        initialDataFields,
		                        constructorInputs(),
		                        constructorHeader());
	}

	public Abi.DeploySet toDeploySet() throws EverSdkException {
		return new Abi.DeploySet(tvc().base64String(),
		                         workchainId(),
		                         abi().convertInitDataInputs(initialDataFields()),
		                         requireNonNullElse(credentials(), Credentials.NONE).publicKey());
	}

	public Abi.CallSet toConstructorCallSet() throws EverSdkException {
		return new Abi.CallSet("constructor",
		                       constructorHeader(),
		                       abi().convertFunctionInputs("constructor", constructorInputs()));
	}

	public Abi.Signer sign() {
		return requireNonNullElse(credentials(), Credentials.NONE).signer();
	}

	public String calculateAddress() throws EverSdkException {
		return Abi.encodeMessage(
				sdk().context(),
				abi().ABI(),
				null,
				toDeploySet(),
				null,
				sign(),
				null
		).address();
	}

	public RETURN deployWithGiver(Giver giver, BigInteger value) throws EverSdkException {
		var address = calculateAddress();
		giver.give(address, value).call();
		return deploy(address);
	}

	public RETURN deploy() throws EverSdkException {
		var address = calculateAddress();
		return deploy(address);
	}

	public RETURN deploy(String address) throws EverSdkException {

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			final Future<Abi.DeploySet> deploySetFuture = scope.fork(this::toDeploySet);
			final Future<Abi.CallSet> callSetFuture = scope.fork(this::toConstructorCallSet);
			//final Future<String> addressFuture = scope.fork(this::calculateAddress);
			scope.join();
			Processing.processMessage(
					sdk().context(),
					abi().ABI(),
					address,
					deploySetFuture.resultNow(),
					callSetFuture.resultNow(),
					sign(),
					null,
					false
			);
			return Contract.instantiate(clazz(), sdk(), address, abi(), credentials());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


}
