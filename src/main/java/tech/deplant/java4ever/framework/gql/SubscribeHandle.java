package tech.deplant.java4ever.framework.gql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.SubscribeEvent;
import tech.deplant.java4ever.framework.Sdk;

import java.util.Map;
import java.util.function.Consumer;

public record SubscribeHandle(Sdk sdk, long handle) {

	private static System.Logger logger = System.getLogger(SubscribeHandle.class.getName());

	public static SubscribeHandle subscribe(Sdk sdk, String queryText, Consumer<SubscribeEvent> subscribeEventConsumer) throws EverSdkException {
		var result = Net.subscribe(sdk.context(),
		              queryText,
		              JsonContext.EMPTY_NODE(),
		              handler -> {
			              try {
				              subscribeEventConsumer.accept(new SubscribeEvent(JsonContext.ABI_JSON_MAPPER().readTree(handler.params())));
			              } catch (JsonProcessingException e) {
				              logger.log(System.Logger.Level.WARNING, e::getMessage);
				              subscribeEventConsumer.accept(new SubscribeEvent(null));
			              }
		              });
		return new SubscribeHandle(sdk, result.handle());
	}

	public void unsubscribe() throws EverSdkException {
		Net.unsubscribe(sdk.context(),new Net.ResultOfSubscribeCollection(handle()));
	}

}
