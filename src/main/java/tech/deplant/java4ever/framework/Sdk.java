package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;

public record Sdk(Context context,
                  Client.ClientConfig clientConfig/*,
                  ExplorerConfig explorerConfig,
                  EnvironmentConfig environmentConfig*/) {
}
