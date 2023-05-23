package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jdk.incubator.concurrent.StructuredTaskScope;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.utils.Objs;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNullElse;

public record DeployHandle<RETURN>(Class<RETURN> clazz,
                                   Sdk sdk,
                                   ContractAbi abi,
                                   Tvc tvc,
                                   int workchainId,
                                   Credentials credentials,
                                   Map<String, Object> initialDataFields,
                                   Map<String, Object> constructorInputs,
                                   Abi.FunctionHeader constructorHeader) {

	//TODO Add DeployHandle.Builder and method toBuilder()

	private static System.Logger logger = System.getLogger(DeployHandle.class.getName());

	private static JsonMapper MAPPER = JsonMapper.builder()
	                                             .addModules(new ParameterNamesModule(),
	                                                         new Jdk8Module(),
	                                                         new JavaTimeModule())
	                                             .build();

	public <T> DeployHandle<T> withReturnClass(Class<T> returnClass) {
		return new DeployHandle<>(returnClass,
		                          sdk(),
		                          abi(),
		                          tvc(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withConstructorHeader(Abi.FunctionHeader constructorHeader) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          abi(),
		                          tvc(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader);
	}

	public DeployHandle<RETURN> withConstructorInputs(Map<String, Object> constructorInputs) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          abi(),
		                          tvc(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs,
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withInitDataFields(Map<String, Object> initialDataFields) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          abi(),
		                          tvc(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields,
		                          constructorInputs(),
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withCredentials(Credentials credentials) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          abi(),
		                          tvc(),
		                          workchainId(),
		                          credentials,
		                          initialDataFields(),
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

	public Abi.Signer toSigner() {
		return Objs.notNullElse(credentials(), Credentials.NONE).signer();
	}

	public String toAddress() throws EverSdkException {
		return Abi.encodeMessage(
				sdk().context(),
				abi().ABI(),
				null,
				toDeploySet(),
				null,
				toSigner(),
				null,
				null
		).address();
	}

	public RETURN deployWithGiver(Giver giver, BigInteger value) throws EverSdkException {
		var address = toAddress();
		giver.give(address, value).call();
		return deploy(address);
	}

	public RETURN deploy() throws EverSdkException {
		var address = toAddress();
		return deploy(address);
	}

	private RETURN deploy(String address) throws EverSdkException {
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
					toSigner(),
					null,
					null,
					false
			);
			Map<String,Object> contractMap = Map.of(
					"sdk", sdk(),
					"address", address,
					"abi",abi(),
					"credentials",credentials()
			);
			//return MAPPER.convertValue(contractMap, clazz());
			return  Contract.instantiate(clazz(), sdk(), address, abi(), credentials());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


}
