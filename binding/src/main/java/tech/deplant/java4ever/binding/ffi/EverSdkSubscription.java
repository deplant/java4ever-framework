package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.JsonContext;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class EverSdkSubscription {

	private final static System.Logger logger = System.getLogger(EverSdkSubscription.class.getName());
	private final Consumer<JsonNode> eventAction;

	private int handle = 0;

	public EverSdkSubscription(Consumer<JsonNode> eventAction) {
		this.eventAction = eventAction;
	}

	public int handle() {
		return this.handle;
	}

	public EverSdkSubscription setHandle(int handle) {
		this.handle = handle;
		return this;
	}

	void acceptEvent(int requestId, String responseString) {
		try {
			JsonNode node = JsonContext.ABI_JSON_MAPPER().readTree(responseString);
			try {
				this.eventAction.accept(node);
			} catch (Exception ex1) {
				logger.log(System.Logger.Level.ERROR,
				           () -> "REQ:%d EVENT:%s Subscribe Event Action processing failed! %s".formatted(requestId, responseString, ex1.toString()));
			}
		} catch (JsonProcessingException ex2) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "REQ:%d EVENT:%s Subscribe Event JSON deserialization failed! %s".formatted(requestId, responseString, ex2.toString()));
		}

	}
}
