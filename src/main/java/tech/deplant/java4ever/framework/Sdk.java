package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.framework.artifact.Solc;
import tech.deplant.java4ever.framework.artifact.TvmLinker;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Sdk(Context context,
                  long debugTreeTimeout,
                  Client.ClientConfig clientConfig,
                  ExplorerConfig explorerConfig,
                  EnvironmentConfig environmentConfig) {

	public static Builder builder() {
		return new Builder();
	}

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

	public static class Builder {

		private boolean cacheInLocalStorage = true;
		private String localStoragePath = "~/.tonclient";
		// JavaConfig
		private long timeout = 60_000L;
		private long debugTimeout = 60_000L;
		private ObjectMapper mapper = ContextBuilder.DEFAULT_MAPPER;
		//Context.NetworkConfig
		private String[] endpoints = new String[]{"https://localhost"};
		@Deprecated
		private String serverAddress; // deprecated, use endpoints
		private Integer networkRetriesCount = 5;
		private Integer messageRetriesCount = 5;
		private Integer messageProcessingTimeout = 40000;
		private Integer waitForTimeout = 40000;
		private Integer outOfSyncThreshold = 15000;
		private Integer reconnectTimeout = 12000;
		private String accessKey;
		//Context.CryptoConfig
		private Integer mnemonicDictionary = 1;
		private Integer mnemonicWordCount = 12;
		private String hdkeyDerivationPath = "m/44'/396'/0'/0/0";
		//Context.AbiConfig;
		private Integer workchain = 0;
		private Integer messageExpirationTimeout = 40000;
		private Integer messageExpirationTimeoutGrowFactor = null;
		private Integer cacheMaxSize = 10240;
		private Integer maxReconnectTimeout = 120000;
		private Integer sendingEndpointCount = 1;
		private Integer latencyDetectionInterval = 60000;
		private Integer maxLatency = 60000;
		private Integer queryTimeout = 60000;
		private Client.NetworkQueriesProtocol queriesProtocol = Client.NetworkQueriesProtocol.HTTP;
		private Integer firstRempStatusTimeout = 1000;
		private Integer nextRempStatusTimeout = 5000;

		private ExplorerConfig explorerConfig;

		private EnvironmentConfig environmentConfig;

		private String explorerConfigPath = System.getProperty("user.dir") + "/explorer.json";

		private String environmentConfigPath = System.getProperty("user.dir") + "/local.json";
		private String solidityCompilerPath = System.getProperty("user.dir");
		private String tvmlinkerPath = System.getProperty("user.dir");
		private String stdLibPath = System.getProperty("user.dir");
		private String soliditySourcesDefaultPath = System.getProperty("user.dir");
		private String solidityArtifactsBuildPath = System.getProperty("user.dir");

		public Builder() {
		}

		public Builder setSolidityCompilerPath(String solidityCompilerPath) {
			this.solidityCompilerPath = solidityCompilerPath;
			return this;
		}

		public Builder setTvmlinkerPath(String tvmlinkerPath) {
			this.tvmlinkerPath = tvmlinkerPath;
			return this;
		}

		public Builder setStdLibPath(String stdLibPath) {
			this.stdLibPath = stdLibPath;
			return this;
		}

		public Builder setSoliditySourcesDefaultPath(String soliditySourcesDefaultPath) {
			this.soliditySourcesDefaultPath = soliditySourcesDefaultPath;
			return this;
		}

		public Builder setSolidityArtifactsBuildPath(String solidityArtifactsBuildPath) {
			this.solidityArtifactsBuildPath = solidityArtifactsBuildPath;
			return this;
		}

		public Builder environmentConfig(EnvironmentConfig environmentConfig) {
			this.environmentConfig = environmentConfig;
			return this;
		}

		public Builder explorerConfig(ExplorerConfig explorerConfig) {
			this.explorerConfig = explorerConfig;
			return this;
		}

		public Builder proofsCacheInLocalStorage(boolean cacheInLocalStorage) {
			this.cacheInLocalStorage = cacheInLocalStorage;
			return this;
		}

		public Builder networkMaxReconnectTimeout(Integer maxReconnectTimeout) {
			this.maxReconnectTimeout = maxReconnectTimeout;
			return this;
		}

		public Builder networkSendingEndpointCount(Integer sendingEndpointCount) {
			this.sendingEndpointCount = sendingEndpointCount;
			return this;
		}

		public Builder networkLatencyDetectionInterval(Integer latencyDetectionInterval) {
			this.latencyDetectionInterval = latencyDetectionInterval;
			return this;
		}

		public Builder networkMaxLatency(Integer maxLatency) {
			this.maxLatency = maxLatency;
			return this;
		}

		public Builder networkQueryTimeout(Integer queryTimeout) {
			this.queryTimeout = queryTimeout;
			return this;
		}

		public Builder networkQueriesProtocol(Client.NetworkQueriesProtocol queriesProtocol) {
			this.queriesProtocol = queriesProtocol;
			return this;
		}

		public Builder networkFirstRempStatusTimeout(Integer firstRempStatusTimeout) {
			this.firstRempStatusTimeout = firstRempStatusTimeout;
			return this;
		}

		public Builder networkNextRempStatusTimeout(Integer nextRempStatusTimeout) {
			this.nextRempStatusTimeout = nextRempStatusTimeout;
			return this;
		}

		public Builder networkEndpoints(String... endpoints) {
			this.endpoints = endpoints;
			return this;
		}

		public Builder networkServerAddress(String server_address) {
			this.serverAddress = server_address;
			return this;
		}

		public Builder networkRetriesCount(Integer network_retries_count) {
			this.networkRetriesCount = network_retries_count;
			return this;
		}

		public Builder networkMessageRetriesCount(Integer message_retries_count) {
			this.messageRetriesCount = message_retries_count;
			return this;
		}

		public Builder networkMessageProcessingTimeout(Integer message_processing_timeout) {
			this.messageProcessingTimeout = message_processing_timeout;
			return this;
		}

		public Builder networkWaitForTimeout(Integer wait_for_timeout) {
			this.waitForTimeout = wait_for_timeout;
			return this;
		}

		public Builder networkOutOfSyncThreshold(Integer out_of_sync_threshold) {
			this.outOfSyncThreshold = out_of_sync_threshold;
			return this;
		}

		public Builder networkReconnectTimeout(Integer reconnect_timeout) {
			this.reconnectTimeout = reconnect_timeout;
			return this;
		}

		public Builder networkAccessKey(String access_key) {
			this.accessKey = access_key;
			return this;
		}

		//cripto
		public Builder cryptoMnemonicDictionary(Integer mnemonic_dictionary) {
			this.mnemonicDictionary = mnemonic_dictionary;
			return this;
		}

		public Builder cryptoMnemonicWordCount(Integer mnemonic_word_count) {
			this.mnemonicWordCount = mnemonic_word_count;
			return this;
		}

		public Builder cryptoHdkeyDerivationPath(String hdkey_derivation_path) {
			this.hdkeyDerivationPath = hdkey_derivation_path;
			return this;
		}

		//abi
		public Builder abiWorkchain(Integer workchain) {
			this.workchain = workchain;
			return this;
		}

		public Builder abiMessageExpirationTimeout(Integer message_expiration_timeout) {
			this.messageExpirationTimeout = message_expiration_timeout;
			return this;
		}

		public Builder abiMessageExpirationTimeoutGrowFactor(Integer message_expiration_timeout_grow_factor) {
			this.messageExpirationTimeoutGrowFactor = message_expiration_timeout_grow_factor;
			return this;
		}

		public Builder timeout(long timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder mapper(ObjectMapper mapper) {
			this.mapper = mapper;
			return this;
		}

		public Sdk build(LibraryLoader loader) throws IOException {
			var config = new Client.ClientConfig(
					new Client.NetworkConfig(
							this.serverAddress,
							this.endpoints,
							this.networkRetriesCount,
							this.maxReconnectTimeout,
							this.reconnectTimeout,
							this.messageRetriesCount,
							this.messageProcessingTimeout,
							this.waitForTimeout,
							this.outOfSyncThreshold,
							this.sendingEndpointCount,
							this.latencyDetectionInterval,
							this.maxLatency,
							this.queryTimeout,
							this.queriesProtocol,
							this.firstRempStatusTimeout,
							this.nextRempStatusTimeout,
							this.accessKey),
					new Client.CryptoConfig(
							this.mnemonicDictionary,
							this.mnemonicWordCount,
							this.hdkeyDerivationPath),
					new Client.AbiConfig(
							this.workchain,
							this.messageExpirationTimeout,
							this.messageExpirationTimeoutGrowFactor),
					new Client.BocConfig(this.cacheMaxSize),
					new Client.ProofsConfig(this.cacheInLocalStorage),
					this.localStoragePath
			);
			var explorerConfig =
					this.explorerConfig == null ? ExplorerConfig.EMPTY(this.explorerConfigPath) : this.explorerConfig;
			var envConfig = this.environmentConfig == null ? new EnvironmentConfig(
					this.environmentConfigPath,
					new Solc(this.solidityCompilerPath),
					new TvmLinker(this.tvmlinkerPath, this.stdLibPath),
					this.soliditySourcesDefaultPath,
					this.solidityArtifactsBuildPath,
					new ConcurrentHashMap<String, String>(),
					new ConcurrentHashMap<String, String>(),
					new ConcurrentHashMap<String, String>()
			) : this.environmentConfig;
			return new Sdk(
					new ContextBuilder()
							.setConfigJson(this.mapper.writeValueAsString(config))
							.setTimeout(this.timeout)
							.setMapper(this.mapper)
							.buildNew(loader),
					this.debugTimeout, config, explorerConfig, envConfig
			);
		}

		public Sdk load(int contextId, int contextRequestCount) throws EverSdkException, IOException {
			var context = new ContextBuilder()
					.setTimeout(this.timeout)
					.setMapper(this.mapper)
					.buildFromExisting(contextId, contextRequestCount);
			Client.ClientConfig config = null;
			var explorerConfig =
					this.explorerConfig == null ? ExplorerConfig.EMPTY(this.explorerConfigPath) : this.explorerConfig;
			var envConfig = this.environmentConfig == null ? new EnvironmentConfig(
					this.environmentConfigPath,
					new Solc(this.solidityCompilerPath),
					new TvmLinker(this.tvmlinkerPath, this.stdLibPath),
					this.soliditySourcesDefaultPath,
					this.solidityArtifactsBuildPath,
					new ConcurrentHashMap<String, String>(),
					new ConcurrentHashMap<String, String>(),
					new ConcurrentHashMap<String, String>()
			) : this.environmentConfig;
			config = Client.config(context);
			return new Sdk(context, this.debugTimeout, config, explorerConfig, envConfig);
		}

		public Builder bocCacheMaxSize(Integer cacheMaxSize) {
			this.cacheMaxSize = cacheMaxSize;
			return this;
		}

		public Builder explorerConfigPath(String explorerConfigPath) {
			this.explorerConfigPath = explorerConfigPath;
			return this;
		}

		public Builder environmentConfigPath(String environmentConfigPath) {
			this.environmentConfigPath = environmentConfigPath;
			return this;
		}
	}
}
