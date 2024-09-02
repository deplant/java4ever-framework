package tech.deplant.java4ever.gen;

import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.reference.ApiReference;
import tech.deplant.java4ever.binding.io.JsonFile;
import tech.deplant.java4ever.binding.io.JsonResource;

import java.io.IOException;

public class EverSdkGenerator {
	public static void main(String[] args) {
		ApiReference apiReference = null;
		try {
			EverSdk.load();
			int contextId = EverSdk.createDefault();
			var jsonFile = new JsonFile(System.getProperty("user.dir") + "/" + "api.json");
			jsonFile.accept(EverSdk.await(Client.getApiReference(contextId)).api().toPrettyString());
			apiReference = ParserEngine.ofJsonString(jsonFile.get());
			ParserEngine.parse(apiReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (EverSdkException e) {
			throw new RuntimeException(e);
		}
	}
}