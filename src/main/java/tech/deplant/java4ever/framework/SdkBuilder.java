package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

public class SdkBuilder {

    private boolean cacheInLocalStorage = true;
    private String localStoragePath = "~/.tonclient";
    // JavaConfig
    private long timeout = 30L;
    private ObjectMapper mapper = Sdk.DEFAULT_MAPPER;
    //Context.NetworkConfig
    private String[] endpoints = Sdk.DEFAULT_ENDPOINTS;
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

    public SdkBuilder() {
    }

    public void proofsCacheInLocalStorage(boolean cacheInLocalStorage) {
        this.cacheInLocalStorage = cacheInLocalStorage;
    }

    public void networkMaxReconnectTimeout(Integer maxReconnectTimeout) {
        this.maxReconnectTimeout = maxReconnectTimeout;
    }

    public void networkSendingEndpointCount(Integer sendingEndpointCount) {
        this.sendingEndpointCount = sendingEndpointCount;
    }

    public void networkLatencyDetectionInterval(Integer latencyDetectionInterval) {
        this.latencyDetectionInterval = latencyDetectionInterval;
    }

    public void networkMaxLatency(Integer maxLatency) {
        this.maxLatency = maxLatency;
    }

    public void networkQueryTimeout(Integer queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public void networkQueriesProtocol(Client.NetworkQueriesProtocol queriesProtocol) {
        this.queriesProtocol = queriesProtocol;
    }

    public void networkFirstRempStatusTimeout(Integer firstRempStatusTimeout) {
        this.firstRempStatusTimeout = firstRempStatusTimeout;
    }

    public void networkNextRempStatusTimeout(Integer nextRempStatusTimeout) {
        this.nextRempStatusTimeout = nextRempStatusTimeout;
    }

    public SdkBuilder networkEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public SdkBuilder networkServerAddress(String server_address) {
        this.serverAddress = server_address;
        return this;
    }

    public SdkBuilder networkRetriesCount(Integer network_retries_count) {
        this.networkRetriesCount = network_retries_count;
        return this;
    }

    public SdkBuilder networkMessageRetriesCount(Integer message_retries_count) {
        this.messageRetriesCount = message_retries_count;
        return this;
    }

    public SdkBuilder networkMessageProcessingTimeout(Integer message_processing_timeout) {
        this.messageProcessingTimeout = message_processing_timeout;
        return this;
    }

    public SdkBuilder networkWaitForTimeout(Integer wait_for_timeout) {
        this.waitForTimeout = wait_for_timeout;
        return this;
    }

    public SdkBuilder networkOutOfSyncThreshold(Integer out_of_sync_threshold) {
        this.outOfSyncThreshold = out_of_sync_threshold;
        return this;
    }

    public SdkBuilder networkReconnectTimeout(Integer reconnect_timeout) {
        this.reconnectTimeout = reconnect_timeout;
        return this;
    }

    public SdkBuilder networkAccessKey(String access_key) {
        this.accessKey = access_key;
        return this;
    }

    //cripto
    public SdkBuilder cryptoMnemonicDictionary(Integer mnemonic_dictionary) {
        this.mnemonicDictionary = mnemonic_dictionary;
        return this;
    }

    public SdkBuilder cryptoMnemonicWordCount(Integer mnemonic_word_count) {
        this.mnemonicWordCount = mnemonic_word_count;
        return this;
    }

    public SdkBuilder cryptoHdkeyDerivationPath(String hdkey_derivation_path) {
        this.hdkeyDerivationPath = hdkey_derivation_path;
        return this;
    }

    //abi
    public SdkBuilder abiWorkchain(Integer workchain) {
        this.workchain = workchain;
        return this;
    }

    public SdkBuilder abiMessageExpirationTimeout(Integer message_expiration_timeout) {
        this.messageExpirationTimeout = message_expiration_timeout;
        return this;
    }

    public SdkBuilder abiMessageExpirationTimeoutGrowFactor(Integer message_expiration_timeout_grow_factor) {
        this.messageExpirationTimeoutGrowFactor = message_expiration_timeout_grow_factor;
        return this;
    }

    public SdkBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public SdkBuilder mapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public Sdk create(LibraryLoader loader) {
        return Sdk.ofContextConfig(loader, new Client.ClientConfig(
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
        ), this.timeout, this.mapper);
    }

    public SdkBuilder bocCacheMaxSize(Integer cacheMaxSize) {
        this.cacheMaxSize = cacheMaxSize;
        return this;
    }

}
