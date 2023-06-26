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
import tech.deplant.java4ever.framework.template.AbstractTemplate;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.java4ever.utils.Objs;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNullElse;

/**
 * Representation of prepared deployment set. Usually this object is returned by template's prepareDeploy() method.
 *
 * @param clazz class of contract wrapper
 * @param sdk
 * @param template
 * @param workchainId
 * @param credentials
 * @param initialDataFields
 * @param constructorInputs
 * @param constructorHeader
 * @param <RETURN>
 */
public record DeployHandle<RETURN>(Class<RETURN> clazz,
                                   Sdk sdk,
                                   Template template,
                                   int workchainId,
                                   Credentials credentials,
                                   Map<String, Object> initialDataFields,
                                   Map<String, Object> constructorInputs,
                                   Abi.FunctionHeader constructorHeader) {

	private static System.Logger logger = System.getLogger(DeployHandle.class.getName());

	//TODO Add DeployHandle.Builder and method toBuilder()
	private static JsonMapper MAPPER = JsonMapper.builder()
	                                             .addModules(new ParameterNamesModule(),
	                                                         new Jdk8Module(),
	                                                         new JavaTimeModule())
	                                             .build();

	public DeployHandle(Class<RETURN> clazz,
	                    Sdk sdk,
	                    ContractAbi abi,
	                    Tvc tvc,
	                    int workchainId,
	                    Credentials credentials,
	                    Map<String, Object> initialDataFields,
	                    Map<String, Object> constructorInputs,
	                    Abi.FunctionHeader constructorHeader) {
		this(clazz,
		     sdk,
		     new AbstractTemplate(abi, tvc),
		     workchainId,
		     credentials,
		     initialDataFields,
		     constructorInputs,
		     constructorHeader);
	}

	public <T> DeployHandle<T> withReturnClass(Class<T> returnClass) {
		return new DeployHandle<>(returnClass,
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withConstructorHeader(Abi.FunctionHeader constructorHeader) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader);
	}

	public DeployHandle<RETURN> withConstructorInputs(Map<String, Object> constructorInputs) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs,
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withInitDataFields(Map<String, Object> initialDataFields) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields,
		                          constructorInputs(),
		                          constructorHeader());
	}

	public DeployHandle<RETURN> withCredentials(Credentials credentials) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials,
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader());
	}

	public Abi.DeploySet toDeploySet() throws EverSdkException {
		return new Abi.DeploySet(template().tvc().base64String(),
		                         null,
		                         null,
		                         workchainId(),
		                         template().abi().convertInitDataInputs(initialDataFields()),
		                         requireNonNullElse(credentials(), Credentials.NONE).publicKey());
	}

	public Abi.CallSet toConstructorCallSet() throws EverSdkException {
		return new Abi.CallSet("constructor",
		                       constructorHeader(),
		                       template().abi().convertFunctionInputs("constructor", constructorInputs()));
	}

	public Abi.Signer toSigner() {
		return Objs.notNullElse(credentials(), Credentials.NONE).signer();
	}

	public String toAddress() throws EverSdkException {
		return Abi.encodeMessage(
				sdk().context(),
				template().abi().ABI(),
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
					template().abi().ABI(),
					address,
					deploySetFuture.resultNow(),
					callSetFuture.resultNow(),
					toSigner(),
					null,
					null,
					false
			);
			Map<String, Object> contractMap = Map.of(
					"sdk", sdk(),
					"address", address,
					"abi", template().abi(),
					"credentials", credentials()
			);
			//return MAPPER.convertValue(contractMap, clazz());
			return Contract.instantiate(clazz(), sdk(), address, template().abi(), credentials());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


}
