package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.gql.TransactionStatus;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SubscribeHandle {

	public static final Predicate<JsonNode> TR_SUCCESSFUL = node ->
			node.get("result").get("transactions").get("status").asInt() == TransactionStatus.FINALIZED.value() &&
			!node.get("result").get("transactions").get("aborted").asBoolean();

	public static final String TRANSACTIONS_SUB = """
				subscription {
							transactions(
									filter: {
										account_addr: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""";
	private static System.Logger logger = System.getLogger(SubscribeHandle.class.getName());
	private final Sdk sdk;
	private final String queryText;
	private long handle;
	private Predicate<JsonNode> stopOnFilter = eventNode -> true;
	private Consumer<JsonNode> eventsConsumer;


	public SubscribeHandle(Sdk sdk, String queryText) {
		this.sdk = sdk;
		this.queryText = queryText;
	}

	public SubscribeHandle subscribe(Consumer<JsonNode> eventsConsumer) throws EverSdkException {

		setEventsConsumer(eventsConsumer);

		var handle = Net.subscribe(sdk.context(),
		                           queryText(),
		                           JsonContext.EMPTY_NODE(),
		                           eventJson -> {
			                           // if we have stop subscription filter and it passes - unsubscribe
			                           if (stopOnFilter() != null && stopOnFilter().test(eventJson)) {
				                           unsubscribe();
			                           }
			                           logger.log(System.Logger.Level.TRACE, "Event received: " + eventJson);
			                           eventsConsumer().accept(eventJson);
		                           }).handle();
		setHandle(handle);
		return this;
	}

	public void unsubscribe() {
		try {
			if (handle() > 0) {
				logger.log(System.Logger.Level.TRACE,
				           () -> "HANDLE:%d Unsubscribing...".formatted(
						           this.handle));
				Net.unsubscribe(sdk().context(), new Net.ResultOfSubscribeCollection(handle()));
			}
		} catch (EverSdkException e) {
			// I think there's no reason to fail everything if unsubscribe failed...
			logger.log(System.Logger.Level.ERROR,
			           () -> "HANDLE:%d Unsubscribe failed!".formatted(
					           this.handle));
		}

	}

	public Sdk sdk() {
		return sdk;
	}

	public String queryText() {
		return queryText;
	}

	public long handle() {
		return handle;
	}

	public SubscribeHandle setHandle(long handle) {
		this.handle = handle;
		return this;
	}

	public Predicate<JsonNode> stopOnFilter() {
		return stopOnFilter;
	}

	public SubscribeHandle setStopOnFilter(Predicate<JsonNode> stopOnFilter) {
		this.stopOnFilter = stopOnFilter;
		return this;
	}

	public Consumer<JsonNode> eventsConsumer() {
		return eventsConsumer;
	}

	public SubscribeHandle setEventsConsumer(Consumer<JsonNode> eventsConsumer) {
		this.eventsConsumer = eventsConsumer;
		return this;
	}

}
