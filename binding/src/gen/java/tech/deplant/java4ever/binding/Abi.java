package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * <strong>Abi</strong>
 * Contains methods of "abi" module of EVER-SDK API
 * <p>
 * Provides message encoding and decoding according to the ABI specification. 
 * @version 1.45.0
 */
public final class Abi {
  /**
   *  Encodes message body according to ABI function call.
   *
   * @param abi  Contract ABI.
   * @param callSet Must be specified in non deploy message.
   *
   * In case of deploy message contains parameters of constructor. Function call parameters.
   * @param isInternal  True if internal message body must be encoded.
   * @param signer  Signing parameters.
   * @param processingTryIndex Used in message processing with retries.
   *
   * Encoder uses the provided try index to calculate message
   * expiration time.
   *
   * Expiration timeouts will grow with every retry.
   *
   * Default value is 0. Processing try index.
   * @param address Since ABI version 2.3 destination address of external inbound message is used in message
   * body signature calculation. Should be provided when signed external inbound message body is
   * created. Otherwise can be omitted. Destination address of the message
   * @param signatureId  Signature ID to be used in data to sign preparing when CapSignatureWithId capability is enabled
   */
  public static CompletableFuture<Abi.ResultOfEncodeMessageBody> encodeMessageBody(int ctxId,
      Abi.ABI abi, Abi.CallSet callSet, Boolean isInternal, Abi.Signer signer,
      Integer processingTryIndex, String address, Long signatureId) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_message_body", new Abi.ParamsOfEncodeMessageBody(abi, callSet, isInternal, signer, processingTryIndex, address, signatureId), Abi.ResultOfEncodeMessageBody.class);
  }

  /**
   * @param abi  Contract ABI
   * @param publicKey Must be encoded with `hex`. Public key.
   * @param message Must be encoded with `base64`. Unsigned message body BOC.
   * @param signature Must be encoded with `hex`. Signature.
   */
  public static CompletableFuture<Abi.ResultOfAttachSignatureToMessageBody> attachSignatureToMessageBody(
      int ctxId, Abi.ABI abi, String publicKey, String message, String signature) throws
      EverSdkException {
    return EverSdk.async(ctxId, "abi.attach_signature_to_message_body", new Abi.ParamsOfAttachSignatureToMessageBody(abi, publicKey, message, signature), Abi.ResultOfAttachSignatureToMessageBody.class);
  }

  /**
   * Allows to encode deploy and function call messages,
   * both signed and unsigned.
   *
   * Use cases include messages of any possible type:
   * - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind
   * of initialization);
   * - deploy without initial function call;
   * - signed/unsigned + data for signing.
   *
   * `Signer` defines how the message should or shouldn't be signed:
   *
   * `Signer::None` creates an unsigned message. This may be needed in case of some public methods,
   * that do not require authorization by pubkey.
   *
   * `Signer::External` takes public key and returns `data_to_sign` for later signing.
   * Use `attach_signature` method with the result signature to get the signed message.
   *
   * `Signer::Keys` creates a signed message with provided key pair.
   *
   * [SOON] `Signer::SigningBox` Allows using a special interface to implement signing
   * without private key disclosure to SDK. For instance, in case of using a cold wallet or HSM,
   * when application calls some API to sign data.
   *
   * There is an optional public key can be provided in deploy set in order to substitute one
   * in TVM file.
   *
   * Public key resolving priority:
   * 1. Public key from deploy set.
   * 2. Public key, specified in TVM file.
   * 3. Public key, provided by signer. Encodes an ABI-compatible message
   *
   * @param abi  Contract ABI.
   * @param address Must be specified in case of non-deploy message. Target address the message will be sent to.
   * @param deploySet Must be specified in case of deploy message. Deploy parameters.
   * @param callSet Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters
   * of the functions that will to be called upon deploy transaction. Function call parameters.
   * @param signer  Signing parameters.
   * @param processingTryIndex Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   * Encoder uses the provided try index to calculate message
   * expiration time. The 1st message expiration time is specified in
   * Client config.
   *
   * Expiration timeouts will grow with every retry.
   * Retry grow factor is set in Client config:
   * &lt;.....add config parameter with default value here&gt;
   *
   * Default value is 0. Processing try index.
   * @param signatureId  Signature ID to be used in data to sign preparing when CapSignatureWithId capability is enabled
   */
  public static CompletableFuture<Abi.ResultOfEncodeMessage> encodeMessage(int ctxId, Abi.ABI abi,
      String address, Abi.DeploySet deploySet, Abi.CallSet callSet, Abi.Signer signer,
      Integer processingTryIndex, Long signatureId) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_message", new Abi.ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex, signatureId), Abi.ResultOfEncodeMessage.class);
  }

  /**
   * Allows to encode deploy and function call messages.
   *
   * Use cases include messages of any possible type:
   * - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind
   * of initialization);
   * - deploy without initial function call;
   * - simple function call
   *
   * There is an optional public key can be provided in deploy set in order to substitute one
   * in TVM file.
   *
   * Public key resolving priority:
   * 1. Public key from deploy set.
   * 2. Public key, specified in TVM file. Encodes an internal ABI-compatible message
   *
   * @param abi Can be None if both deploy_set and call_set are None. Contract ABI.
   * @param address Must be specified in case of non-deploy message. Target address the message will be sent to.
   * @param srcAddress  Source address of the message.
   * @param deploySet Must be specified in case of deploy message. Deploy parameters.
   * @param callSet Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters
   * of the functions that will to be called upon deploy transaction. Function call parameters.
   * @param value  Value in nanotokens to be sent with message.
   * @param bounce Default is true. Flag of bounceable message.
   * @param enableIhr Default is false. Enable Instant Hypercube Routing for the message.
   */
  public static CompletableFuture<Abi.ResultOfEncodeInternalMessage> encodeInternalMessage(
      int ctxId, Abi.ABI abi, String address, String srcAddress, Abi.DeploySet deploySet,
      Abi.CallSet callSet, String value, Boolean bounce, Boolean enableIhr) throws
      EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_internal_message", new Abi.ParamsOfEncodeInternalMessage(abi, address, srcAddress, deploySet, callSet, value, bounce, enableIhr), Abi.ResultOfEncodeInternalMessage.class);
  }

  /**
   *  Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`. Returns signed message encoded in `base64`.
   *
   * @param abi  Contract ABI
   * @param publicKey  Public key encoded in `hex`.
   * @param message  Unsigned message BOC encoded in `base64`.
   * @param signature  Signature encoded in `hex`.
   */
  public static CompletableFuture<Abi.ResultOfAttachSignature> attachSignature(int ctxId,
      Abi.ABI abi, String publicKey, String message, String signature) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.attach_signature", new Abi.ParamsOfAttachSignature(abi, publicKey, message, signature), Abi.ResultOfAttachSignature.class);
  }

  /**
   *  Decodes message body using provided message BOC and ABI.
   *
   * @param abi  contract ABI
   * @param message  Message BOC
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   * @param functionName  Function name or function id if is known in advance
   */
  public static CompletableFuture<Abi.DecodedMessageBody> decodeMessage(int ctxId, Abi.ABI abi,
      String message, Boolean allowPartial, String functionName, Abi.DataLayout dataLayout) throws
      EverSdkException {
    return EverSdk.async(ctxId, "abi.decode_message", new Abi.ParamsOfDecodeMessage(abi, message, allowPartial, functionName, dataLayout), Abi.DecodedMessageBody.class);
  }

  /**
   *  Decodes message body using provided body BOC and ABI.
   *
   * @param abi  Contract ABI used to decode.
   * @param body  Message body BOC encoded in `base64`.
   * @param isInternal  True if the body belongs to the internal message.
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   * @param functionName  Function name or function id if is known in advance
   */
  public static CompletableFuture<Abi.DecodedMessageBody> decodeMessageBody(int ctxId, Abi.ABI abi,
      String body, Boolean isInternal, Boolean allowPartial, String functionName,
      Abi.DataLayout dataLayout) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.decode_message_body", new Abi.ParamsOfDecodeMessageBody(abi, body, isInternal, allowPartial, functionName, dataLayout), Abi.DecodedMessageBody.class);
  }

  /**
   *  Creates account state BOC
   *
   * @param stateInit  Account state init.
   * @param balance  Initial balance.
   * @param lastTransLt  Initial value for the `last_trans_lt`.
   * @param lastPaid  Initial value for the `last_paid`.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public static CompletableFuture<Abi.ResultOfEncodeAccount> encodeAccount(int ctxId,
      String stateInit, BigInteger balance, BigInteger lastTransLt, Long lastPaid,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_account", new Abi.ParamsOfEncodeAccount(stateInit, balance, lastTransLt, lastPaid, bocCache), Abi.ResultOfEncodeAccount.class);
  }

  /**
   * Note: this feature requires ABI 2.1 or higher. Decodes account data using provided data BOC and ABI.
   *
   * @param abi  Contract ABI
   * @param data  Data BOC or BOC handle
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   */
  public static CompletableFuture<Abi.ResultOfDecodeAccountData> decodeAccountData(int ctxId,
      Abi.ABI abi, String data, Boolean allowPartial) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.decode_account_data", new Abi.ParamsOfDecodeAccountData(abi, data, allowPartial), Abi.ResultOfDecodeAccountData.class);
  }

  /**
   * Doesn't support ABI version &gt;= 2.4. Use `encode_initial_data` instead Updates initial account data with initial values for the contract's static variables and owner's public key. This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
   *
   * @param abi  Contract ABI
   * @param data  Data BOC or BOC handle
   * @param initialData `abi` parameter should be provided to set initial data List of initial values for contract's static variables.
   * @param initialPubkey  Initial account owner's public key to set into account data
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Abi.ResultOfUpdateInitialData> updateInitialData(int ctxId,
      Abi.ABI abi, String data, JsonNode initialData, String initialPubkey,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.update_initial_data", new Abi.ParamsOfUpdateInitialData(abi, data, initialData, initialPubkey, bocCache), Abi.ResultOfUpdateInitialData.class);
  }

  /**
   * This function is analogue of `tvm.buildDataInit` function in Solidity. Encodes initial account data with initial values for the contract's static variables and owner's public key into a data BOC that can be passed to `encode_tvc` function afterwards.
   *
   * @param abi  Contract ABI
   * @param initialData `abi` parameter should be provided to set initial data List of initial values for contract's static variables.
   * @param initialPubkey  Initial account owner's public key to set into account data
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Abi.ResultOfEncodeInitialData> encodeInitialData(int ctxId,
      Abi.ABI abi, JsonNode initialData, String initialPubkey, Boc.BocCacheType bocCache) throws
      EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_initial_data", new Abi.ParamsOfEncodeInitialData(abi, initialData, initialPubkey, bocCache), Abi.ResultOfEncodeInitialData.class);
  }

  /**
   * Doesn't support ABI version &gt;= 2.4. Use `decode_account_data` instead Decodes initial values of a contract's static variables and owner's public key from account initial data This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
   *
   * @param abi Initial data is decoded if this parameter is provided Contract ABI.
   * @param data  Data BOC or BOC handle
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   */
  public static CompletableFuture<Abi.ResultOfDecodeInitialData> decodeInitialData(int ctxId,
      Abi.ABI abi, String data, Boolean allowPartial) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.decode_initial_data", new Abi.ParamsOfDecodeInitialData(abi, data, allowPartial), Abi.ResultOfDecodeInitialData.class);
  }

  /**
   * Solidity functions use ABI types for [builder encoding](https://github.com/tonlabs/TON-Solidity-Compiler/blob/master/API.md#tvmbuilderstore).
   * The simplest way to decode such a BOC is to use ABI decoding.
   * ABI has it own rules for fields layout in cells so manually encoded
   * BOC can not be described in terms of ABI rules.
   *
   * To solve this problem we introduce a new ABI type `Ref(&lt;ParamType&gt;)`
   * which allows to store `ParamType` ABI parameter in cell reference and, thus,
   * decode manually encoded BOCs. This type is available only in `decode_boc` function
   * and will not be available in ABI messages encoding until it is included into some ABI revision.
   *
   * Such BOC descriptions covers most users needs. If someone wants to decode some BOC which
   * can not be described by these rules (i.e. BOC with TLB containing constructors of flags
   * defining some parsing conditions) then they can decode the fields up to fork condition,
   * check the parsed data manually, expand the parsing schema and then decode the whole BOC
   * with the full schema. Decodes BOC into JSON as a set of provided parameters.
   *
   * @param params  Parameters to decode from BOC
   * @param boc  Data BOC or BOC handle
   */
  public static CompletableFuture<Abi.ResultOfDecodeBoc> decodeBoc(int ctxId, Abi.AbiParam[] params,
      String boc, Boolean allowPartial) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.decode_boc", new Abi.ParamsOfDecodeBoc(params, boc, allowPartial), Abi.ResultOfDecodeBoc.class);
  }

  /**
   *  Encodes given parameters in JSON into a BOC using param types from ABI.
   *
   * @param params  Parameters to encode into BOC
   * @param data  Parameters and values as a JSON structure
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public static CompletableFuture<Abi.ResultOfAbiEncodeBoc> encodeBoc(int ctxId,
      Abi.AbiParam[] params, JsonNode data, Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.encode_boc", new Abi.ParamsOfAbiEncodeBoc(params, data, bocCache), Abi.ResultOfAbiEncodeBoc.class);
  }

  /**
   *  Calculates contract function ID by contract ABI
   *
   * @param abi  Contract ABI.
   * @param functionName  Contract function name
   * @param output  If set to `true` output function ID will be returned which is used in contract response. Default is `false`
   */
  public static CompletableFuture<Abi.ResultOfCalcFunctionId> calcFunctionId(int ctxId, Abi.ABI abi,
      String functionName, Boolean output) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.calc_function_id", new Abi.ParamsOfCalcFunctionId(abi, functionName, output), Abi.ResultOfCalcFunctionId.class);
  }

  /**
   *  Extracts signature from message body and calculates hash to verify the signature
   *
   * @param abi  Contract ABI used to decode.
   * @param message  Message BOC encoded in `base64`.
   * @param signatureId  Signature ID to be used in unsigned data preparing when CapSignatureWithId capability is enabled
   */
  public static CompletableFuture<Abi.ResultOfGetSignatureData> getSignatureData(int ctxId,
      Abi.ABI abi, String message, Long signatureId) throws EverSdkException {
    return EverSdk.async(ctxId, "abi.get_signature_data", new Abi.ParamsOfGetSignatureData(abi, message, signatureId), Abi.ResultOfGetSignatureData.class);
  }

  /**
   * @param stateInit  Account state init.
   * @param balance  Initial balance.
   * @param lastTransLt  Initial value for the `last_trans_lt`.
   * @param lastPaid  Initial value for the `last_paid`.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public record ParamsOfEncodeAccount(String stateInit, BigInteger balance, BigInteger lastTransLt,
      Long lastPaid, Boc.BocCacheType bocCache) {
  }

  /**
   * @param abi  Contract ABI used to decode.
   * @param message  Message BOC encoded in `base64`.
   * @param signatureId  Signature ID to be used in unsigned data preparing when CapSignatureWithId capability is enabled
   */
  public record ParamsOfGetSignatureData(Abi.ABI abi, String message, Long signatureId) {
  }

  /**
   * @param body  Message body BOC encoded with `base64`.
   * @param dataToSign Encoded with `base64`. 
   * Presents when `message` is unsigned. Can be used for external
   * message signing. Is this case you need to sing this data and
   * produce signed message using `abi.attach_signature`. Optional data to sign.
   */
  public record ResultOfEncodeMessageBody(String body, String dataToSign) {
  }

  /**
   * @param abi  Contract ABI
   * @param publicKey  Public key encoded in `hex`.
   * @param message  Unsigned message BOC encoded in `base64`.
   * @param signature  Signature encoded in `hex`.
   */
  public record ParamsOfAttachSignature(Abi.ABI abi, String publicKey, String message,
      String signature) {
  }

  /**
   * @param abi  Contract ABI.
   * @param callSet Must be specified in non deploy message.
   *
   * In case of deploy message contains parameters of constructor. Function call parameters.
   * @param isInternal  True if internal message body must be encoded.
   * @param signer  Signing parameters.
   * @param processingTryIndex Used in message processing with retries.
   *
   * Encoder uses the provided try index to calculate message
   * expiration time.
   *
   * Expiration timeouts will grow with every retry.
   *
   * Default value is 0. Processing try index.
   * @param address Since ABI version 2.3 destination address of external inbound message is used in message
   * body signature calculation. Should be provided when signed external inbound message body is
   * created. Otherwise can be omitted. Destination address of the message
   * @param signatureId  Signature ID to be used in data to sign preparing when CapSignatureWithId capability is enabled
   */
  public record ParamsOfEncodeMessageBody(Abi.ABI abi, Abi.CallSet callSet, Boolean isInternal,
      Abi.Signer signer, Integer processingTryIndex, String address, Long signatureId) {
  }

  public record AbiContract(@JsonProperty("ABI version") Long abiVersionMajor, Long abiVersion,
      String version, String[] header, Abi.AbiFunction[] functions, Abi.AbiEvent[] events,
      Abi.AbiData[] data, Abi.AbiParam[] fields) {
  }

  public enum DataLayout {
    Input,

    Output
  }

  public enum MessageBodyType {
    Input,

    Output,

    InternalOutput,

    Event
  }

  public record AbiFunction(String name, Abi.AbiParam[] inputs, Abi.AbiParam[] outputs, String id) {
  }

  @JsonIgnoreProperties("outputs")
  public record AbiEvent(String name, Abi.AbiParam[] inputs, String id) {
  }

  /**
   * @param message  Signed message BOC
   * @param messageId  Message ID
   */
  public record ResultOfAttachSignature(String message, String messageId) {
  }

  /**
   * @param message  Message BOC encoded with `base64`.
   * @param address  Destination address.
   * @param messageId  Message id.
   */
  public record ResultOfEncodeInternalMessage(String message, String address, String messageId) {
  }

  /**
   * @param abi  Contract ABI
   * @param initialData `abi` parameter should be provided to set initial data List of initial values for contract's static variables.
   * @param initialPubkey  Initial account owner's public key to set into account data
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfEncodeInitialData(Abi.ABI abi, JsonNode initialData, String initialPubkey,
      Boc.BocCacheType bocCache) {
  }

  /**
   * @param message  Message BOC encoded with `base64`.
   * @param dataToSign Returned in case of `Signer::External`. Can be used for external
   * message signing. Is this case you need to use this data to create signature and
   * then produce signed message using `abi.attach_signature`. Optional data to be signed encoded in `base64`.
   * @param address  Destination address.
   * @param messageId  Message id.
   */
  public record ResultOfEncodeMessage(String message, String dataToSign, String address,
      String messageId) {
  }

  /**
   * @param abi Initial data is decoded if this parameter is provided Contract ABI.
   * @param data  Data BOC or BOC handle
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   */
  public record ParamsOfDecodeInitialData(Abi.ABI abi, String data, Boolean allowPartial) {
  }

  /**
   * @param params  Parameters to encode into BOC
   * @param data  Parameters and values as a JSON structure
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public record ParamsOfAbiEncodeBoc(Abi.AbiParam[] params, JsonNode data,
      Boc.BocCacheType bocCache) {
  }

  /**
   * @param params  Parameters to decode from BOC
   * @param boc  Data BOC or BOC handle
   */
  public record ParamsOfDecodeBoc(Abi.AbiParam[] params, String boc, Boolean allowPartial) {
  }

  /**
   * @param data  Updated data BOC or BOC handle
   */
  public record ResultOfEncodeInitialData(String data) {
  }

  /**
   * @param functionId  Contract function ID
   */
  public record ResultOfCalcFunctionId(Long functionId) {
  }

  /**
   * @param abi  Contract ABI used to decode.
   * @param body  Message body BOC encoded in `base64`.
   * @param isInternal  True if the body belongs to the internal message.
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   * @param functionName  Function name or function id if is known in advance
   */
  public record ParamsOfDecodeMessageBody(Abi.ABI abi, String body, Boolean isInternal,
      Boolean allowPartial, String functionName, Abi.DataLayout dataLayout) {
  }

  /**
   * @param boc  BOC encoded as base64
   */
  public record ResultOfAbiEncodeBoc(String boc) {
  }

  /**
   * @param abi Can be None if both deploy_set and call_set are None. Contract ABI.
   * @param address Must be specified in case of non-deploy message. Target address the message will be sent to.
   * @param srcAddress  Source address of the message.
   * @param deploySet Must be specified in case of deploy message. Deploy parameters.
   * @param callSet Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters
   * of the functions that will to be called upon deploy transaction. Function call parameters.
   * @param value  Value in nanotokens to be sent with message.
   * @param bounce Default is true. Flag of bounceable message.
   * @param enableIhr Default is false. Enable Instant Hypercube Routing for the message.
   */
  public record ParamsOfEncodeInternalMessage(Abi.ABI abi, String address, String srcAddress,
      Abi.DeploySet deploySet, Abi.CallSet callSet, String value, Boolean bounce,
      Boolean enableIhr) {
  }

  public sealed interface ABI {
    record Contract(Abi.AbiContract value) implements ABI {
      @JsonProperty("type")
      public String type() {
        return "Contract";
      }
    }

    record Json(String value) implements ABI {
      @JsonProperty("type")
      public String type() {
        return "Json";
      }
    }

    record Handle(Long value) implements ABI {
      @JsonProperty("type")
      public String type() {
        return "Handle";
      }
    }

    record Serialized(Abi.AbiContract value) implements ABI {
      @JsonProperty("type")
      public String type() {
        return "Serialized";
      }
    }
  }

  /**
   * @param abi  Contract ABI
   * @param publicKey Must be encoded with `hex`. Public key.
   * @param message Must be encoded with `base64`. Unsigned message body BOC.
   * @param signature Must be encoded with `hex`. Signature.
   */
  public record ParamsOfAttachSignatureToMessageBody(Abi.ABI abi, String publicKey, String message,
      String signature) {
  }

  /**
   * @param abi  Contract ABI.
   * @param address Must be specified in case of non-deploy message. Target address the message will be sent to.
   * @param deploySet Must be specified in case of deploy message. Deploy parameters.
   * @param callSet Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters
   * of the functions that will to be called upon deploy transaction. Function call parameters.
   * @param signer  Signing parameters.
   * @param processingTryIndex Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   * Encoder uses the provided try index to calculate message
   * expiration time. The 1st message expiration time is specified in
   * Client config.
   *
   * Expiration timeouts will grow with every retry.
   * Retry grow factor is set in Client config:
   * &lt;.....add config parameter with default value here&gt;
   *
   * Default value is 0. Processing try index.
   * @param signatureId  Signature ID to be used in data to sign preparing when CapSignatureWithId capability is enabled
   */
  public record ParamsOfEncodeMessage(Abi.ABI abi, String address, Abi.DeploySet deploySet,
      Abi.CallSet callSet, Abi.Signer signer, Integer processingTryIndex, Long signatureId) {
  }

  /**
   * @param initialData Initial data is decoded if `abi` input parameter is provided List of initial values of contract's public variables.
   * @param initialPubkey  Initial account owner's public key
   */
  public record ResultOfDecodeInitialData(JsonNode initialData, String initialPubkey) {
  }

  /**
   * @param bodyType  Type of the message body content.
   * @param name  Function or event name.
   * @param value  Parameters or result value.
   * @param header  Function header.
   */
  public record DecodedMessageBody(Abi.MessageBodyType bodyType, String name, JsonNode value,
      Abi.FunctionHeader header) {
  }

  public record AbiData(Long key, String name, String type, Abi.AbiParam[] components) {
  }

  public enum AbiErrorCode {
    RequiredAddressMissingForEncodeMessage(301),

    RequiredCallSetMissingForEncodeMessage(302),

    InvalidJson(303),

    InvalidMessage(304),

    EncodeDeployMessageFailed(305),

    EncodeRunMessageFailed(306),

    AttachSignatureFailed(307),

    InvalidTvcImage(308),

    RequiredPublicKeyMissingForFunctionHeader(309),

    InvalidSigner(310),

    InvalidAbi(311),

    InvalidFunctionId(312),

    InvalidData(313),

    EncodeInitialDataFailed(314),

    InvalidFunctionName(315),

    PubKeyNotSupported(316);

    private final Integer value;

    AbiErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  public record ResultOfAttachSignatureToMessageBody(String body) {
  }

  /**
   * @param signature  Signature from the message in `hex`.
   * @param unsigned  Data to verify the signature in `base64`.
   */
  public record ResultOfGetSignatureData(String signature, String unsigned) {
  }

  /**
   * Includes several hidden function parameters that contract
   * uses for security, message delivery monitoring and replay protection reasons.
   *
   * The actual set of header fields depends on the contract's ABI.
   * If a contract's ABI does not include some headers, then they are not filled. The ABI function header.
   *
   * @param expire If not specified - calculated automatically from message_expiration_timeout(),
   * try_index and message_expiration_timeout_grow_factor() (if ABI includes `expire` header). Message expiration timestamp (UNIX time) in seconds.
   * @param time If not specified, `now` is used (if ABI includes `time` header). Message creation time in milliseconds.
   * @param pubkey Encoded in `hex`. If not specified, method fails with exception (if ABI includes `pubkey` header).. Public key is used by the contract to check the signature.
   */
  public record FunctionHeader(Long expire, BigInteger time, String pubkey) {
  }

  /**
   * @param functionName  Function name that is being called. Or function id encoded as string in hex (starting with 0x).
   * @param header If an application omits some header parameters required by the
   * contract's ABI, the library will set the default values for
   * them. Function header.
   * @param input  Function input parameters according to ABI.
   */
  public record CallSet(String functionName, Abi.FunctionHeader header, JsonNode input) {
  }

  public sealed interface Signer {
    /**
     * Creates an unsigned message. No keys are provided.
     */
    record None() implements Signer {
      @JsonProperty("type")
      public String type() {
        return "None";
      }
    }

    /**
     *  Only public key is provided in unprefixed hex string format to generate unsigned message and `data_to_sign` which can be signed later.
     */
    record External(String publicKey) implements Signer {
      @JsonProperty("type")
      public String type() {
        return "External";
      }
    }

    /**
     *  Key pair is provided for signing
     */
    record Keys(Crypto.KeyPair keys) implements Signer {
      @JsonProperty("type")
      public String type() {
        return "Keys";
      }
    }

    /**
     *  Signing Box interface is provided for signing, allows Dapps to sign messages using external APIs, such as HSM, cold wallet, etc.
     */
    record SigningBox(Long handle) implements Signer {
      @JsonProperty("type")
      public String type() {
        return "SigningBox";
      }
    }
  }

  /**
   * @param data  Decoded data as a JSON structure.
   */
  public record ResultOfDecodeBoc(JsonNode data) {
  }

  /**
   * @param account  Account BOC encoded in `base64`.
   * @param id  Account ID  encoded in `hex`.
   */
  public record ResultOfEncodeAccount(String account, String id) {
  }

  /**
   * @param abi  Contract ABI
   * @param data  Data BOC or BOC handle
   * @param initialData `abi` parameter should be provided to set initial data List of initial values for contract's static variables.
   * @param initialPubkey  Initial account owner's public key to set into account data
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfUpdateInitialData(Abi.ABI abi, String data, JsonNode initialData,
      String initialPubkey, Boc.BocCacheType bocCache) {
  }

  /**
   * @param abi  Contract ABI
   * @param data  Data BOC or BOC handle
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   */
  public record ParamsOfDecodeAccountData(Abi.ABI abi, String data, Boolean allowPartial) {
  }

  /**
   * @param data  Updated data BOC or BOC handle
   */
  public record ResultOfUpdateInitialData(String data) {
  }

  /**
   * @param abi  contract ABI
   * @param message  Message BOC
   * @param allowPartial  Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
   * @param functionName  Function name or function id if is known in advance
   */
  public record ParamsOfDecodeMessage(Abi.ABI abi, String message, Boolean allowPartial,
      String functionName, Abi.DataLayout dataLayout) {
  }

  public record AbiParam(String name, String type, Abi.AbiParam[] components, Boolean init) {
  }

  /**
   * @param data  Decoded data as a JSON structure.
   */
  public record ResultOfDecodeAccountData(JsonNode data) {
  }

  /**
   * @param tvc  Content of TVC file encoded in `base64`. For compatibility reason this field can contain an encoded  `StateInit`.
   * @param code  Contract code BOC encoded with base64.
   * @param stateInit  State init BOC encoded with base64.
   * @param workchainId Default is `0`. Target workchain for destination address.
   * @param initialData  List of initial values for contract's public variables.
   * @param initialPubkey Public key resolving priority:
   * 1. Public key from deploy set.
   * 2. Public key, specified in TVM file.
   * 3. Public key, provided by Signer.
   *
   * Applicable only for contracts with ABI version &lt; 2.4. Contract initial public key should be
   * explicitly provided inside `initial_data` since ABI 2.4 Optional public key that can be provided in deploy set in order to substitute one in TVM file or provided by Signer.
   */
  public record DeploySet(String tvc, String code, String stateInit, Long workchainId,
      JsonNode initialData, String initialPubkey) {
  }

  /**
   * @param abi  Contract ABI.
   * @param functionName  Contract function name
   * @param output  If set to `true` output function ID will be returned which is used in contract response. Default is `false`
   */
  public record ParamsOfCalcFunctionId(Abi.ABI abi, String functionName, Boolean output) {
  }
}
