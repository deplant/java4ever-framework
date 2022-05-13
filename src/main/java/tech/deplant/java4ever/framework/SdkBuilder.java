package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

public class SdkBuilder {

    // JavaConfig
    private long timeout = 30L;
    private ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            // and possibly other configuration, modules, then:
            .build();
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

    public SdkBuilder mapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public Sdk create(LibraryLoader loader) {
        return Sdk.ofContextConfig(loader, new Context.Config(
                new Context.NetworkConfig(
                        this.endpoints,
                        this.server_address,
                        this.network_retries_count,
                        this.message_retries_count,
                        this.message_processing_timeout,
                        this.wait_for_timeout,
                        this.out_of_sync_threshold,
                        this.reconnect_timeout,
                        this.access_key),
                new Context.CryptoConfig(
                        this.mnemonic_dictionary,
                        this.mnemonic_word_count,
                        this.hdkey_derivation_path),
                new Context.AbiConfig(
                        this.workchain,
                        this.message_expiration_timeout,
                        this.message_expiration_timeout_grow_factor)
        ), this.timeout, this.mapper);
    }

    public enum Network {

        MAIN_NET(new String[]{
                "https://eri01.main.everos.dev/",
                "https://gra01.main.everos.dev/",
                "https://gra02.main.everos.dev/",
                "https://lim01.main.everos.dev/",
                "https://rbx01.main.everos.dev/"
        }),

        DEV_NET(new String[]{
                "https://eri01.net.everos.dev/",
                "https://rbx01.net.everos.dev/",
                "https://gra01.net.everos.dev/"
        }),
        LOCALHOST(new String[]{
                "http://0.0.0.0/",
                "http://127.0.0.1/",
                "http://localhost/"
        });

        @Getter
        String[] endpoints;

        Network(String[] endpoints) {
            this.endpoints = endpoints;
        }
    }
}
