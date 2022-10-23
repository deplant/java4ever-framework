package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.io.IOException;
import java.util.Map;

public record Sdk(Context context,
                  long debugTreeTimeout,
                  Client.ClientConfig clientConfig,
                  ExplorerConfig explorerConfig,
                  EnvironmentConfig environmentConfig) {

	public String[] endpoints() {
		return clientConfig().network().endpoints();
	}

	public String version() throws EverSdkException {
		return Client.version(context()).version();
	}

	public <T> T convertMap(Map<String, Object> inputMap, Class<T> outputClass) {
		return context().mapper().convertValue(inputMap, outputClass);
	}

	public JsonNode parseStruct(Object struct) throws JsonProcessingException {
		return context().mapper().readTree(serialize(struct));
	}

	public <T> T deserialize(String inputString, Class<T> outputClass) throws JsonProcessingException {
		return context().mapper().readValue(inputString, outputClass);
	}

	public String serialize(Object inputObject) throws JsonProcessingException {
		return context().mapper().writeValueAsString(inputObject);
	}

	public void saveContract(String name, OwnedContract contract) throws IOException {
		explorerConfig().addContract(name, contract);
	}

	public void saveKeys(String name, Credentials keys) throws IOException {
		explorerConfig().credentials().put(name, keys);
		explorerConfig().sync();
	}

	public Tvm.ExecutionOptions executionOptions() {
//		return new Tvm.ExecutionOptions(blockchainConfig,
//		                                blockTime,
//		                                blockLt,
//		                                transactionLt,
//		                                false);
		return null;
	}
}
