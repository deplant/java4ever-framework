package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * <strong>Tvm</strong>
 * Contains methods of "tvm" module of EVER-SDK API
 * <p>
 *  
 * @version 1.45.0
 */
public final class Tvm {
  /**
   * Performs all the phases of contract execution on Transaction Executor -
   * the same component that is used on Validator Nodes.
   *
   * Can be used for contract debugging, to find out the reason why a message was not delivered successfully.
   * Validators throw away the failed external inbound messages (if they failed before `ACCEPT`) in the real network.
   * This is why these messages are impossible to debug in the real network.
   * With the help of run_executor you can do that. In fact, `process_message` function
   * performs local check with `run_executor` if there was no transaction as a result of processing
   * and returns the error, if there is one.
   *
   * Another use case to use `run_executor` is to estimate fees for message execution.
   * Set  `AccountForExecutor::Account.unlimited_balance`
   * to `true` so that emulation will not depend on the actual balance.
   * This may be needed to calculate deploy fees for an account that does not exist yet.
   * JSON with fees is in `fees` field of the result.
   *
   * One more use case - you can produce the sequence of operations,
   * thus emulating the sequential contract calls locally.
   * And so on.
   *
   * Transaction executor requires account BOC (bag of cells) as a parameter.
   * To get the account BOC - use `net.query` method to download it from GraphQL API
   * (field `boc` of `account`) or generate it with `abi.encode_account` method.
   *
   * Also it requires message BOC. To get the message BOC - use `abi.encode_message` or `abi.encode_internal_message`.
   *
   * If you need this emulation to be as precise as possible (for instance - emulate transaction
   * with particular lt in particular block or use particular blockchain config,
   * downloaded from a particular key block - then specify `execution_options` parameter.
   *
   * If you need to see the aborted transaction as a result, not as an error, set `skip_transaction_check` to `true`. Emulates all the phases of contract execution locally
   *
   * @param message Must be encoded as base64. Input message BOC.
   * @param account  Account to run on executor
   * @param executionOptions  Execution options.
   * @param abi  Contract ABI for decoding output messages
   * @param skipTransactionCheck  Skip transaction check flag
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   * @param returnUpdatedAccount Empty string is returned if the flag is `false` Return updated account flag.
   */
  public static CompletableFuture<Tvm.ResultOfRunExecutor> runExecutor(int ctxId, String message,
      Tvm.AccountForExecutor account, Tvm.ExecutionOptions executionOptions, Abi.ABI abi,
      Boolean skipTransactionCheck, Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "tvm.run_executor", new Tvm.ParamsOfRunExecutor(message, account, executionOptions, abi, skipTransactionCheck, bocCache, returnUpdatedAccount), Tvm.ResultOfRunExecutor.class);
  }

  /**
   * Performs only a part of compute phase of transaction execution
   * that is used to run get-methods of ABI-compatible contracts.
   *
   * If you try to run get-methods with `run_executor` you will get an error, because it checks ACCEPT and exits
   * if there is none, which is actually true for get-methods.
   *
   *  To get the account BOC (bag of cells) - use `net.query` method to download it from GraphQL API
   * (field `boc` of `account`) or generate it with `abi.encode_account method`.
   * To get the message BOC - use `abi.encode_message` or prepare it any other way, for instance, with FIFT script.
   *
   * Attention! Updated account state is produces as well, but only
   * `account_state.storage.state.data`  part of the BOC is updated. Executes get-methods of ABI-compatible contracts
   *
   * @param message Must be encoded as base64. Input message BOC.
   * @param account Must be encoded as base64. Account BOC.
   * @param executionOptions  Execution options.
   * @param abi  Contract ABI for decoding output messages
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   * @param returnUpdatedAccount Empty string is returned if the flag is `false` Return updated account flag.
   */
  public static CompletableFuture<Tvm.ResultOfRunTvm> runTvm(int ctxId, String message,
      String account, Tvm.ExecutionOptions executionOptions, Abi.ABI abi, Boc.BocCacheType bocCache,
      Boolean returnUpdatedAccount) throws EverSdkException {
    return EverSdk.async(ctxId, "tvm.run_tvm", new Tvm.ParamsOfRunTvm(message, account, executionOptions, abi, bocCache, returnUpdatedAccount), Tvm.ResultOfRunTvm.class);
  }

  /**
   * Executes a get-method of FIFT contract that fulfills the smc-guidelines https://test.ton.org/smc-guidelines.txt
   * and returns the result data from TVM's stack Executes a get-method of FIFT contract
   *
   * @param account  Account BOC in `base64`
   * @param functionName  Function name
   * @param input  Input parameters
   * @param executionOptions  Execution options
   * @param tupleListAsArray Default is `false`. Input parameters may use any of lists representations
   * If you receive this error on Web: "Runtime error. Unreachable code should not be executed...",
   * set this flag to true.
   * This may happen, for example, when elector contract contains too many participants Convert lists based on nested tuples in the **result** into plain arrays.
   */
  public static CompletableFuture<Tvm.ResultOfRunGet> runGet(int ctxId, String account,
      String functionName, JsonNode input, Tvm.ExecutionOptions executionOptions,
      Boolean tupleListAsArray) throws EverSdkException {
    return EverSdk.async(ctxId, "tvm.run_get", new Tvm.ParamsOfRunGet(account, functionName, input, executionOptions, tupleListAsArray), Tvm.ResultOfRunGet.class);
  }

  /**
   * @param blockchainConfig  boc with config
   * @param blockTime  time that is used as transaction time
   * @param blockLt  block logical time
   * @param transactionLt  transaction logical time
   * @param chksigAlwaysSucceed  Overrides standard TVM behaviour. If set to `true` then CHKSIG always will return `true`.
   * @param signatureId  Signature ID to be used in signature verifying instructions when CapSignatureWithId capability is enabled
   */
  public record ExecutionOptions(String blockchainConfig, Long blockTime, BigInteger blockLt,
      BigInteger transactionLt, Boolean chksigAlwaysSucceed, Long signatureId) {
  }

  /**
   * @param transaction In addition to the regular transaction fields there is a
   * `boc` field encoded with `base64` which contains source
   * transaction BOC. Parsed transaction.
   * @param outMessages Encoded as `base64` List of output messages' BOCs.
   * @param decoded  Optional decoded message bodies according to the optional `abi` parameter.
   * @param account Encoded as `base64` Updated account state BOC.
   * @param fees  Transaction fees
   */
  public record ResultOfRunExecutor(JsonNode transaction, String[] outMessages,
      Processing.DecodedOutput decoded, String account, Tvm.TransactionFees fees) {
  }

  /**
   * @param output  Values returned by get-method on stack
   */
  public record ResultOfRunGet(JsonNode output) {
  }

  /**
   * @param message Must be encoded as base64. Input message BOC.
   * @param account Must be encoded as base64. Account BOC.
   * @param executionOptions  Execution options.
   * @param abi  Contract ABI for decoding output messages
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   * @param returnUpdatedAccount Empty string is returned if the flag is `false` Return updated account flag.
   */
  public record ParamsOfRunTvm(String message, String account,
      Tvm.ExecutionOptions executionOptions, Abi.ABI abi, Boc.BocCacheType bocCache,
      Boolean returnUpdatedAccount) {
  }

  /**
   * @param inMsgFwdFee Contains the same data as ext_in_msg_fee field Deprecated.
   * @param storageFee  Fee for account storage
   * @param gasFee  Fee for processing
   * @param outMsgsFwdFee Contains the same data as total_fwd_fees field. Deprecated because of its confusing name, that is not the same with GraphQL API Transaction type's field. Deprecated.
   * @param totalAccountFees Contains the same data as account_fees field Deprecated.
   * @param totalOutput  Deprecated because it means total value sent in the transaction, which does not relate to any fees.
   * @param extInMsgFee  Fee for inbound external message import.
   * @param totalFwdFees  Total fees the account pays for message forwarding
   * @param accountFees  Total account fees for the transaction execution. Compounds of storage_fee + gas_fee + ext_in_msg_fee + total_fwd_fees
   */
  public record TransactionFees(BigInteger inMsgFwdFee, BigInteger storageFee, BigInteger gasFee,
      BigInteger outMsgsFwdFee, BigInteger totalAccountFees, BigInteger totalOutput,
      BigInteger extInMsgFee, BigInteger totalFwdFees, BigInteger accountFees) {
  }

  /**
   * @param account  Account BOC in `base64`
   * @param functionName  Function name
   * @param input  Input parameters
   * @param executionOptions  Execution options
   * @param tupleListAsArray Default is `false`. Input parameters may use any of lists representations
   * If you receive this error on Web: "Runtime error. Unreachable code should not be executed...",
   * set this flag to true.
   * This may happen, for example, when elector contract contains too many participants Convert lists based on nested tuples in the **result** into plain arrays.
   */
  public record ParamsOfRunGet(String account, String functionName, JsonNode input,
      Tvm.ExecutionOptions executionOptions, Boolean tupleListAsArray) {
  }

  public enum TvmErrorCode {
    CanNotReadTransaction(401),

    CanNotReadBlockchainConfig(402),

    TransactionAborted(403),

    InternalError(404),

    ActionPhaseFailed(405),

    AccountCodeMissing(406),

    LowBalance(407),

    AccountFrozenOrDeleted(408),

    AccountMissing(409),

    UnknownExecutionError(410),

    InvalidInputStack(411),

    InvalidAccountBoc(412),

    InvalidMessageType(413),

    ContractExecutionError(414),

    AccountIsSuspended(415);

    private final Integer value;

    TvmErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param message Must be encoded as base64. Input message BOC.
   * @param account  Account to run on executor
   * @param executionOptions  Execution options.
   * @param abi  Contract ABI for decoding output messages
   * @param skipTransactionCheck  Skip transaction check flag
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   * @param returnUpdatedAccount Empty string is returned if the flag is `false` Return updated account flag.
   */
  public record ParamsOfRunExecutor(String message, Tvm.AccountForExecutor account,
      Tvm.ExecutionOptions executionOptions, Abi.ABI abi, Boolean skipTransactionCheck,
      Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) {
  }

  public sealed interface AccountForExecutor {
    /**
     *  Non-existing account to run a creation internal message. Should be used with `skip_transaction_check = true` if the message has no deploy data since transactions on the uninitialized account are always aborted
     */
    record None() implements AccountForExecutor {
      @JsonProperty("type")
      public String type() {
        return "None";
      }
    }

    /**
     *  Emulate uninitialized account to run deploy message
     */
    record Uninit() implements AccountForExecutor {
      @JsonProperty("type")
      public String type() {
        return "Uninit";
      }
    }

    /**
     *  Account state to run message
     *
     * @param boc Encoded as base64. Account BOC.
     * @param unlimitedBalance Can be used to calculate transaction fees without balance check Flag for running account with the unlimited balance.
     */
    record Account(String boc, Boolean unlimitedBalance) implements AccountForExecutor {
      @JsonProperty("type")
      public String type() {
        return "Account";
      }
    }
  }

  /**
   * @param outMessages Encoded as `base64` List of output messages' BOCs.
   * @param decoded  Optional decoded message bodies according to the optional `abi` parameter.
   * @param account Encoded as `base64`. Attention! Only `account_state.storage.state.data` part of the BOC is updated. Updated account state BOC.
   */
  public record ResultOfRunTvm(String[] outMessages, Processing.DecodedOutput decoded,
      String account) {
  }
}
