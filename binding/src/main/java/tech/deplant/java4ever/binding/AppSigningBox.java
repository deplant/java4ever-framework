package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static tech.deplant.java4ever.binding.JsonContext.SDK_JSON_MAPPER;

public abstract class AppSigningBox implements AppObject {

	@Override
	public final void consumeParams(int contextId, long appRequestId, JsonNode jsonNode) throws EverSdkException {
		try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
			exec.submit(() -> {
				try {
					String requestMethod = jsonNode.get("type").asText();
					if ("Sign".equals(requestMethod)) {
						String signature = sign(jsonNode.get("unsigned").asText());
						var resultNode = JsonContext.SDK_JSON_MAPPER()
						                            .valueToTree(new Crypto.ResultOfAppSigningBox.Sign(signature));
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Ok(resultNode));
					} else if ("GetPublicKey".equals(requestMethod)) {
						String pk = getPublicKey();
						var resultNode = JsonContext.SDK_JSON_MAPPER()
						                            .valueToTree(new Crypto.ResultOfAppSigningBox.GetPublicKey(pk));
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Ok(resultNode));
					} else {
						throw new IllegalStateException("Unexpected value: " + jsonNode.get("type").asText());
					}
				} catch(Exception e) {
					try {
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Error(e.getMessage()));
					} catch (EverSdkException ex) {
						throw new RuntimeException(ex);
					}
				}
			});
			exec.shutdown();
			if (!exec.awaitTermination(60, TimeUnit.SECONDS)) {
				Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Error("Unsuccessful completion"));
			}
		} catch (Exception e) {
			Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Error(e.getMessage()));
		}
	}

	public abstract String getPublicKey();

	public abstract String sign(String unsigned);
}
