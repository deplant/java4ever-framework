package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AppPasswordProvider implements AppObject {

	public abstract Crypto.ResultOfAppPasswordProvider.GetPassword getPassword(String encryptionPublicKey);

	@Override
	public final void consumeParams(int contextId, long appRequestId, JsonNode jsonNode) throws EverSdkException {
		try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
			exec.submit(() -> {
				try {
					String requestMethod = jsonNode.get("type").asText();
					if ("GetPassword".equals(requestMethod)) {
						Crypto.ResultOfAppPasswordProvider.GetPassword password = getPassword(jsonNode.get(
								"encryptionPublicKey").asText());
						var resultNode = JsonContext.SDK_JSON_MAPPER()
						                            .valueToTree(password);
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Ok(resultNode));
					} else {
						throw new IllegalStateException("Unexpected value: " + jsonNode.get("type").asText());
					}
				} catch (Exception e) {
					try {
						Client.resolveAppRequest(contextId,
						                         appRequestId,
						                         new Client.AppRequestResult.Error(e.getMessage()));
					} catch (EverSdkException ex) {
						throw new RuntimeException(ex);
					}
				}
			});
			exec.shutdown();
			if (!exec.awaitTermination(60, TimeUnit.SECONDS)) {
				Client.resolveAppRequest(contextId,
				                         appRequestId,
				                         new Client.AppRequestResult.Error("Unsuccessful completion"));
			}
		} catch (Exception e) {
			Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Error(e.getMessage()));
		}
	}
}
