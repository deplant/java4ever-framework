package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.io.IOException;

public record Sdk(Context context,
                  long debugTreeTimeout,
                  Client.ClientConfig clientConfig,
                  ExplorerConfig explorerConfig,
                  EnvironmentConfig environmentConfig) {

	public ObjectMapper mapper() {
		return context().mapper();
	}

	public void saveContract(String name, OwnedContract contract) throws IOException {
		explorerConfig().addContract(name, contract);
	}

	public void saveKeys(String name, Credentials keys) throws IOException {
		explorerConfig().credentials().put(name, keys);
		explorerConfig().sync();
	}

}
