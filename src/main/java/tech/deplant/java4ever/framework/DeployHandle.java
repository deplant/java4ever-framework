package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.AbstractTemplate;
import tech.deplant.java4ever.framework.template.Template;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNullElse;

/**
 * Representation of prepared deployment set. Usually this object is returned by template's prepareDeploy() method.
 *
 * @param <RETURN> the type parameter
 */
public record DeployHandle<RETURN extends AbstractContract>(Class<RETURN> clazz,
                                                            int sdk,
                                                            Template template,
                                                            long workchainId,
                                                            Credentials credentials,
                                                            Map<String, Object> initialDataFields,
                                                            Map<String, Object> constructorInputs,
                                                            Abi.FunctionHeader constructorHeader,
                                                            String abiVersion,
                                                            DebugOptions debugOptions) {

	private static System.Logger logger = System.getLogger(DeployHandle.class.getName());

	//TODO Add DeployHandle.Builder and method toBuilder()

	/**
	 * Instantiates a new Deploy handle.
	 *
	 * @param clazz             the clazz
	 * @param sdk               the sdk
	 * @param abi               the abi
	 * @param tvc               the tvc
	 * @param workchainId       the workchain id
	 * @param credentials       the credentials
	 * @param initialDataFields the initial data fields
	 * @param constructorInputs the constructor inputs
	 * @param constructorHeader the constructor header
	 */
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

	/**
	 * Instantiates a new Deploy handle.
	 *
	 * @param clazz             the clazz
	 * @param sdk               the sdk
	 * @param template          the template
	 * @param workchainId       the workchain id
	 * @param credentials       the credentials
	 * @param initialDataFields the initial data fields
	 * @param constructorInputs the constructor inputs
	 * @param constructorHeader the constructor header
	 */
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
		     "2.2",
		     new DebugOptions(false, 60000L, false, 50L));
	}

	public String encodeInitialDataBase64() throws EverSdkException {
		var result = EverSdk.await(Abi.encodeInitialData(sdk(),
		                                                 template().abi().ABI(),
		                                                 JsonContext.convertAbiMap(initialDataFields(), JsonNode.class),
		                                                 credentials().publicKey(),
		                                                 null));
		return result.data();
	}

	/**
	 * With debug tree deploy handle.
	 *
	 * @param enabled             the enabled
	 * @param timeout             the timeout
	 * @param throwErrors         the throw errors
	 * @param maxTransactionCount the max transaction count
	 * @param treeAbis            the tree abis
	 * @return the deploy handle
	 */
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
		                          abiVersion(),
		                          new DebugOptions(enabled, timeout, throwErrors, maxTransactionCount, treeAbis));
	}

	/**
	 * With debug tree deploy handle.
	 *
	 * @param debugOptions the debug options
	 * @return the deploy handle
	 */
	public DeployHandle<RETURN> withDebugTree(DebugOptions debugOptions) {
		return new DeployHandle<>(clazz(),
		                          sdk(),
		                          template(),
		                          workchainId(),
		                          credentials(),
		                          initialDataFields(),
		                          constructorInputs(),
		                          constructorHeader(),
		                          abiVersion(),
		                          debugOptions);
	}

	/**
	 * With return class deploy handle.
	 *
	 * @param <T>         the type parameter
	 * @param returnClass the return class
	 * @return the deploy handle
	 */
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

	/**
	 * With constructor header deploy handle.
	 *
	 * @param constructorHeader the constructor header
	 * @return the deploy handle
	 */
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

	/**
	 * With constructor inputs deploy handle.
	 *
	 * @param constructorInputs the constructor inputs
	 * @return the deploy handle
	 */
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

	/**
	 * With init data fields deploy handle.
	 *
	 * @param initialDataFields the initial data fields
	 * @return the deploy handle
	 */
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

	/**
	 * With credentials deploy handle.
	 *
	 * @param credentials the credentials
	 * @return the deploy handle
	 */
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

	/**
	 * To deploy set abi . deploy set.
	 *
	 * @return the abi . deploy set
	 * @throws EverSdkException the ever sdk exception
	 */
	public Abi.DeploySet toDeploySet() throws EverSdkException {
		return new Abi.DeploySet(template().tvc().base64String(),
		                         null,
		                         null,
		                         workchainId(),
		                         JsonContext.ABI_JSON_MAPPER()
		                                    .valueToTree(template().abi().convertInitDataInputs(initialDataFields())),
		                         requireNonNullElse(credentials(), Credentials.NONE).publicKey());
	}

	/**
	 * To constructor call set abi . call set.
	 *
	 * @return the abi . call set
	 * @throws EverSdkException the ever sdk exception
	 */
	public Abi.CallSet toConstructorCallSet() throws EverSdkException {
		return new Abi.CallSet("constructor",
		                       constructorHeader(),
		                       JsonContext.ABI_JSON_MAPPER()
		                                  .valueToTree(template().abi()
		                                                         .convertFunctionInputs("constructor",
		                                                                                constructorInputs())));
	}

	/**
	 * To signer abi . signer.
	 *
	 * @return the abi . signer
	 */
	public Abi.Signer toSigner() {
		return Objs.notNullElse(credentials(), Credentials.NONE).signer();
	}

	/**
	 * To address address.
	 *
	 * @return the address
	 * @throws EverSdkException the ever sdk exception
	 */
	public Address toAddress() throws EverSdkException {
		return new Address(EverSdk.await(Abi.encodeMessage(sdk(),
		                                                   template().abi().ABI(),
		                                                   null,
		                                                   toDeploySet(),
		                                                   null,
		                                                   toSigner(),
		                                                   null,
		                                                   null)).address());
	}

	/**
	 * Deploy with giver return.
	 *
	 * @param giver the giver
	 * @param value the value
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN deployWithGiver(GiverContract giver, BigInteger value) throws EverSdkException {
		var address = toAddress();
		try {
			var uninitContract = new AbstractContract(sdk(), address, template().abi(), Credentials.NONE);
			final CompletableFuture<JsonNode> waiter = new CompletableFuture<>();
			Consumer<JsonNode> eventConsumer = waiter::complete;
			uninitContract.subscribeOnTransactions(eventConsumer, "in_message", "{ src }", "aborted", "status")
			              .subscribeUntilFirst(sdk());
			giver.give(address, value).call();
			waiter.get(10, TimeUnit.MINUTES);
			return deploy(address);
		} catch (InterruptedException e) {
			logger.log(System.Logger.Level.ERROR, () -> "Wait for giver funds interrupted! Message: " + e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR, () -> "Wait for giver funds timeout! Message: " + e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-402, "EVER-SDK Execution expired on Timeout!"),
			                           e);
		} catch (ExecutionException e) {
			logger.log(System.Logger.Level.ERROR, () -> "Wait for giver funds failed! Message: " + e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call failed!"), e);
		}

	}

	/**
	 * Deploy return.
	 *
	 * @return the return
	 * @throws EverSdkException the ever sdk exception
	 */
	public RETURN deploy() throws EverSdkException {
		var address = toAddress();
		return deploy(address);
	}

	private RETURN deploy(Address address) throws EverSdkException {
		EverSdk.await(Processing.processMessage(sdk(),
		                                        template().abi().ABI(),
		                                        address.makeAddrStd(),
		                                        toDeploySet(),
		                                        toConstructorCallSet(),
		                                        toSigner(),
		                                        null,
		                                        null,
		                                        false));
		Map<String, Object> contractMap = Map.of("sdk",
		                                         sdk(),
		                                         "address",
		                                         address,
		                                         "abi",
		                                         template().abi(),
		                                         "credentials",
		                                         credentials());
		var contract = Contract.instantiate(clazz(), sdk(), address.makeAddrStd(), template().abi(), credentials());
		logger.log(System.Logger.Level.TRACE,
		           () -> "Contract deployed and instantiated: %s".formatted(
				           contract == null ? "" : contract.toString()));
		return contract;
	}


}
