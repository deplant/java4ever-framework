package tech.deplant.java4ever.framework.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SubscriptionHandle implements Subscriptions {

	private static System.Logger logger = System.getLogger(SubscriptionHandle.class.getName());
	private final int contextId;
	private final String queryText;
	private final boolean hasUnsubsribeCondition;

	public boolean hasUnsubsribeCondition() {
		return hasUnsubsribeCondition;
	}

	public Predicate<JsonNode> unsubscribeCondition() {
		return unsubscribeCondition;
	}

	public Set<Consumer<JsonNode>> consumers() {
		return consumers;
	}

	public String queryText() {
		return queryText;
	}

	private final Predicate<JsonNode> unsubscribeCondition;
	private final Set<Predicate<JsonNode>> consumeFilters;
	private final Set<Consumer<JsonNode>> consumers;
	protected long handle;
	private final boolean isCallbackToQueue;
	private final Deque<JsonNode> callbackQueue = new ConcurrentLinkedDeque<>();

	protected SubscriptionHandle(int contextId,
	                             String queryText,
	                             boolean hasUnsubsribeCondition,
	                             Predicate<JsonNode> unsubscribeCondition,
	                             Set<Predicate<JsonNode>> consumeFilters,
	                             Set<Consumer<JsonNode>> consumers,
	                             boolean isCallbackToQueue) {
		this.contextId = contextId;
		this.queryText = queryText;
		this.hasUnsubsribeCondition = hasUnsubsribeCondition;
		this.unsubscribeCondition = unsubscribeCondition;
		this.consumeFilters = consumeFilters;
		this.consumers = consumers;
		this.isCallbackToQueue = isCallbackToQueue;

	}

	@Override
	public int contextId() {
		return this.contextId;
	}

	@Override
	public long handle() {
		return this.handle;
	}

	@Override
	public boolean isCallbackToQueue() {
		return this.isCallbackToQueue;
	}

	@Override
	public Deque<JsonNode> callbackQueue() {
		return this.callbackQueue;
	}

	public boolean testConsumeFilters(JsonNode jsonNode) {
		return this.consumeFilters.isEmpty() || this.consumeFilters.stream().allMatch(flt -> flt.test(jsonNode));
	}

	public void broadcastToConsumers(JsonNode jsonNode) {
		this.consumers.forEach(consumer -> consumer.accept(jsonNode));
	}

	/**
	 * Unsubscribe.
	 */
	public void unsubscribe() {
		try {
			logger.log(System.Logger.Level.TRACE, () -> "HANDLE:%d Unsubscribing...".formatted(this.handle));
			Net.unsubscribe(this.contextId, new Net.ResultOfSubscribeCollection(handle()));
		} catch (EverSdkException e) {
			// I think there's no reason to fail everything if unsubscribe failed...
			logger.log(System.Logger.Level.ERROR, () -> "HANDLE:%d Unsubscribe failed!".formatted(this.handle));
		}

	}
}
