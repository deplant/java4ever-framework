package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.ffi.EverSdkSubscription;
import tech.deplant.java4ever.framework.gql.TransactionStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SubscribeHandle implements AutoCloseable {

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

	public static JsonNode singleJsonGetSyncAwait(int contextId, String queryText) throws ExecutionException, InterruptedException, TimeoutException, EverSdkException {
		CompletableFuture<JsonNode> node = new CompletableFuture<>();
		Consumer<JsonNode> consumer = node::complete;
		new SubscribeHandle(contextId, queryText)
				.addEventConsumer(consumer)
				.subscribe();
		return node.get(300000L, TimeUnit.MILLISECONDS);
	}

	private static System.Logger logger = System.getLogger(SubscribeHandle.class.getName());
	private final int contextId;
	private final String queryText;
	private long handle;
	private final Set<Predicate<JsonNode>> unsubscribeFilters = new HashSet<>();

	private final Set<Predicate<JsonNode>> consumeFilters = new HashSet<>();
	private final Set<Consumer<JsonNode>> consumers = new HashSet<>();

	public SubscribeHandle(int contextId, String queryText) {
		this.contextId = contextId;
		this.queryText = queryText;
	}

	public SubscribeHandle addEventConsumer(Consumer<JsonNode> eventConsumer) {
		consumers.add(eventConsumer);
		return this;
	}

	public SubscribeHandle addConsumeFilter(Predicate<JsonNode> consumeFilter) {
		this.consumeFilters.add(consumeFilter);
		return this;
	}

	public SubscribeHandle addUnsubscribeFilter(Predicate<JsonNode> unsubscribeFilter) {
		this.unsubscribeFilters.add(unsubscribeFilter);
		return this;
	}

	public SubscribeHandle subscribe() throws EverSdkException {

		var subscription = new EverSdkSubscription(eventJson -> {
			logger.log(System.Logger.Level.TRACE, "Event received: " + eventJson);
			// if we have stop subscription filter and it passes - unsubscribe
			if (testConsumeFilters(eventJson)) {
				if (testUnsubscribeFilters(eventJson)) {
					if (!this.consumers.isEmpty()) {
						unsubscribe();
					}
				}
				broadcastToConsumers(eventJson);
			}
		});

		this.handle = Net.subscribe(this.contextId,
		                            queryText(),
		                            JsonContext.EMPTY_NODE(),
		                            subscription
		).handle();
		return this;
	}

	private boolean testUnsubscribeFilters(JsonNode jsonNode) {
		return this.unsubscribeFilters.isEmpty() || this.unsubscribeFilters.stream().anyMatch(flt -> flt.test(jsonNode));
	}

	private boolean testConsumeFilters(JsonNode jsonNode) {
		return this.consumeFilters.isEmpty() || this.consumeFilters.stream().allMatch(flt -> flt.test(jsonNode));
	}

	private void broadcastToConsumers(JsonNode jsonNode) {
		this.consumers.forEach(consumer -> consumer.accept(jsonNode));
	}

	public void unsubscribe() {
		try {
			if (handle() > 0) {
				logger.log(System.Logger.Level.TRACE,
				           () -> "HANDLE:%d Unsubscribing...".formatted(
						           this.handle));
				Net.unsubscribe(this.contextId, new Net.ResultOfSubscribeCollection(handle()));
			}
		} catch (EverSdkException e) {
			// I think there's no reason to fail everything if unsubscribe failed...
			logger.log(System.Logger.Level.ERROR,
			           () -> "HANDLE:%d Unsubscribe failed!".formatted(
					           this.handle));
		}

	}

	public int contextId() {
		return contextId;
	}

	public String queryText() {
		return queryText;
	}

	public long handle() {
		return handle;
	}

	@Override
	public void close() {
		unsubscribe();
	}
}
