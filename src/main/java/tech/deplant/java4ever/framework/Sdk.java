package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.framework.artifact.EnvironmentConfig;

public record Sdk(Context context,
                  long debugTreeTimeout,
                  Client.ClientConfig clientConfig,
                  ExplorerConfig explorerConfig,
                  EnvironmentConfig environmentConfig) {

	public ObjectMapper mapper() {
		return context().mapper();
	}
}
