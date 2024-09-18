package tech.deplant.java4ever.framework.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Subscriptions {

	static Builder onAccounts(String... outputFields) {
		return new Builder(SubscriptionType.ACCOUNTS, outputFields);
	}

	static Builder onMessages(String... outputFields) {
		return new Builder(SubscriptionType.MESSAGES, outputFields);
	}

	static Builder onTransactions(String... outputFields) {
		return new Builder(SubscriptionType.TRANSACTIONS, outputFields);
	}

	static Builder onBlocks(String... outputFields) {
		return new Builder(SubscriptionType.BLOCKS, outputFields);
	}

	static Builder onBlocksSignatures(String... outputFields) {
		return new Builder(SubscriptionType.BLOCKS_SIGNATURES, outputFields);
	}

	static Builder onZeroStates(String... outputFields) {
		return new Builder(SubscriptionType.ZEROSTATES, outputFields);
	}

	static Builder onCounterparties(String... outputFields) {
		return new Builder(SubscriptionType.COUNTERPARTIES, outputFields);
	}

	static Builder onRempReceipts(String... outputFields) {
		return new Builder(SubscriptionType.REMP_RECEIMPTS, outputFields);
	}

	int contextId();

	long handle();

	boolean isCallbackToQueue();

	Deque<JsonNode> callbackQueue();

	enum SubscriptionType {
		ACCOUNTS,
		TRANSACTIONS,
		MESSAGES,
		BLOCKS,
		BLOCKS_SIGNATURES,
		ZEROSTATES,
		COUNTERPARTIES,
		REMP_RECEIMPTS
	}

	class Builder {

		private final Set<Predicate<JsonNode>> consumeFilters = new HashSet<>();
		private final Set<Consumer<JsonNode>> consumers = new HashSet<>();
		private String queryText;
		private boolean hasUnsubsribeCondition = false;
		private Predicate<JsonNode> unsubscribeCondition;
		private long handle;
		private boolean isCallbackToQueue = false;
		private boolean hasGqlFilter = false;
		private Set<String> gqlFilters = new HashSet<>();

		private Builder(SubscriptionType type, String... outputFields) {
			String text = """
					subscription {
								<TYPE> (
								   filter: { <FILTER> }
					                ) {
					              <OUTPUT>
								}
							}
					""";
			switch (type) {
				case ACCOUNTS -> text = text.replace("<TYPE>", "accounts");
				case TRANSACTIONS -> text = text.replace("<TYPE>", "transactions");
				case MESSAGES -> text = text.replace("<TYPE>", "messages");
				case BLOCKS -> text = text.replace("<TYPE>", "blocks");
				case BLOCKS_SIGNATURES -> text = text.replace("<TYPE>", "blocks_signatures");
				case ZEROSTATES -> text = text.replace("<TYPE>", "zerostates");
				case COUNTERPARTIES -> text = text.replace("<TYPE>", "counterparties");
				case REMP_RECEIMPTS -> text = text.replace("<TYPE>", "rempReceipts");
			}
			text = text.replace("<OUTPUT>", String.join(" ", outputFields));
			this.queryText = text;
		}

		private Builder() {

		}

		/**
		 * If set, receives events that pass filters will be immediately consumed with provided Consumer interface
		 *
		 * @param callbackConsumer consumer lambda function that handles new JsonNode callback
		 **/
		public Builder addCallbackConsumer(Consumer<JsonNode> callbackConsumer) {
			consumers.add(callbackConsumer);
			return this;
		}

		/**
		 * If set, received events that pass filters will be consumed by internal queue object.
		 * You can later retrieve them by calling SubscribeHandle::queue().
		 **/
		public Builder setCallbackToQueue(boolean doCallbackToQueue) {
			this.isCallbackToQueue = doCallbackToQueue;
			return this;
		}

		/**
		 * If Gql filter is set, event will be received by graphql client only if does meet provided filter requirements.
		 * Gql filter is limited to GraphQL api filters of your endpoint.
		 *
		 * @param gqlFilter String representation of GraphQL filter query
		 **/
		public Builder addFilterOnSubscription(String gqlFilter) {
			this.hasGqlFilter = true;
			this.gqlFilters.add(gqlFilter);
			return this;
		}

		/**
		 * If Java filter is set, event will be received, but will not be consumed until predicate is true.
		 * This filter is not limited by GraphQL endpoint possible filters, but it can take more network bandwidth as it receives all events.
		 * It's better to use GraphQL Server filter for most tasks and add Java client-side filter for more complex conditions.
		 * You can add any number of filters.
		 **/
		public Builder addFilterOnConsume(Predicate<JsonNode> javaFilter) {
			this.consumeFilters.add(javaFilter);
			return this;
		}

		private String constructQuery() {
			String finalQuery = this.queryText.replace("<FILTER>", String.join(" ", gqlFilters.toArray(String[]::new)));
			finalQuery = finalQuery.replace("<OUTPUT>", ""); // if no output fields
			return finalQuery;
		}

		/**
		 * Subscribe subscribe handle.
		 *
		 * @return the subscribe handle
		 * @throws EverSdkException the ever sdk exception
		 */
		private SubscriptionHandle subscribe(int contextId) throws EverSdkException {

			final var subscription = new SubscriptionHandle(contextId,
			                                                constructQuery(),
			                                                this.hasUnsubsribeCondition,
			                                                this.unsubscribeCondition,
			                                                this.consumeFilters,
			                                                this.consumers,
			                                                this.isCallbackToQueue);

			Consumer<JsonNode> rootConsumer = eventJson -> {
				if (subscription.testConsumeFilters(eventJson)) {
					if (subscription.hasUnsubsribeCondition()) {
						if (subscription.unsubscribeCondition().test(eventJson)) {
							subscription.unsubscribe();
						}
					}
					subscription.broadcastToConsumers(eventJson);
					if (subscription.isCallbackToQueue()) {
						subscription.callbackQueue().add(eventJson);
					}
				}
			};
			subscription.handle = EverSdk.await(Net.subscribe(contextId,
			                                                  subscription.queryText(),
			                                                  JsonContext.EMPTY_NODE(),
			                                                  rootConsumer)).handle();
			return subscription;
		}

		public SubscriptionHandle subscribeUntilCancel(int contextId) throws EverSdkException {
			this.hasUnsubsribeCondition = false;
			return subscribe(contextId);
		}

		public SubscriptionHandle subscribeUntilFirst(int contextId) throws EverSdkException {
			this.hasUnsubsribeCondition = true;
			this.unsubscribeCondition = _ -> true;
			return subscribe(contextId);
		}

		public SubscriptionHandle subscribeUntilCondition(int contextId,
		                                             Predicate<JsonNode> unsubscribeCondition) throws EverSdkException {
			this.hasUnsubsribeCondition = true;
			this.unsubscribeCondition = unsubscribeCondition;
			return subscribe(contextId);
		}

	}
}
