package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.datatype.Address;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Class that represents deployed contract in one of the networks. It holds info about
 * network (sdk), address and abi of contract. If you own this contract, initialize it
 * with correct credentials.
 * If it's foreign contract, use shorter constructor oe explicit Credentials.NONE.
 * You can make calls to contract with prepareCall() method.
 */
public class AbstractContract implements Contract {

	private final Sdk sdk;
	private final String address;
	private final ContractAbi abi;
	private Credentials credentials;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public AbstractContract(@JsonProperty(value = "sdk") Sdk sdk,
	                        @JsonProperty(value = "address") String address,
	                        @JsonProperty(value = "abi") ContractAbi abi,
	                        @JsonProperty(value = "credentials") Credentials credentials) {
		this.sdk = sdk;
		this.address = address;
		this.abi = abi;
		this.credentials = credentials;
	}

	public void waitForTransaction(Address from,
	                               boolean onlySuccessful,
	                               Runnable startEvent) throws EverSdkException, InterruptedException, TimeoutException {

		CompletableFuture<JsonNode> futureSubscriptionResult = new CompletableFuture<>();

		new SubscribeHandle(sdk(),
		                    SubscribeHandle.TRANSACTIONS_SUB.formatted(address(),
		                                                               "in_message { src } aborted status"))
				.setStopOnFilter(SubscribeHandle.TR_SUCCESSFUL)
				.subscribe(futureSubscriptionResult::complete);

		startEvent.run();
		try {
			futureSubscriptionResult.get();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
//				var transaction = subscribeEvent.result().get("result").get("transactions");
//				if (Objs.isNotNull(transaction.get("in_message")) &&
//				    Objs.isNotNull(transaction.get("in_message").get("src")) &&
//				    transaction.get("in_message").get("src").asText().equals(from.toString())) {
//					if (!onlySuccessful ||
//					    (Objs.isNotNull(transaction.get("aborted")) &&
//					     Objs.isNotNull(transaction.get("status")) &&
//					     !transaction.get("aborted").asBoolean() &&
//					     transaction.get("status").asInt() == TransactionStatus.FINALIZED.value())) {
//						logger.log(System.Logger.Level.TRACE, () -> "Await change!!!");
//						awaitDone.set(true);
//						logger.log(System.Logger.Level.TRACE, () -> "Await Done!!!");
//					}
//				}
//			}
	}

	public SubscribeHandle subscribeOnIncomingMessages(String resultFields,
	                                                   Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							messages(
									filter: {
										dst: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(), resultFields);
		return new SubscribeHandle(sdk(), queryText).subscribe(subscribeEventConsumer);
	}

	public SubscribeHandle subscribeOnOutgoingMessages(String resultFields,
	                                                   Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							messages(
									filter: {
										src: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(), resultFields);
		return new SubscribeHandle(sdk(), queryText).subscribe(subscribeEventConsumer);
	}

	public SubscribeHandle subscribeOnAccount(String resultFields,
	                                          Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							accounts(
									filter: {
										id: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(), resultFields);
		return new SubscribeHandle(sdk(), queryText).subscribe(subscribeEventConsumer);
	}

	public SubscribeHandle subscribeOnTransactions(String resultFields,
	                                               Consumer<JsonNode> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							transactions(
									filter: {
										account_addr: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(), resultFields);
		return new SubscribeHandle(sdk(), queryText).subscribe(subscribeEventConsumer);
	}

	public FunctionHandle.Builder createFunctionCall() {
		return new FunctionHandle.Builder(JsonNode.class).setContract(this);
	}

	@Override
	public Sdk sdk() {
		return sdk;
	}

	@Override
	public String address() {
		return address;
	}

	@Override
	public ContractAbi abi() {
		return abi;
	}

	@Override
	public Credentials credentials() {
		return credentials;
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
