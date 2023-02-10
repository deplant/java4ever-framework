package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import jdk.incubator.concurrent.StructuredTaskScope;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNullElse;

public record DeployCall<RETURN>(Sdk sdk,
                                 Template templateHandle,
                                 int workchainId,
                                 Credentials credentials,
                                 Map<String, Object> initialDataFields,
                                 Map<String, Object> constructorInputs,
                                 Abi.FunctionHeader constructorHeader) {

	//TODO Add DeployHandle.Builder and method toBuilder()

	private static System.Logger logger = System.getLogger(FunctionCall.class.getName());

	public Abi.DeploySet toDeploySet() throws EverSdkException {
		return new Abi.DeploySet(templateHandle().tvc().base64String(),
		                         workchainId(),
		                         templateHandle().abi().convertInitDataInputs(initialDataFields()),
		                         requireNonNullElse(credentials(), Credentials.NONE).publicKey());
	}

	public Abi.CallSet toConstructorCallSet() throws EverSdkException {
		return new Abi.CallSet("constructor",
		                       constructorHeader(),
		                       templateHandle().abi().convertFunctionInputs("constructor", constructorInputs()));
	}

	public Abi.Signer sign() {
		return requireNonNullElse(credentials(), Credentials.NONE).signer();
	}

	public String calculateAddress() throws EverSdkException {
		return Abi.encodeMessage(
				sdk().context(),
				templateHandle().abi().ABI(),
				null,
				toDeploySet(),
				null,
				sign(),
				null
		).address();
	}

	public RETURN deploy() throws EverSdkException {

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			final Future<Abi.DeploySet> deploySetFuture = scope.fork(this::toDeploySet);
			final Future<Abi.CallSet> callSetFuture = scope.fork(this::toConstructorCallSet);
			final Future<String> addressFuture = scope.fork(this::calculateAddress);
			scope.join();
			var address = addressFuture.resultNow();
			Processing.processMessage(
					sdk().context(),
					templateHandle().abi().ABI(),
					address,
					deploySetFuture.resultNow(),
					callSetFuture.resultNow(),
					sign(),
					null,
					false
			);
			var map = Map.of(
					"sdk", sdk(),
					"address", address,
					"abi", templateHandle().abi(),
					"credentials", credentials());
			return sdk().convertMap(map, new TypeReference<>() {
			});
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


}
