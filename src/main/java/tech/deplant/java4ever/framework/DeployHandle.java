package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.AbstractTemplate;
import tech.deplant.java4ever.framework.template.Template;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNullElse;

/**
 * Representation of prepared deployment set. Usually this object is returned by template's prepareDeploy() method.
 *
 * @param clazz             class of contract wrapper
 * @param sdk
 * @param template
 * @param workchainId
 * @param credentials
 * @param initialDataFields
 * @param constructorInputs
 * @param constructorHeader
 * @param <RETURN>
 */
public record DeployHandle<RETURN extends AbstractContract>(Class<RETURN> clazz,
                                                            int sdk,
                                                            Template template,
                                                            long workchainId,
                                                            Credentials credentials,
                                                            Map<String, Object> initialDataFields,
                                                            Map<String, Object> constructorInputs,
                                                            Abi.FunctionHeader constructorHeader,
                                                            DebugOptions debugOptions) {

	private static System.Logger logger = System.getLogger(DeployHandle.class.getName());

	//TODO Add DeployHandle.Builder and method toBuilder()
	private static JsonMapper MAPPER = JsonMapper.builder()
	                                             .addModules(new ParameterNamesModule(),
	                                                         new Jdk8Module(),
	                                                         new JavaTimeModule())
	                                             .build();

	public DeployHandle(Class<RETURN> clazz,
	                    int sdk,
	                    ContractAbi abi,
	                    Tvc tvc,
	                    long workchainId,
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

	public DeployHandle(Class<RETURN> clazz,
	                    int sdk,
	                    Template template,
	                    long workchainId,
	                    Credentials credentials,
	                    Map<String, Object> initialDataFields,
	                    Map<String, Object> constructorInputs,
	                    Abi.FunctionHeader constructorHeader) {
		this(clazz,
		     sdk,
		     template,
		     workchainId,
		     credentials,
		     initialDataFields,
		     constructorInputs,
		     constructorHeader,
		     new DebugOptions(false, 60000L, false, 50L));
	}

	public DeployHandle<RETURN> withDebugTree(boolean enabled,
	                                          long timeout,
	                                          boolean throwErrors,
	                                          long maxTransactionCount,
	                                          ContractAbi... treeAbis) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader(),
		                          new DebugOptions(enabled, timeout, throwErrors, maxTransactionCount, treeAbis));
	}

	public DeployHandle<RETURN> withDebugTree(DebugOptions debugOptions) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader(),
		                          debugOptions);
	}

	public <T extends AbstractContract> DeployHandle<T> withReturnClass(Class<T> returnClass) {
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
		                         JsonContext.ABI_JSON_MAPPER()
		                                    .valueToTree(template().abi().convertInitDataInputs(initialDataFields())),
		                         requireNonNullElse(credentials(), Credentials.NONE).publicKey());
	}

	public Abi.CallSet toConstructorCallSet() throws EverSdkException {
		return new Abi.CallSet("constructor",
		                       constructorHeader(),
		                       JsonContext.ABI_JSON_MAPPER()
		                                  .valueToTree(template().abi()
		                                                         .convertFunctionInputs("constructor",
		                                                                                constructorInputs())));
	}

	public Abi.Signer toSigner() {
		return Objs.notNullElse(credentials(), Credentials.NONE).signer();
	}

	public Address toAddress() throws EverSdkException {
		return new Address(Abi.encodeMessage(sdk(),
		                         template().abi().ABI(),
		                         null,
		                         toDeploySet(),
		                         null,
		                         toSigner(),
		                         null,
		                         null).address());
	}

	public RETURN deployWithGiver(GiverContract giver, BigInteger value) throws EverSdkException {
		var address = toAddress();
		try {
			new AbstractContract(sdk(), address, template().abi(), Credentials.NONE).waitForTransaction(giver.address(),
			                                                                                            false,
			                                                                                            () -> {
				                                                                                            try {
					                                                                                            giver.give(
							                                                                                                 address,
							                                                                                                 value)
					                                                                                                 .call();
				                                                                                            } catch (
						                                                                                            EverSdkException e) {
					                                                                                            logger.log(
							                                                                                            System.Logger.Level.ERROR,
							                                                                                            () -> "Error! Message: " +
							                                                                                                  e.getMessage());
					                                                                                            throw new RuntimeException(
							                                                                                            e);
				                                                                                            }
			                                                                                            });
			return deploy(address);
		} catch (InterruptedException e) {
			logger.log(System.Logger.Level.ERROR, () -> "Wait for giver funds interrupted! Message: " + e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Wait for giver funds timeout! Message: " +
			                 e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-402,
			                                                            "EVER-SDK Execution expired on Timeout!"), e);
		}

	}

	public RETURN deploy() throws EverSdkException {
		var address = toAddress();
		return deploy(address);
	}

	private RETURN deploy(Address address) throws EverSdkException {
		Processing.processMessage(sdk(),
		                          template().abi().ABI(),
		                          address.makeAddrStd(),
		                          toDeploySet(),
		                          toConstructorCallSet(),
		                          toSigner(),
		                          null,
		                          null,
		                          false);
		Map<String, Object> contractMap = Map.of("sdk",
		                                         sdk(),
		                                         "address",
		                                         address,
		                                         "abi",
		                                         template().abi(),
		                                         "credentials",
		                                         credentials());
		return Contract.instantiate(clazz(), sdk(), address.makeAddrStd(), template().abi(), credentials());
	}


}
