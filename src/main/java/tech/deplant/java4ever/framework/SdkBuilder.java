package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

public class SdkBuilder {

    // JavaConfig
    private long timeout = 30L;
    //Context.NetworkConfig
    private String[] endpoints;
    @Deprecated
    private String server_address; // deprecated, use endpoints
    private Integer network_retries_count = 5;
    private Integer message_retries_count = 5;
    private Integer message_processing_timeout = 40000;
    private Integer wait_for_timeout = 40000;
    private Integer out_of_sync_threshold = 15000;
    private Integer reconnect_timeout = 12000;
    private String access_key;
    //Context.CryptoConfig
    private Integer mnemonic_dictionary = 1;
    private Integer mnemonic_word_count = 12;
    private String hdkey_derivation_path = "m/44'/396'/0'/0/0";
    //Context.AbiConfig;
    private Integer workchain = 0;
    private Integer message_expiration_timeout = 40000;
    private Integer message_expiration_timeout_grow_factor = null;

    public SdkBuilder() {
    }

    public SdkBuilder networkEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public SdkBuilder networkServerAddress(String server_address) {
        this.server_address = server_address;
        return this;
    }

    public SdkBuilder networkRetriesCount(Integer network_retries_count) {
        this.network_retries_count = network_retries_count;
        return this;
    }

    public SdkBuilder networkMessageRetriesCount(Integer message_retries_count) {
        this.message_retries_count = message_retries_count;
        return this;
    }

    public SdkBuilder networkMessageProcessingTimeout(Integer message_processing_timeout) {
        this.message_processing_timeout = message_processing_timeout;
        return this;
    }

    public SdkBuilder networkWaitForTimeout(Integer wait_for_timeout) {
        this.wait_for_timeout = wait_for_timeout;
        return this;
    }

    public SdkBuilder networkOutOfSyncThreshold(Integer out_of_sync_threshold) {
        this.out_of_sync_threshold = out_of_sync_threshold;
        return this;
    }

    public SdkBuilder networkReconnectTimeout(Integer reconnect_timeout) {
        this.reconnect_timeout = reconnect_timeout;
        return this;
    }

    public SdkBuilder networkAccessKey(String access_key) {
        this.access_key = access_key;
        return this;
    }

    //cripto
    public SdkBuilder cryptoMnemonicDictionary(Integer mnemonic_dictionary) {
        this.mnemonic_dictionary = mnemonic_dictionary;
        return this;
    }

    public SdkBuilder cryptoMnemonicWordCount(Integer mnemonic_word_count) {
        this.mnemonic_word_count = mnemonic_word_count;
        return this;
    }

    public SdkBuilder cryptoHdkeyDerivationPath(String hdkey_derivation_path) {
        this.hdkey_derivation_path = hdkey_derivation_path;
        return this;
    }

    //abi
    public SdkBuilder abiWorkchain(Integer workchain) {
        this.workchain = workchain;
        return this;
    }

    public SdkBuilder abiMessageExpirationTimeout(Integer message_expiration_timeout) {
        this.message_expiration_timeout = message_expiration_timeout;
        return this;
    }

    public SdkBuilder abiMessageExpirationTimeoutGrowFactor(Integer message_expiration_timeout_grow_factor) {
        this.message_expiration_timeout_grow_factor = message_expiration_timeout_grow_factor;
        return this;
    }

    public SdkBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Sdk create(LibraryLoader loader) {
        return Sdk.fromConfig(loader, new Context.Config(
                new Context.NetworkConfig(
                        endpoints,
                        server_address,
                        network_retries_count,
                        message_retries_count,
                        message_processing_timeout,
                        wait_for_timeout,
                        out_of_sync_threshold,
                        reconnect_timeout,
                        access_key),
                new Context.CryptoConfig(
                        mnemonic_dictionary,
                        mnemonic_word_count,
                        hdkey_derivation_path),
                new Context.AbiConfig(
                        workchain,
                        message_expiration_timeout,
                        message_expiration_timeout_grow_factor)
        ), timeout);
    }
}
