package tech.deplant.java4ever.gen;

import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.reference.ApiReference;

import java.io.IOException;

public class EverSdkGenerator {
	public static void main(String[] args) {
		ApiReference apiReference = null;
		try {
			apiReference = ParserEngine.ofJsonResource("api.json");
			ParserEngine.parse(apiReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}