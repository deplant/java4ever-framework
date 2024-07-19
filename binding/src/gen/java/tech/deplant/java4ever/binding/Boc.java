package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.CompletableFuture;

/**
 * <strong>Boc</strong>
 * Contains methods of "boc" module of EVER-SDK API
 * <p>
 * BOC manipulation module. 
 * @version 1.45.0
 */
public final class Boc {
  /**
   *  Decodes tvc according to the tvc spec. Read more about tvc structure here https://github.com/tonlabs/ever-struct/blob/main/src/scheme/mod.rs#L30
   *
   * @param tvc  Contract TVC BOC encoded as base64 or BOC handle
   */
  public static CompletableFuture<Boc.ResultOfDecodeTvc> decodeTvc(int ctxId, String tvc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.decode_tvc", new Boc.ParamsOfDecodeTvc(tvc), Boc.ResultOfDecodeTvc.class);
  }

  /**
   * JSON structure is compatible with GraphQL API message object Parses message boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static CompletableFuture<Boc.ResultOfParse> parseMessage(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.parse_message", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API transaction object Parses transaction boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static CompletableFuture<Boc.ResultOfParse> parseTransaction(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.parse_transaction", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API account object Parses account boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static CompletableFuture<Boc.ResultOfParse> parseAccount(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.parse_account", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API block object Parses block boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static CompletableFuture<Boc.ResultOfParse> parseBlock(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.parse_block", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API shardstate object Parses shardstate boc into a JSON
   *
   * @param boc  BOC encoded as base64
   * @param id  Shardstate identifier
   * @param workchainId  Workchain shardstate belongs to
   */
  public static CompletableFuture<Boc.ResultOfParse> parseShardstate(int ctxId, String boc,
      String id, Long workchainId) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.parse_shardstate", new Boc.ParamsOfParseShardstate(boc, id, workchainId), Boc.ResultOfParse.class);
  }

  /**
   *  Extract blockchain configuration from key block and also from zerostate.
   *
   * @param blockBoc  Key block BOC or zerostate BOC encoded as base64
   */
  public static CompletableFuture<Boc.ResultOfGetBlockchainConfig> getBlockchainConfig(int ctxId,
      String blockBoc) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.get_blockchain_config", new Boc.ParamsOfGetBlockchainConfig(blockBoc), Boc.ResultOfGetBlockchainConfig.class);
  }

  /**
   *  Calculates BOC root hash
   *
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static CompletableFuture<Boc.ResultOfGetBocHash> getBocHash(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.get_boc_hash", new Boc.ParamsOfGetBocHash(boc), Boc.ResultOfGetBocHash.class);
  }

  /**
   *  Calculates BOC depth
   *
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static CompletableFuture<Boc.ResultOfGetBocDepth> getBocDepth(int ctxId, String boc) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.get_boc_depth", new Boc.ParamsOfGetBocDepth(boc), Boc.ResultOfGetBocDepth.class);
  }

  /**
   *  Extracts code from TVC contract image
   *
   * @param tvc  Contract TVC image or image BOC handle
   */
  public static CompletableFuture<Boc.ResultOfGetCodeFromTvc> getCodeFromTvc(int ctxId, String tvc)
      throws EverSdkException {
    return EverSdk.async(ctxId, "boc.get_code_from_tvc", new Boc.ParamsOfGetCodeFromTvc(tvc), Boc.ResultOfGetCodeFromTvc.class);
  }

  /**
   *  Get BOC from cache
   *
   * @param bocRef  Reference to the cached BOC
   */
  public static CompletableFuture<Boc.ResultOfBocCacheGet> cacheGet(int ctxId, String bocRef) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.cache_get", new Boc.ParamsOfBocCacheGet(bocRef), Boc.ResultOfBocCacheGet.class);
  }

  /**
   *  Save BOC into cache or increase pin counter for existing pinned BOC
   *
   * @param boc  BOC encoded as base64 or BOC reference
   * @param cacheType  Cache type
   */
  public static CompletableFuture<Boc.ResultOfBocCacheSet> cacheSet(int ctxId, String boc,
      Boc.BocCacheType cacheType) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.cache_set", new Boc.ParamsOfBocCacheSet(boc, cacheType), Boc.ResultOfBocCacheSet.class);
  }

  /**
   *  Unpin BOCs with specified pin defined in the `cache_set`. Decrease pin reference counter for BOCs with specified pin defined in the `cache_set`. BOCs which have only 1 pin and its reference counter become 0 will be removed from cache
   *
   * @param pin  Pinned name
   * @param bocRef If it is provided then only referenced BOC is unpinned Reference to the cached BOC.
   */
  public static void cacheUnpin(int ctxId, String pin, String bocRef) throws EverSdkException {
    EverSdk.asyncVoid(ctxId, "boc.cache_unpin", new Boc.ParamsOfBocCacheUnpin(pin, bocRef));
  }

  /**
   *  Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type.
   *
   * @param builder  Cell builder operations.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Boc.ResultOfEncodeBoc> encodeBoc(int ctxId,
      Boc.BuilderOp[] builder, Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.encode_boc", new Boc.ParamsOfEncodeBoc(builder, bocCache), Boc.ResultOfEncodeBoc.class);
  }

  /**
   *  Returns the contract code's salt if it is present.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Boc.ResultOfGetCodeSalt> getCodeSalt(int ctxId, String code,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.get_code_salt", new Boc.ParamsOfGetCodeSalt(code, bocCache), Boc.ResultOfGetCodeSalt.class);
  }

  /**
   * Returns the new contract code with salt. Sets new salt to contract code.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param salt BOC encoded as base64 or BOC handle Code salt to set.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Boc.ResultOfSetCodeSalt> setCodeSalt(int ctxId, String code,
      String salt, Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.set_code_salt", new Boc.ParamsOfSetCodeSalt(code, salt, bocCache), Boc.ResultOfSetCodeSalt.class);
  }

  /**
   *  Decodes contract's initial state into code, data, libraries and special options.
   *
   * @param stateInit  Contract StateInit image BOC encoded as base64 or BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Boc.ResultOfDecodeStateInit> decodeStateInit(int ctxId,
      String stateInit, Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.decode_state_init", new Boc.ParamsOfDecodeStateInit(stateInit, bocCache), Boc.ResultOfDecodeStateInit.class);
  }

  /**
   *  Encodes initial contract state from code, data, libraries ans special options (see input params)
   *
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static CompletableFuture<Boc.ResultOfEncodeStateInit> encodeStateInit(int ctxId,
      String code, String data, String library, Boolean tick, Boolean tock, Long splitDepth,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.encode_state_init", new Boc.ParamsOfEncodeStateInit(code, data, library, tick, tock, splitDepth, bocCache), Boc.ResultOfEncodeStateInit.class);
  }

  /**
   * Allows to encode any external inbound message. Encodes a message
   *
   * @param src  Source address.
   * @param dst  Destination address.
   * @param init  Bag of cells with state init (used in deploy messages).
   * @param body  Bag of cells with the message body encoded as base64.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public static CompletableFuture<Boc.ResultOfEncodeExternalInMessage> encodeExternalInMessage(
      int ctxId, String src, String dst, String init, String body, Boc.BocCacheType bocCache) throws
      EverSdkException {
    return EverSdk.async(ctxId, "boc.encode_external_in_message", new Boc.ParamsOfEncodeExternalInMessage(src, dst, init, body, bocCache), Boc.ResultOfEncodeExternalInMessage.class);
  }

  /**
   *  Returns the compiler version used to compile the code.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   */
  public static CompletableFuture<Boc.ResultOfGetCompilerVersion> getCompilerVersion(int ctxId,
      String code) throws EverSdkException {
    return EverSdk.async(ctxId, "boc.get_compiler_version", new Boc.ParamsOfGetCompilerVersion(code), Boc.ResultOfGetCompilerVersion.class);
  }

  /**
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public record ParamsOfGetBocDepth(String boc) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   */
  public record ParamsOfGetCompilerVersion(String code) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param salt BOC encoded as base64 or BOC handle Code salt to set.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfSetCodeSalt(String code, String salt, Boc.BocCacheType bocCache) {
  }

  /**
   * @param boc  BOC encoded as base64.
   */
  public record ResultOfBocCacheGet(String boc) {
  }

  /**
   * @param tvc  Contract TVC BOC encoded as base64 or BOC handle
   */
  public record ParamsOfDecodeTvc(String tvc) {
  }

  /**
   * @param message  Message BOC encoded with `base64`.
   * @param messageId  Message id.
   */
  public record ResultOfEncodeExternalInMessage(String message, String messageId) {
  }

  /**
   * @param boc  BOC encoded as base64 or BOC reference
   * @param cacheType  Cache type
   */
  public record ParamsOfBocCacheSet(String boc, Boc.BocCacheType cacheType) {
  }

  /**
   * @param configBoc  Blockchain config BOC encoded as base64
   */
  public record ResultOfGetBlockchainConfig(String configBoc) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfGetCodeSalt(String code, Boc.BocCacheType bocCache) {
  }

  /**
   * @param builder  Cell builder operations.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfEncodeBoc(Boc.BuilderOp[] builder, Boc.BocCacheType bocCache) {
  }

  /**
   * @param src  Source address.
   * @param dst  Destination address.
   * @param init  Bag of cells with state init (used in deploy messages).
   * @param body  Bag of cells with the message body encoded as base64.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public record ParamsOfEncodeExternalInMessage(String src, String dst, String init, String body,
      Boc.BocCacheType bocCache) {
  }

  public enum BocErrorCode {
    InvalidBoc(201),

    SerializationError(202),

    InappropriateBlock(203),

    MissingSourceBoc(204),

    InsufficientCacheSize(205),

    BocRefNotFound(206),

    InvalidBocRef(207);

    private final Integer value;

    BocErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param tvc  Contract TVC image or image BOC handle
   */
  public record ParamsOfGetCodeFromTvc(String tvc) {
  }

  /**
   * @param salt BOC encoded as base64 or BOC handle Contract code salt if present.
   */
  public record ResultOfGetCodeSalt(String salt) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfEncodeStateInit(String code, String data, String library, Boolean tick,
      Boolean tock, Long splitDepth, Boc.BocCacheType bocCache) {
  }

  /**
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public record ParamsOfGetBocHash(String boc) {
  }

  /**
   * @param blockBoc  Key block BOC or zerostate BOC encoded as base64
   */
  public record ParamsOfGetBlockchainConfig(String blockBoc) {
  }

  public sealed interface Tvc {
    record V1(Boc.TvcV1 value) implements Tvc {
      @JsonProperty("type")
      public String type() {
        return "V1";
      }
    }
  }

  /**
   * @param bocRef  Reference to the cached BOC
   */
  public record ParamsOfBocCacheGet(String bocRef) {
  }

  /**
   * @param depth  BOC root cell depth
   */
  public record ResultOfGetBocDepth(Long depth) {
  }

  /**
   * @param bocRef  Reference to the cached BOC
   */
  public record ResultOfBocCacheSet(String bocRef) {
  }

  /**
   * @param boc  BOC encoded as base64
   * @param id  Shardstate identifier
   * @param workchainId  Workchain shardstate belongs to
   */
  public record ParamsOfParseShardstate(String boc, String id, Long workchainId) {
  }

  /**
   * @param tvc  Decoded TVC
   */
  public record ResultOfDecodeTvc(Boc.Tvc tvc) {
  }

  /**
   * @param code BOC encoded as base64 or BOC handle Contract code with salt set.
   */
  public record ResultOfSetCodeSalt(String code) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param codeHash  Contract code hash
   * @param codeDepth  Contract code depth
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param dataHash  Contract data hash
   * @param dataDepth  Contract data depth
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param compilerVersion  Compiler version, for example 'sol 0.49.0'
   */
  public record ResultOfDecodeStateInit(String code, String codeHash, Long codeDepth, String data,
      String dataHash, Long dataDepth, String library, Boolean tick, Boolean tock, Long splitDepth,
      String compilerVersion) {
  }

  /**
   * @param parsed  JSON containing parsed BOC
   */
  public record ResultOfParse(JsonNode parsed) {
  }

  public record TvcV1(String code, String description) {
  }

  /**
   * @param stateInit  Contract StateInit image BOC encoded as base64 or BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public record ParamsOfDecodeStateInit(String stateInit, Boc.BocCacheType bocCache) {
  }

  /**
   * @param hash  BOC root hash encoded with hex
   */
  public record ResultOfGetBocHash(String hash) {
  }

  /**
   * @param boc  Encoded cell BOC or BOC cache key.
   */
  public record ResultOfEncodeBoc(String boc) {
  }

  /**
   * @param boc  BOC encoded as base64
   */
  public record ParamsOfParse(String boc) {
  }

  /**
   * @param pin  Pinned name
   * @param bocRef If it is provided then only referenced BOC is unpinned Reference to the cached BOC.
   */
  public record ParamsOfBocCacheUnpin(String pin, String bocRef) {
  }

  /**
   * @param version  Compiler version, for example 'sol 0.49.0'
   */
  public record ResultOfGetCompilerVersion(String version) {
  }

  /**
   * @param stateInit  Contract StateInit image BOC encoded as base64 or BOC handle of boc_cache parameter was specified
   */
  public record ResultOfEncodeStateInit(String stateInit) {
  }

  /**
   * @param code  Contract code encoded as base64
   */
  public record ResultOfGetCodeFromTvc(String code) {
  }

  /**
   *  Cell builder operation.
   */
  public sealed interface BuilderOp {
    /**
     *  Append integer to cell data.
     *
     * @param size  Bit size of the value.
     * @param value e.g. `123`, `-123`. - Decimal string. e.g. `"123"`, `"-123"`.
     * - `0x` prefixed hexadecimal string.
     *   e.g `0x123`, `0X123`, `-0x123`. Value: - `Number` containing integer number.
     */
    record Integer(Long size, String value) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Integer";
      }
    }

    /**
     *  Append bit string to cell data.
     *
     * @param value Contains hexadecimal string representation:
     * - Can end with `_` tag.
     * - Can be prefixed with `x` or `X`.
     * - Can be prefixed with `x{` or `X{` and ended with `}`.
     *
     * Contains binary string represented as a sequence
     * of `0` and `1` prefixed with `n` or `N`.
     *
     * Examples:
     * `1AB`, `x1ab`, `X1AB`, `x{1abc}`, `X{1ABC}`
     * `2D9_`, `x2D9_`, `X2D9_`, `x{2D9_}`, `X{2D9_}`
     * `n00101101100`, `N00101101100` Bit string content using bitstring notation. See `TON VM specification` 1.0.
     */
    record BitString(String value) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "BitString";
      }
    }

    /**
     *  Append ref to nested cells.
     *
     * @param builder  Nested cell builder.
     */
    record Cell(Boc.BuilderOp[] builder) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Cell";
      }
    }

    /**
     *  Append ref to nested cell.
     *
     * @param boc  Nested cell BOC encoded with `base64` or BOC cache key.
     */
    record CellBoc(String boc) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "CellBoc";
      }
    }

    /**
     *  Address.
     *
     * @param address  Address in a common `workchain:account` or base64 format.
     */
    record Address(String address) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Address";
      }
    }
  }

  public sealed interface BocCacheType {
    /**
     * Such BOC will not be removed from cache until it is unpinned BOCs can have several pins and each of the pins has reference counter indicating how many
     * times the BOC was pinned with the pin. BOC is removed from cache after all references for all
     * pins are unpinned with `cache_unpin` function calls. Pin the BOC with `pin` name.
     */
    record Pinned(String pin) implements BocCacheType {
      @JsonProperty("type")
      public String type() {
        return "Pinned";
      }
    }

    /**
     * BOC resides there until it is replaced with other BOCs if it is not used BOC is placed into a common BOC pool with limited size regulated by LRU (least recently used) cache lifecycle.
     */
    record Unpinned() implements BocCacheType {
      @JsonProperty("type")
      public String type() {
        return "Unpinned";
      }
    }
  }
}
