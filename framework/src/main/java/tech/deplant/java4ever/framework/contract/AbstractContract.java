package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.subscription.Subscriptions;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class that represents deployed contract in one of the networks. It holds info about
 * network (sdk), address and abi of contract. If you own this contract, initialize it
 * with correct credentials.
 * If it's you are not a contract owner, use shorter constructor or explicit Credentials.NONE.
 * You can make calls to contract with prepareCall() method.
 */
public class AbstractContract implements Contract {

	private final int contextId;
	private final Address address;
	private final ContractAbi abi;
	private Abi.Signer signer;

	/**
	 * Instantiates a new Abstract contract.
	 *
	 * @param contextId   the context id
	 * @param address     the address
	 * @param abi         the abi
	 * @param credentials the credentials
	 */
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public AbstractContract(@JsonProperty(value = "contextId") int contextId,
	                        @JsonProperty(value = "address") Address address,
	                        @JsonProperty(value = "abi") ContractAbi abi,
	                        @JsonProperty(value = "credentials") Credentials credentials) {
		this(contextId, address, abi, Objs.notNullReplaceElse(credentials,credentials.signer(),new Abi.Signer.None()));
	}

	/**
	 * Instantiates a new Abstract contract.
	 *
	 * @param contextId   the context id
	 * @param address     the address
	 * @param abi         the abi
	 * @param credentials the credentials
	 */
	public AbstractContract(@JsonProperty(value = "contextId") int contextId,
	                        @JsonProperty(value = "address") String address,
	                        @JsonProperty(value = "abi") ContractAbi abi,
	                        @JsonProperty(value = "credentials") Credentials credentials) {
		this(contextId, new Address(address), abi, credentials);
	}

	/**
	 * Instantiates a new Abstract contract.
	 *
	 * @param contextId   the context id
	 * @param address     the address
	 * @param abi         the abi
	 * @param signer the signer
	 */
	public AbstractContract(int contextId,
	                        Address address,
	                        ContractAbi abi,
	                        Abi.Signer signer) {
		this.contextId = contextId;
		this.address = address;
		this.abi = abi;
		this.signer = signer;
	}

//	/**
//	 * Wait for transaction.
//	 *
//	 * @param from           the from
//	 * @param onlySuccessful the only successful
//	 * @param startEvent     the start event
//	 * @throws EverSdkException     the ever sdk exception
//	 * @throws InterruptedException the interrupted exception
//	 * @throws TimeoutException     the timeout exception
//	 */
//	public void waitForTransaction(Address from,
//	                               boolean onlySuccessful,
//	                               Runnable startEvent) throws EverSdkException, InterruptedException, TimeoutException {
//
//		CompletableFuture<JsonNode> futureSubscriptionResult = new CompletableFuture<>();
//
//		new SubscribeHandle(contextId(),
//		                    SubscribeHandle.TRANSACTIONS_SUB.formatted(address(),
//		                                                               "in_message { src } aborted status"))
//				//.addConsumeFilter(SubscribeHandle.TR_SUCCESSFUL)
//				.addCallbackConsumer(futureSubscriptionResult::complete)
//				.subscribe();
//
//		startEvent.run();
//		try {
//			futureSubscriptionResult.get();
//		} catch (ExecutionException e) {
//			throw new RuntimeException(e);
//		}
////				var transaction = subscribeEvent.result().get("result").get("transactions");
////				if (Objs.isNotNull(transaction.get("in_message")) &&
////				    Objs.isNotNull(transaction.get("in_message").get("src")) &&
////				    transaction.get("in_message").get("src").asText().equals(from.toString())) {
////					if (!onlySuccessful ||
////					    (Objs.isNotNull(transaction.get("aborted")) &&
////					     Objs.isNotNull(transaction.get("status")) &&
////					     !transaction.get("aborted").asBoolean() &&
////					     transaction.get("status").asInt() == TransactionStatus.FINALIZED.value())) {
////						logger.log(System.Logger.Level.TRACE, () -> "Await change!!!");
////						awaitDone.set(true);
////						logger.log(System.Logger.Level.TRACE, () -> "Await Done!!!");
////					}
////				}
////			}
//	}

	/**
	 * Subscribe on incoming messages subscribe handle.
	 *
	 * @param resultFields           the result fields
	 * @param subscribeEventConsumer the subscribe event consumer
	 * @return the subscribe handle
	 * @throws EverSdkException the ever sdk exception
	 */
	public Subscriptions.Builder subscribeOnIncomingMessages(String resultFields,
	                                                         Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		return Subscriptions.onMessages(resultFields)
		                    .addFilterOnSubscription("dst: { eq: \"%s\" }".formatted(address().toString()))
		                    .addCallbackConsumer(subscribeEventConsumer);
	}

	/**
	 * Subscribe on outgoing messages subscribe handle.
	 *
	 * @param resultFields           the result fields
	 * @param subscribeEventConsumer the subscribe event consumer
	 * @return the subscribe handle
	 * @throws EverSdkException the ever sdk exception
	 */
	public Subscriptions.Builder subscribeOnOutgoingMessages(String resultFields,
	                                                         Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		return Subscriptions.onMessages(resultFields)
		                    .addFilterOnSubscription("src: { eq: \"%s\" }".formatted(address().toString()))
		                    .addCallbackConsumer(subscribeEventConsumer);
	}

	/**
	 * Subscribe on account subscribe handle.
	 *
	 * @param resultFields           the result fields
	 * @param subscribeEventConsumer the subscribe event consumer
	 * @return the subscribe handle
	 * @throws EverSdkException the ever sdk exception
	 */
	public Subscriptions.Builder subscribeOnAccount(String resultFields,
	                                                Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		return Subscriptions.onAccounts(resultFields)
		                    .addFilterOnSubscription("id: { eq: \"%s\" }".formatted(address().toString()))
		                    .addCallbackConsumer(subscribeEventConsumer);
	}

	/**
	 * Subscribe on transactions subscribe handle.
	 *
	 * @param resultFields           the result fields
	 * @param subscribeEventConsumer the subscribe event consumer
	 * @return the subscribe handle
	 * @throws EverSdkException the ever sdk exception
	 */
	public Subscriptions.Builder subscribeOnTransactions(Consumer<JsonNode> subscribeEventConsumer, String... resultFields) throws EverSdkException {
		return Subscriptions.onTransactions(resultFields)
		                    .addFilterOnSubscription("account_addr: { eq: \"%s\" }".formatted(address().toString()))
		                    .addCallbackConsumer(subscribeEventConsumer);
	}

	/**
	 * Function call builder function handle . builder.
	 *
	 * @return the function handle . builder
	 */
	public FunctionHandle.Builder functionCallBuilder() {
		return new FunctionHandle.Builder(JsonNode.class).setContract(this);
	}

	@Override
	public int contextId() {
		return this.contextId;
	}

	@Override
	public Address address() {
		return address;
	}

	@Override
	public ContractAbi abi() {
		return abi;
	}

	@Override
	public Credentials credentials() {
		return switch (signer()) {
			case Abi.Signer.SigningBox _ -> throw new UnsupportedOperationException("You can't get credentials for contract that use signing_box as a signer.");
			case Abi.Signer.Keys keys -> new Credentials(keys.keys().publicKey(),keys.keys().secretKey());
			case Abi.Signer.External ext -> throw new UnsupportedOperationException("You can't get credentials for contract that use external signer.");
			case Abi.Signer.None _ -> Credentials.NONE;
		};
	}

	@Override
	public Abi.Signer signer() {
		return this.signer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address(), abi(), credentials());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AbstractContract that)) {
			return false;
		}
		return Objects.equals(address(), that.address()) && Objects.equals(abi(), that.abi()) &&
		       Objects.equals(credentials(), that.credentials());
	}
}
