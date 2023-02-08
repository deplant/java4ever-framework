package tech.deplant.java4ever.framework.contract;

import jdk.incubator.concurrent.StructuredTaskScope;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.util.Map;
import java.util.concurrent.Future;

public record DeployHandle<RETURN>(Sdk sdk,
                                   TemplateHandle templateHandle,
                                   int workchainId,
                                   Credentials credentials,
                                   Map<String, Object> initialDataFields,
                                   Map<String, Object> constructorInputs,
                                   Abi.FunctionHeader functionHeader) {

	//TODO Add DeployHandle.Builder and method toBuilder()

	private static System.Logger logger = System.getLogger(CallHandle.class.getName());

	public RETURN deploy() throws EverSdkException {

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			final Future<Abi.DeploySet> deploySetFuture = scope.fork(() -> new Abi.DeploySet(
					templateHandle().tvc().base64String(),
					workchainId(),
					templateHandle().abi().convertInitDataInputs(
							initialDataFields()),
					credentials().publicKey())
			);
			final Future<Abi.CallSet> callSetFuture = scope.fork(() -> new Abi.CallSet(
					"constructor",
					null,
					templateHandle().abi().convertFunctionInputs(
							"constructor",
							this.constructorInputs)));
			var address = templateHandle().calculateAddress(this.sdk, initialDataFields(), this.credentials);
			scope.join();
			Processing.processMessage(
					this.sdk.context(),
					templateHandle().abi().ABI(),
					address,
					deploySetFuture.resultNow(),
					callSetFuture.resultNow(),
					this.credentials.signer(),
					null,
					false
			);
			return new OwnedContract(this.sdk, address, abi(), this.credentials);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


}
