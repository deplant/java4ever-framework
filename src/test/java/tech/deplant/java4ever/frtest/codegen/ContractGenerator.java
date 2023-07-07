package tech.deplant.java4ever.frtest.codegen;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.generator.ContractWrapper;

import java.io.IOException;

public class ContractGenerator {
	public static void main(String[] args) {
		try {
			ContractWrapper.generateFromConfig("codegen/generator-config.json");
		} catch (IOException | EverSdkException e) {
			throw new RuntimeException(e);
		}
	}

}
