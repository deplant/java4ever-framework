package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.gql.TransactionStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The type Subscribe handle.
 */
public class SubscribeHandle implements AutoCloseable {

	/**
	 * The constant TR_SUCCESSFUL.
	 */
	public static final Predicate<JsonNode> TR_SUCCESSFUL = node ->
			node.get("result").get("transactions").get("status").asInt() == TransactionStatus.FINALIZED.value() &&
			!node.get("result").get("transactions").get("aborted").asBoolean();

	/**
	 * The constant TRANSACTIONS_SUB.
	 */
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

	/**
	 * Single json get sync await json node.
	 *
	 * @param contextId the context id
	 * @param queryText the query text
	 * @return the json node
	 * @throws ExecutionException   the execution exception
	 * @throws InterruptedException the interrupted exception
	 * @throws TimeoutException     the timeout exception
	 * @throws EverSdkException     the ever sdk exception
	 */
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

	/**
	 * Instantiates a new Subscribe handle.
	 *
	 * @param contextId the context id
	 * @param queryText the query text
	 */
	public SubscribeHandle(int contextId, String queryText) {
		this.contextId = contextId;
		this.queryText = queryText;
	}

	/**
	 * Add event consumer subscribe handle.
	 *
	 * @param eventConsumer the event consumer
	 * @return the subscribe handle
	 */
	public SubscribeHandle addEventConsumer(Consumer<JsonNode> eventConsumer) {
		consumers.add(eventConsumer);
		return this;
	}

	/**
	 * Add consume filter subscribe handle.
	 *
	 * @param consumeFilter the consume filter
	 * @return the subscribe handle
	 */
	public SubscribeHandle addConsumeFilter(Predicate<JsonNode> consumeFilter) {
		this.consumeFilters.add(consumeFilter);
		return this;
	}

	/**
	 * Add unsubscribe filter subscribe handle.
	 *
	 * @param unsubscribeFilter the unsubscribe filter
	 * @return the subscribe handle
	 */
	public SubscribeHandle addUnsubscribeFilter(Predicate<JsonNode> unsubscribeFilter) {
		this.unsubscribeFilters.add(unsubscribeFilter);
		return this;
	}

	/**
	 * Subscribe subscribe handle.
	 *
	 * @return the subscribe handle
	 * @throws EverSdkException the ever sdk exception
	 */
	public SubscribeHandle subscribe() throws EverSdkException {

		Consumer<JsonNode> subscription = eventJson -> {
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
		};

		this.handle = EverSdk.await(Net.subscribe(this.contextId,
		                                    queryText(),
		                                    JsonContext.EMPTY_NODE(),
		                                    subscription
		)).handle();
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

	/**
	 * Unsubscribe.
	 */
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

	/**
	 * Context id int.
	 *
	 * @return the int
	 */
	public int contextId() {
		return contextId;
	}

	/**
	 * Query text string.
	 *
	 * @return the string
	 */
	public String queryText() {
		return queryText;
	}

	/**
	 * Handle long.
	 *
	 * @return the long
	 */
	public long handle() {
		return handle;
	}

	@Override
	public void close() {
		unsubscribe();
	}
}
