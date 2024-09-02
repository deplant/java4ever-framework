package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AppEncryptionBox implements AppObject {

	@Override
	public final void consumeParams(int contextId, long appRequestId, JsonNode jsonNode) throws EverSdkException {
		try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
			exec.submit(() -> {
				try {
					String requestMethod = jsonNode.get("type").asText();
					if ("GetInfo".equals(requestMethod)) {
						Crypto.EncryptionBoxInfo encryptionBox = getInfo();
						var resultNode = JsonContext.SDK_JSON_MAPPER().valueToTree(new Crypto.ResultOfAppEncryptionBox.GetInfo(encryptionBox));
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Ok(resultNode));
					} else if ("Encrypt".equals(requestMethod)) {
						String encrypted = encrypt(jsonNode.get("data").asText());
						var resultNode = JsonContext.SDK_JSON_MAPPER().valueToTree(new Crypto.ResultOfAppEncryptionBox.Encrypt(encrypted));
						Client.resolveAppRequest(contextId, appRequestId, new Client.AppRequestResult.Ok(resultNode));
					} else if ("Decrypt".equals(requestMethod)) {
						String decrypted = decrypt(jsonNode.get("data").asText());
						var resultNode = JsonContext.SDK_JSON_MAPPER().valueToTree(new Crypto.ResultOfAppEncryptionBox.Decrypt(decrypted));
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

	public abstract Crypto.EncryptionBoxInfo getInfo();

	public abstract String encrypt(String data);

	public abstract String decrypt(String data);
}
