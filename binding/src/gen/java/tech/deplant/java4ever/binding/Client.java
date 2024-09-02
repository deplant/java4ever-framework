package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.CompletableFuture;

/**
 * <strong>Client</strong>
 * Contains methods of "client" module of EVER-SDK API
 * <p>
 * Provides information about library. 
 * @version 1.45.0
 */
public final class Client {
  /**
   *  Returns Core Library API reference
   */
  public static CompletableFuture<Client.ResultOfGetApiReference> getApiReference(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "client.get_api_reference", null, Client.ResultOfGetApiReference.class);
  }

  /**
   *  Returns Core Library version
   */
  public static CompletableFuture<Client.ResultOfVersion> version(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "client.version", null, Client.ResultOfVersion.class);
  }

  /**
   *  Returns Core Library API reference
   */
  public static CompletableFuture<Client.ClientConfig> config(int ctxId) throws EverSdkException {
    return EverSdk.async(ctxId, "client.config", null, Client.ClientConfig.class);
  }

  /**
   *  Returns detailed information about this build.
   */
  public static CompletableFuture<Client.ResultOfBuildInfo> buildInfo(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "client.build_info", null, Client.ResultOfBuildInfo.class);
  }

  /**
   *  Resolves application request processing result
   *
   * @param appRequestId  Request ID received from SDK
   * @param result  Result of request processing
   */
  public static void resolveAppRequest(int ctxId, Long appRequestId, Client.AppRequestResult result)
      throws EverSdkException {
    EverSdk.asyncVoid(ctxId, "client.resolve_app_request", new Client.ParamsOfResolveAppRequest(appRequestId, result));
  }

  public enum ClientErrorCode {
    NotImplemented(1),

    InvalidHex(2),

    InvalidBase64(3),

    InvalidAddress(4),

    CallbackParamsCantBeConvertedToJson(5),

    WebsocketConnectError(6),

    WebsocketReceiveError(7),

    WebsocketSendError(8),

    HttpClientCreateError(9),

    HttpRequestCreateError(10),

    HttpRequestSendError(11),

    HttpRequestParseError(12),

    CallbackNotRegistered(13),

    NetModuleNotInit(14),

    InvalidConfig(15),

    CannotCreateRuntime(16),

    InvalidContextHandle(17),

    CannotSerializeResult(18),

    CannotSerializeError(19),

    CannotConvertJsValueToJson(20),

    CannotReceiveSpawnedResult(21),

    SetTimerError(22),

    InvalidParams(23),

    ContractsAddressConversionFailed(24),

    UnknownFunction(25),

    AppRequestError(26),

    NoSuchRequest(27),

    CanNotSendRequestResult(28),

    CanNotReceiveRequestResult(29),

    CanNotParseRequestResult(30),

    UnexpectedCallbackResponse(31),

    CanNotParseNumber(32),

    InternalError(33),

    InvalidHandle(34),

    LocalStorageError(35),

    InvalidData(36);

    private final Integer value;

    ClientErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param workchain  Workchain id that is used by default in DeploySet
   * @param messageExpirationTimeout Must be specified in milliseconds. Default is 40000 (40 sec). Message lifetime for contracts which ABI includes "expire" header.
   * @param messageExpirationTimeoutGrowFactor Default is 1.5 Factor that increases the expiration timeout for each retry
   */
  public record AbiConfig(Long workchain, Long messageExpirationTimeout,
      Long messageExpirationTimeoutGrowFactor) {
  }

  /**
   * @param name Usually it is a crate name. Dependency name.
   * @param gitCommit  Git commit hash of the related repository.
   */
  public record BuildInfoDependency(String name, String gitCommit) {
  }

  public record BindingConfig(String library, String version) {
  }

  /**
   * @param cacheInLocalStorage Default is `true`. If this value is set to `true`, downloaded proofs and master-chain BOCs are saved into the
   * persistent local storage (e.g. file system for native environments or browser's IndexedDB
   * for the web); otherwise all the data is cached only in memory in current client's context
   * and will be lost after destruction of the client. Cache proofs in the local storage.
   */
  public record ProofsConfig(Boolean cacheInLocalStorage) {
  }

  public record ClientError(Long code, String message, JsonNode data) {
  }

  /**
   * @param serverAddress  **This field is deprecated, but left for backward-compatibility.** Evernode endpoint.
   * @param endpoints Any correct URL format can be specified, including IP addresses. This parameter is prevailing over `server_address`.
   * Check the full list of [supported network endpoints](https://docs.evercloud.dev/products/evercloud/networks-endpoints). List of Evernode endpoints.
   * @param networkRetriesCount You must use `network.max_reconnect_timeout` that allows to specify maximum network resolving timeout. Deprecated.
   * @param maxReconnectTimeout Must be specified in milliseconds. Default is 120000 (2 min). Maximum time for sequential reconnections.
   * @param reconnectTimeout  Deprecated
   * @param messageRetriesCount Default is 5. The number of automatic message processing retries that SDK performs in case of `Message Expired (507)` error - but only for those messages which local emulation was successful or failed with replay protection error.
   * @param messageProcessingTimeout Must be specified in milliseconds. Default is 40000 (40 sec). Timeout that is used to process message delivery for the contracts which ABI does not include "expire" header. If the message is not delivered within the specified timeout the appropriate error occurs.
   * @param waitForTimeout Must be specified in milliseconds. Default is 40000 (40 sec). Maximum timeout that is used for query response.
   * @param outOfSyncThreshold  **DEPRECATED**: This parameter was deprecated.
   * @param sendingEndpointCount Default is 1. Maximum number of randomly chosen endpoints the library uses to broadcast a message.
   * @param latencyDetectionInterval Library periodically checks the current endpoint for blockchain data synchronization latency.
   * If the latency (time-lag) is less then `NetworkConfig.max_latency`
   * then library selects another endpoint.
   *
   * Must be specified in milliseconds. Default is 60000 (1 min). Frequency of sync latency detection.
   * @param maxLatency Must be specified in milliseconds. Default is 60000 (1 min). Maximum value for the endpoint's blockchain data synchronization latency (time-lag). Library periodically checks the current endpoint for blockchain data synchronization latency. If the latency (time-lag) is less then `NetworkConfig.max_latency` then library selects another endpoint.
   * @param queryTimeout Is is used when no timeout specified for the request to limit the answer waiting time. If no answer received during the timeout requests ends with
   * error.
   *
   * Must be specified in milliseconds. Default is 60000 (1 min). Default timeout for http requests.
   * @param queriesProtocol `HTTP` or `WS`. 
   * Default is `HTTP`. Queries protocol.
   * @param firstRempStatusTimeout First REMP status awaiting timeout. If no status received during the timeout than fallback transaction scenario is activated.
   *
   * Must be specified in milliseconds. Default is 1 (1 ms) in order to start fallback scenario
   * together with REMP statuses processing while REMP is not properly tuned yet. UNSTABLE.
   * @param nextRempStatusTimeout Subsequent REMP status awaiting timeout. If no status received during the timeout than fallback transaction scenario is activated.
   *
   * Must be specified in milliseconds. Default is 5000 (5 sec). UNSTABLE.
   * @param signatureId This parameter should be set to `global_id` field from any blockchain block if network can
   * not be reachable at the moment of message encoding and the message is aimed to be sent into
   * network with `CapSignatureWithId` enabled. Otherwise signature ID is detected automatically
   * inside message encoding functions Network signature ID which is used by VM in signature verifying instructions if capability `CapSignatureWithId` is enabled in blockchain configuration parameters.
   * @param accessKey  Access key to GraphQL API (Project secret)
   */
  public record NetworkConfig(String serverAddress, String[] endpoints, Integer networkRetriesCount,
      Long maxReconnectTimeout, Long reconnectTimeout, Integer messageRetriesCount,
      Long messageProcessingTimeout, Long waitForTimeout, Long outOfSyncThreshold,
      Integer sendingEndpointCount, Long latencyDetectionInterval, Long maxLatency,
      Long queryTimeout, Client.NetworkQueriesProtocol queriesProtocol, Long firstRempStatusTimeout,
      Long nextRempStatusTimeout, Long signatureId, String accessKey) {
  }

  /**
   * @param appRequestId Should be used in `resolve_app_request` call Request ID.
   * @param requestData  Request describing data
   */
  public record ParamsOfAppRequest(Long appRequestId, JsonNode requestData) {
  }

  public sealed interface AppRequestResult {
    /**
     *  Error occurred during request processing
     *
     * @param text  Error description
     */
    record Error(String text) implements AppRequestResult {
      @JsonProperty("type")
      public String type() {
        return "Error";
      }
    }

    /**
     *  Request processed successfully
     *
     * @param result  Request processing result
     */
    record Ok(JsonNode result) implements AppRequestResult {
      @JsonProperty("type")
      public String type() {
        return "Ok";
      }
    }
  }

  public record ResultOfGetApiReference(JsonNode api) {
  }

  /**
   * @param appRequestId  Request ID received from SDK
   * @param result  Result of request processing
   */
  public record ParamsOfResolveAppRequest(Long appRequestId, Client.AppRequestResult result) {
  }

  /**
   * @param cacheMaxSize Default is 10 MB Maximum BOC cache size in kilobytes.
   */
  public record BocConfig(Long cacheMaxSize) {
  }

  /**
   * @param version  Core Library version
   */
  public record ResultOfVersion(String version) {
  }

  /**
   *  Network protocol used to perform GraphQL queries.
   */
  public enum NetworkQueriesProtocol {
    HTTP,

    WS
  }

  /**
   * @param localStoragePath  For file based storage is a folder name where SDK will store its data. For browser based is a browser async storage key prefix. Default (recommended) value is "~/.tonclient" for native environments and ".tonclient" for web-browser.
   */
  public record ClientConfig(Client.BindingConfig binding, Client.NetworkConfig network,
      Client.CryptoConfig crypto, Client.AbiConfig abi, Client.BocConfig boc,
      Client.ProofsConfig proofs, String localStoragePath) {
  }

  /**
   * @param buildNumber  Build number assigned to this build by the CI.
   * @param dependencies  Fingerprint of the most important dependencies.
   */
  public record ResultOfBuildInfo(Long buildNumber, Client.BuildInfoDependency[] dependencies) {
  }

  /**
   *  Crypto config.
   *
   * @param mnemonicDictionary  Mnemonic dictionary that will be used by default in crypto functions. If not specified, `English` dictionary will be used.
   * @param mnemonicWordCount  Mnemonic word count that will be used by default in crypto functions. If not specified the default value will be 12.
   * @param hdkeyDerivationPath  Derivation path that will be used by default in crypto functions. If not specified `m/44'/396'/0'/0/0` will be used.
   */
  public record CryptoConfig(Crypto.MnemonicDictionary mnemonicDictionary,
      Integer mnemonicWordCount, String hdkeyDerivationPath) {
  }
}
