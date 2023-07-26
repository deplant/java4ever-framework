package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.framework.contract.AbstractContract;

import java.io.IOException;
import java.util.Map;

public record Sdk(EverSdkContext context,
                  long debugTreeTimeout,
                  Client.ClientConfig clientConfig,
                  OnchainConfig onchainConfig,
                  LocalConfig localConfig) {

	public static Sdk DEFAULT() throws IOException {
		return Sdk.builder().build();
	}

	public static Sdk DEFAULT(String endpoint) throws IOException {
		return Sdk.builder().networkEndpoints(endpoint).build();
	}

	public static Builder builder() {
		return new Builder();
	}

	public String[] endpoints() {
		return clientConfig().network().endpoints();
	}

	public String version() throws EverSdkException {
		return Client.version(context()).version();
	}

	public ObjectMapper mapper() {
		return context().mapper();
	}

	public <T> T convertMap(Map<String, Object> inputMap, Class<T> outputClass) {
		return context().mapper().convertValue(inputMap, outputClass);
	}

	public <T> T convertMap(Map<String, Object> inputMap, TypeReference<T> outputType) {
		return context().mapper().convertValue(inputMap, outputType);
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

	public void saveContract(String name, AbstractContract contract) throws IOException {
		onchainConfig().addContract(name, contract);
	}

	public void saveKeys(String name, Credentials keys) throws IOException {
		onchainConfig().addKeys(name, keys);
		onchainConfig().sync();
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
		private Integer timeout = 60_000;
		private Integer debugTimeout = 60_000;

		private ObjectMapper mapper = JsonContext.SDK_JSON_MAPPER();
		//Context.NetworkConfig
		private String[] endpoints = new String[]{"https://localhost"};
		@Deprecated private String serverAddress; // deprecated, use endpoints
		private Integer networkRetriesCount = 5;
		private Integer messageRetriesCount = 5;
		private Long messageProcessingTimeout = 40000L;
		private Long waitForTimeout = 40000L;
		private Long outOfSyncThreshold = 15000L;
		private Long reconnectTimeout = 12000L;
		private String accessKey;
		//Context.CryptoConfig
		private Crypto.MnemonicDictionary mnemonicDictionary = Crypto.MnemonicDictionary.English;
		private Integer mnemonicWordCount = 12;
		private String hdkeyDerivationPath = "m/44'/396'/0'/0/0";
		//Context.AbiConfig;
		private Long workchain = 0L;
		private Long messageExpirationTimeout = 40000L;
		private Long messageExpirationTimeoutGrowFactor = null;
		private Long cacheMaxSize = 10240L;
		private Long maxReconnectTimeout = 120000L;
		private Integer sendingEndpointCount = 1;
		private Long latencyDetectionInterval = 60000L;
		private Long maxLatency = 60000L;
		private Long queryTimeout = 60000L;
		private Client.NetworkQueriesProtocol queriesProtocol = Client.NetworkQueriesProtocol.HTTP;
		private Long firstRempStatusTimeout = 1000L;
		private Long nextRempStatusTimeout = 5000L;

		private Long signatureId = null;

		private OnchainConfig onchainConfig;

		private LocalConfig localConfig;

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

		public Builder environmentConfig(LocalConfig localConfig) {
			this.localConfig = localConfig;
			return this;
		}

		public Builder explorerConfig(OnchainConfig onchainConfig) {
			this.onchainConfig = onchainConfig;
			return this;
		}

		public Builder proofsCacheInLocalStorage(boolean cacheInLocalStorage) {
			this.cacheInLocalStorage = cacheInLocalStorage;
			return this;
		}

		public Builder networkMaxReconnectTimeout(Long maxReconnectTimeout) {
			this.maxReconnectTimeout = maxReconnectTimeout;
			return this;
		}

		public Builder networkSendingEndpointCount(Integer sendingEndpointCount) {
			this.sendingEndpointCount = sendingEndpointCount;
			return this;
		}

		public Builder networkLatencyDetectionInterval(Long latencyDetectionInterval) {
			this.latencyDetectionInterval = latencyDetectionInterval;
			return this;
		}

		public Builder networkMaxLatency(Long maxLatency) {
			this.maxLatency = maxLatency;
			return this;
		}

		public Builder networkQueryTimeout(Long queryTimeout) {
			this.queryTimeout = queryTimeout;
			return this;
		}

		public Builder networkQueriesProtocol(Client.NetworkQueriesProtocol queriesProtocol) {
			this.queriesProtocol = queriesProtocol;
			return this;
		}

		public Builder networkFirstRempStatusTimeout(Long firstRempStatusTimeout) {
			this.firstRempStatusTimeout = firstRempStatusTimeout;
			return this;
		}

		public Builder networkNextRempStatusTimeout(Long nextRempStatusTimeout) {
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

		public Builder networkMessageProcessingTimeout(Long message_processing_timeout) {
			this.messageProcessingTimeout = message_processing_timeout;
			return this;
		}

		public Builder networkWaitForTimeout(Long wait_for_timeout) {
			this.waitForTimeout = wait_for_timeout;
			return this;
		}

		public Builder networkOutOfSyncThreshold(Long out_of_sync_threshold) {
			this.outOfSyncThreshold = out_of_sync_threshold;
			return this;
		}

		public Builder networkReconnectTimeout(Long reconnect_timeout) {
			this.reconnectTimeout = reconnect_timeout;
			return this;
		}

		public Builder networkSignatureId(Long signatureId) {
			this.signatureId = signatureId;
			return this;
		}

		public Builder networkAccessKey(String access_key) {
			this.accessKey = access_key;
			return this;
		}

		//cripto
		public Builder cryptoMnemonicDictionary(Crypto.MnemonicDictionary mnemonic_dictionary) {
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
		public Builder abiWorkchain(Long workchain) {
			this.workchain = workchain;
			return this;
		}

		public Builder abiMessageExpirationTimeout(Long message_expiration_timeout) {
			this.messageExpirationTimeout = message_expiration_timeout;
			return this;
		}

		public Builder abiMessageExpirationTimeoutGrowFactor(Long message_expiration_timeout_grow_factor) {
			this.messageExpirationTimeoutGrowFactor = message_expiration_timeout_grow_factor;
			return this;
		}

		public Builder timeout(Integer timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder mapper(ObjectMapper mapper) {
			this.mapper = mapper;
			return this;
		}

		public Sdk build() throws IOException {
			return build(new DefaultLoader(this.getClass().getClassLoader()));
		}

		public Sdk build(LibraryLoader loader) throws IOException {
			var config = new Client.ClientConfig(new Client.BindingConfig("java4ever", "2.2.0"),
			                                     new Client.NetworkConfig(this.serverAddress,
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
			                                                              this.signatureId,
			                                                              this.accessKey),
			                                     new Client.CryptoConfig(this.mnemonicDictionary,
			                                                             this.mnemonicWordCount,
			                                                             this.hdkeyDerivationPath),
			                                     new Client.AbiConfig(this.workchain,
			                                                          this.messageExpirationTimeout,
			                                                          this.messageExpirationTimeoutGrowFactor),
			                                     new Client.BocConfig(this.cacheMaxSize),
			                                     new Client.ProofsConfig(this.cacheInLocalStorage),
			                                     this.localStoragePath);
			var explorerConfig =
					this.onchainConfig == null ? OnchainConfig.EMPTY(this.explorerConfigPath) : this.onchainConfig;
			var envConfig = this.localConfig == null ? LocalConfig.EMPTY(this.environmentConfigPath,
			                                                             this.solidityCompilerPath,
			                                                             this.tvmlinkerPath,
			                                                             this.stdLibPath,
			                                                             this.soliditySourcesDefaultPath,
			                                                             this.solidityArtifactsBuildPath) : this.localConfig;
			return new Sdk(EverSdkContext.builder()
			                             .setConfigJson(this.mapper.writeValueAsString(config))
			                             .setTimeout(this.timeout)
			                             .setMapper(this.mapper)
			                             .buildNew(loader), this.debugTimeout, config, explorerConfig, envConfig);
		}

		public Sdk load(int contextId, int contextRequestCount) throws EverSdkException, IOException {
			var context = EverSdkContext.builder()
			                            .setTimeout(this.timeout)
			                            .setMapper(this.mapper)
			                            .buildFromExisting(contextId, contextRequestCount);
			Client.ClientConfig config = null;
			var explorerConfig =
					this.onchainConfig == null ? OnchainConfig.EMPTY(this.explorerConfigPath) : this.onchainConfig;
			var envConfig = this.localConfig == null ? LocalConfig.EMPTY(this.environmentConfigPath,
			                                                             this.solidityCompilerPath,
			                                                             this.tvmlinkerPath,
			                                                             this.stdLibPath,
			                                                             this.soliditySourcesDefaultPath,
			                                                             this.solidityArtifactsBuildPath) : this.localConfig;
			config = Client.config(context);
			return new Sdk(context, this.debugTimeout, config, explorerConfig, envConfig);
		}

		public Builder bocCacheMaxSize(Long cacheMaxSize) {
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
