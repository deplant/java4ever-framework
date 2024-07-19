package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * <strong>Net</strong>
 * Contains methods of "net" module of EVER-SDK API
 * <p>
 * Network access. 
 * @version 1.45.0
 */
public final class Net {
  /**
   *  Performs DAppServer GraphQL query.
   *
   * @param query  GraphQL query text.
   * @param variables Must be a map with named values that can be used in query. Variables used in query.
   */
  public static CompletableFuture<Net.ResultOfQuery> query(int ctxId, String query,
      JsonNode variables) throws EverSdkException {
    return EverSdk.async(ctxId, "net.query", new Net.ParamsOfQuery(query, variables), Net.ResultOfQuery.class);
  }

  /**
   *  Performs multiple queries per single fetch.
   *
   * @param operations  List of query operations that must be performed per single fetch.
   */
  public static CompletableFuture<Net.ResultOfBatchQuery> batchQuery(int ctxId,
      Net.ParamsOfQueryOperation[] operations) throws EverSdkException {
    return EverSdk.async(ctxId, "net.batch_query", new Net.ParamsOfBatchQuery(operations), Net.ResultOfBatchQuery.class);
  }

  /**
   * Queries data that satisfies the `filter` conditions,
   * limits the number of returned records and orders them.
   * The projection fields are limited to `result` fields Queries collection data
   *
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param order  Sorting order
   * @param limit  Number of documents to return
   */
  public static CompletableFuture<Net.ResultOfQueryCollection> queryCollection(int ctxId,
      String collection, JsonNode filter, String result, Net.OrderBy[] order, Long limit) throws
      EverSdkException {
    return EverSdk.async(ctxId, "net.query_collection", new Net.ParamsOfQueryCollection(collection, filter, result, order, limit), Net.ResultOfQueryCollection.class);
  }

  /**
   * Aggregates values from the specified `fields` for records
   * that satisfies the `filter` conditions, Aggregates collection data.
   *
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param fields  Projection (result) string
   */
  public static CompletableFuture<Net.ResultOfAggregateCollection> aggregateCollection(int ctxId,
      String collection, JsonNode filter, Net.FieldAggregation[] fields) throws EverSdkException {
    return EverSdk.async(ctxId, "net.aggregate_collection", new Net.ParamsOfAggregateCollection(collection, filter, fields), Net.ResultOfAggregateCollection.class);
  }

  /**
   * Triggers only once.
   * If object that satisfies the `filter` conditions
   * already exists - returns it immediately.
   * If not - waits for insert/update of data within the specified `timeout`,
   * and returns it.
   * The projection fields are limited to `result` fields Returns an object that fulfills the conditions or waits for its appearance
   *
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param timeout  Query timeout
   */
  public static CompletableFuture<Net.ResultOfWaitForCollection> waitForCollection(int ctxId,
      String collection, JsonNode filter, String result, Long timeout) throws EverSdkException {
    return EverSdk.async(ctxId, "net.wait_for_collection", new Net.ParamsOfWaitForCollection(collection, filter, result, timeout), Net.ResultOfWaitForCollection.class);
  }

  /**
   * Cancels a subscription specified by its handle. Cancels a subscription
   */
  public static void unsubscribe(int ctxId, Net.ResultOfSubscribeCollection params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "net.unsubscribe", params);
  }

  /**
   * Triggers for each insert/update of data that satisfies
   * the `filter` conditions.
   * The projection fields are limited to `result` fields.
   *
   * The subscription is a persistent communication channel between
   * client and Free TON Network.
   * All changes in the blockchain will be reflected in realtime.
   * Changes means inserts and updates of the blockchain entities.
   *
   * ### Important Notes on Subscriptions
   *
   * Unfortunately sometimes the connection with the network brakes down.
   * In this situation the library attempts to reconnect to the network.
   * This reconnection sequence can take significant time.
   * All of this time the client is disconnected from the network.
   *
   * Bad news is that all blockchain changes that happened while
   * the client was disconnected are lost.
   *
   * Good news is that the client report errors to the callback when
   * it loses and resumes connection.
   *
   * So, if the lost changes are important to the application then
   * the application must handle these error reports.
   *
   * Library reports errors with `responseType` == 101
   * and the error object passed via `params`.
   *
   * When the library has successfully reconnected
   * the application receives callback with
   * `responseType` == 101 and `params.code` == 614 (NetworkModuleResumed).
   *
   * Application can use several ways to handle this situation:
   * - If application monitors changes for the single blockchain
   * object (for example specific account):  application
   * can perform a query for this object and handle actual data as a
   * regular data from the subscription.
   * - If application monitors sequence of some blockchain objects
   * (for example transactions of the specific account): application must
   * refresh all cached (or visible to user) lists where this sequences presents. Creates a collection subscription
   *
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   */
  public static CompletableFuture<Net.ResultOfSubscribeCollection> subscribeCollection(int ctxId,
      String collection, JsonNode filter, String result, Consumer<JsonNode> callback) throws
      EverSdkException {
    return EverSdk.asyncCallback(ctxId, "net.subscribe_collection", new Net.ParamsOfSubscribeCollection(collection, filter, result), Net.ResultOfSubscribeCollection.class, callback);
  }

  /**
   * The subscription is a persistent communication channel between
   * client and Everscale Network.
   *
   * ### Important Notes on Subscriptions
   *
   * Unfortunately sometimes the connection with the network breaks down.
   * In this situation the library attempts to reconnect to the network.
   * This reconnection sequence can take significant time.
   * All of this time the client is disconnected from the network.
   *
   * Bad news is that all changes that happened while
   * the client was disconnected are lost.
   *
   * Good news is that the client report errors to the callback when
   * it loses and resumes connection.
   *
   * So, if the lost changes are important to the application then
   * the application must handle these error reports.
   *
   * Library reports errors with `responseType` == 101
   * and the error object passed via `params`.
   *
   * When the library has successfully reconnected
   * the application receives callback with
   * `responseType` == 101 and `params.code` == 614 (NetworkModuleResumed).
   *
   * Application can use several ways to handle this situation:
   * - If application monitors changes for the single
   * object (for example specific account):  application
   * can perform a query for this object and handle actual data as a
   * regular data from the subscription.
   * - If application monitors sequence of some objects
   * (for example transactions of the specific account): application must
   * refresh all cached (or visible to user) lists where this sequences presents. Creates a subscription
   *
   * @param subscription  GraphQL subscription text.
   * @param variables Must be a map with named values that can be used in query. Variables used in subscription.
   */
  public static CompletableFuture<Net.ResultOfSubscribeCollection> subscribe(int ctxId,
      String subscription, JsonNode variables, Consumer<JsonNode> callback) throws
      EverSdkException {
    return EverSdk.asyncCallback(ctxId, "net.subscribe", new Net.ParamsOfSubscribe(subscription, variables), Net.ResultOfSubscribeCollection.class, callback);
  }

  /**
   *  Suspends network module to stop any network activity
   */
  public static void suspend(int ctxId) throws EverSdkException {
    EverSdk.asyncVoid(ctxId, "net.suspend", null);
  }

  /**
   *  Resumes network module to enable network activity
   */
  public static void resume(int ctxId) throws EverSdkException {
    EverSdk.asyncVoid(ctxId, "net.resume", null);
  }

  /**
   *  Returns ID of the last block in a specified account shard
   *
   * @param address  Account address
   */
  public static CompletableFuture<Net.ResultOfFindLastShardBlock> findLastShardBlock(int ctxId,
      String address) throws EverSdkException {
    return EverSdk.async(ctxId, "net.find_last_shard_block", new Net.ParamsOfFindLastShardBlock(address), Net.ResultOfFindLastShardBlock.class);
  }

  /**
   *  Requests the list of alternative endpoints from server
   */
  public static CompletableFuture<Net.EndpointsSet> fetchEndpoints(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "net.fetch_endpoints", null, Net.EndpointsSet.class);
  }

  /**
   *  Sets the list of endpoints to use on reinit
   */
  public static void setEndpoints(int ctxId, Net.EndpointsSet params) throws EverSdkException {
    EverSdk.asyncVoid(ctxId, "net.set_endpoints", params);
  }

  /**
   *  Requests the list of alternative endpoints from server
   */
  public static CompletableFuture<Net.ResultOfGetEndpoints> getEndpoints(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "net.get_endpoints", null, Net.ResultOfGetEndpoints.class);
  }

  /**
   * *Attention* this query retrieves data from 'Counterparties' service which is not supported in
   * the opensource version of DApp Server (and will not be supported) as well as in Evernode SE (will be supported in SE in future),
   * but is always accessible via [EVER OS Clouds](../ton-os-api/networks.md) Allows to query and paginate through the list of accounts that the specified account has interacted with, sorted by the time of the last internal message between accounts
   *
   * @param account  Account address
   * @param result  Projection (result) string
   * @param first  Number of counterparties to return
   * @param after  `cursor` field of the last received result
   */
  public static CompletableFuture<Net.ResultOfQueryCollection> queryCounterparties(int ctxId,
      String account, String result, Long first, String after) throws EverSdkException {
    return EverSdk.async(ctxId, "net.query_counterparties", new Net.ParamsOfQueryCounterparties(account, result, first, after), Net.ResultOfQueryCollection.class);
  }

  /**
   * Performs recursive retrieval of a transactions tree produced by a specific message:
   * in_msg -&gt; dst_transaction -&gt; out_messages -&gt; dst_transaction -&gt; ...
   * If the chain of transactions execution is in progress while the function is running,
   * it will wait for the next transactions to appear until the full tree or more than 50 transactions
   * are received.
   *
   * All the retrieved messages and transactions are included
   * into `result.messages` and `result.transactions` respectively.
   *
   * Function reads transactions layer by layer, by pages of 20 transactions.
   *
   * The retrieval process goes like this:
   * Let's assume we have an infinite chain of transactions and each transaction generates 5 messages.
   * 1. Retrieve 1st message (input parameter) and corresponding transaction - put it into result.
   * It is the first level of the tree of transactions - its root.
   * Retrieve 5 out message ids from the transaction for next steps.
   * 2. Retrieve 5 messages and corresponding transactions on the 2nd layer. Put them into result.
   * Retrieve 5*5 out message ids from these transactions for next steps
   * 3. Retrieve 20 (size of the page) messages and transactions (3rd layer) and 20*5=100 message ids (4th layer).
   * 4. Retrieve the last 5 messages and 5 transactions on the 3rd layer + 15 messages and transactions (of 100) from the 4th layer
   * + 25 message ids of the 4th layer + 75 message ids of the 5th layer.
   * 5. Retrieve 20 more messages and 20 more transactions of the 4th layer + 100 more message ids of the 5th layer.
   * 6. Now we have 1+5+20+20+20 = 66 transactions, which is more than 50. Function exits with the tree of
   * 1m-&gt;1t-&gt;5m-&gt;5t-&gt;25m-&gt;25t-&gt;35m-&gt;35t. If we see any message ids in the last transactions out_msgs, which don't have
   * corresponding messages in the function result, it means that the full tree was not received and we need to continue iteration.
   *
   * To summarize, it is guaranteed that each message in `result.messages` has the corresponding transaction
   * in the `result.transactions`.
   * But there is no guarantee that all messages from transactions `out_msgs` are
   * presented in `result.messages`.
   * So the application has to continue retrieval for missing messages if it requires. Returns a tree of transactions triggered by a specific message.
   *
   * @param inMsg  Input message id.
   * @param abiRegistry  List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry.
   * @param timeout If some of the following messages and transactions are missing yet
   * The maximum waiting time is regulated by this option.
   *
   * Default value is 60000 (1 min). If `timeout` is set to 0 then function will wait infinitely
   * until the whole transaction tree is executed Timeout used to limit waiting time for the missing messages and transaction.
   * @param transactionMaxCount If transaction tree contains more transaction then this parameter then only first `transaction_max_count` transaction are awaited and returned.
   *
   * Default value is 50. If `transaction_max_count` is set to 0 then no limitation on
   * transaction count is used and all transaction are returned. Maximum transaction count to wait.
   */
  public static CompletableFuture<Net.ResultOfQueryTransactionTree> queryTransactionTree(int ctxId,
      String inMsg, Abi.ABI[] abiRegistry, Long timeout, Long transactionMaxCount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "net.query_transaction_tree", new Net.ParamsOfQueryTransactionTree(inMsg, abiRegistry, timeout, transactionMaxCount), Net.ResultOfQueryTransactionTree.class);
  }

  /**
   * Block iterator uses robust iteration methods that guaranties that every
   * block in the specified range isn't missed or iterated twice.
   *
   * Iterated range can be reduced with some filters:
   * - `start_time` – the bottom time range. Only blocks with `gen_utime`
   * more or equal to this value is iterated. If this parameter is omitted then there is
   * no bottom time edge, so all blocks since zero state is iterated.
   * - `end_time` – the upper time range. Only blocks with `gen_utime`
   * less then this value is iterated. If this parameter is omitted then there is
   * no upper time edge, so iterator never finishes.
   * - `shard_filter` – workchains and shard prefixes that reduce the set of interesting
   * blocks. Block conforms to the shard filter if it belongs to the filter workchain
   * and the first bits of block's `shard` fields matches to the shard prefix.
   * Only blocks with suitable shard are iterated.
   *
   * Items iterated is a JSON objects with block data. The minimal set of returned
   * fields is:
   * ```text
   * id
   * gen_utime
   * workchain_id
   * shard
   * after_split
   * after_merge
   * prev_ref {
   *     root_hash
   * }
   * prev_alt_ref {
   *     root_hash
   * }
   * ```
   * Application can request additional fields in the `result` parameter.
   *
   * Application should call the `remove_iterator` when iterator is no longer required. Creates block iterator.
   *
   * @param startTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &gt;= `start_time`.
   * Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds. Starting time to iterate from.
   * @param endTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &lt; `end_time`.
   * Otherwise the iteration never stops.
   *
   * Must be specified in seconds. Optional end time to iterate for.
   * @param shardFilter If the application specifies this parameter and it is not the empty array
   * then the iteration will include items related to accounts that belongs to
   * the specified shard prefixes.
   * Shard prefix must be represented as a string "workchain:prefix".
   * Where `workchain` is a signed integer and the `prefix` if a hexadecimal
   * representation if the 64-bit unsigned integer with tagged shard prefix.
   * For example: "0:3800000000000000". Shard prefix filter.
   * @param result List of the fields that must be returned for iterated items.
   * This field is the same as the `result` parameter of
   * the `query_collection` function.
   * Note that iterated items can contains additional fields that are
   * not requested in the `result`. Projection (result) string.
   */
  public static CompletableFuture<Net.RegisteredIterator> createBlockIterator(int ctxId,
      Long startTime, Long endTime, String[] shardFilter, String result) throws EverSdkException {
    return EverSdk.async(ctxId, "net.create_block_iterator", new Net.ParamsOfCreateBlockIterator(startTime, endTime, shardFilter, result), Net.RegisteredIterator.class);
  }

  /**
   * The iterator stays exactly at the same position where the `resume_state` was caught.
   *
   * Application should call the `remove_iterator` when iterator is no longer required. Resumes block iterator.
   *
   * @param resumeState Same as value returned from `iterator_next`. Iterator state from which to resume.
   */
  public static CompletableFuture<Net.RegisteredIterator> resumeBlockIterator(int ctxId,
      JsonNode resumeState) throws EverSdkException {
    return EverSdk.async(ctxId, "net.resume_block_iterator", new Net.ParamsOfResumeBlockIterator(resumeState), Net.RegisteredIterator.class);
  }

  /**
   * Transaction iterator uses robust iteration methods that guaranty that every
   * transaction in the specified range isn't missed or iterated twice.
   *
   * Iterated range can be reduced with some filters:
   * - `start_time` – the bottom time range. Only transactions with `now`
   * more or equal to this value are iterated. If this parameter is omitted then there is
   * no bottom time edge, so all the transactions since zero state are iterated.
   * - `end_time` – the upper time range. Only transactions with `now`
   * less then this value are iterated. If this parameter is omitted then there is
   * no upper time edge, so iterator never finishes.
   * - `shard_filter` – workchains and shard prefixes that reduce the set of interesting
   * accounts. Account address conforms to the shard filter if
   * it belongs to the filter workchain and the first bits of address match to
   * the shard prefix. Only transactions with suitable account addresses are iterated.
   * - `accounts_filter` – set of account addresses whose transactions must be iterated.
   * Note that accounts filter can conflict with shard filter so application must combine
   * these filters carefully.
   *
   * Iterated item is a JSON objects with transaction data. The minimal set of returned
   * fields is:
   * ```text
   * id
   * account_addr
   * now
   * balance_delta(format:DEC)
   * bounce { bounce_type }
   * in_message {
   *     id
   *     value(format:DEC)
   *     msg_type
   *     src
   * }
   * out_messages {
   *     id
   *     value(format:DEC)
   *     msg_type
   *     dst
   * }
   * ```
   * Application can request an additional fields in the `result` parameter.
   *
   * Another parameter that affects on the returned fields is the `include_transfers`.
   * When this parameter is `true` the iterator computes and adds `transfer` field containing
   * list of the useful `TransactionTransfer` objects.
   * Each transfer is calculated from the particular message related to the transaction
   * and has the following structure:
   * - message – source message identifier.
   * - isBounced – indicates that the transaction is bounced, which means the value will be returned back to the sender.
   * - isDeposit – indicates that this transfer is the deposit (true) or withdraw (false).
   * - counterparty – account address of the transfer source or destination depending on `isDeposit`.
   * - value – amount of nano tokens transferred. The value is represented as a decimal string
   * because the actual value can be more precise than the JSON number can represent. Application
   * must use this string carefully – conversion to number can follow to loose of precision.
   *
   * Application should call the `remove_iterator` when iterator is no longer required. Creates transaction iterator.
   *
   * @param startTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &gt;= `start_time`.
   * Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds. Starting time to iterate from.
   * @param endTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &lt; `end_time`.
   * Otherwise the iteration never stops.
   *
   * Must be specified in seconds. Optional end time to iterate for.
   * @param shardFilter If the application specifies this parameter and it is not an empty array
   * then the iteration will include items related to accounts that belongs to
   * the specified shard prefixes.
   * Shard prefix must be represented as a string "workchain:prefix".
   * Where `workchain` is a signed integer and the `prefix` if a hexadecimal
   * representation if the 64-bit unsigned integer with tagged shard prefix.
   * For example: "0:3800000000000000".
   * Account address conforms to the shard filter if
   * it belongs to the filter workchain and the first bits of address match to
   * the shard prefix. Only transactions with suitable account addresses are iterated. Shard prefix filters.
   * @param accountsFilter Application can specify the list of accounts for which
   * it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates
   * transactions for all accounts that pass the shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter
   * if both are specified.
   * So it is an application responsibility to specify the correct filter combination. Account address filter.
   * @param result List of the fields that must be returned for iterated items.
   * This field is the same as the `result` parameter of
   * the `query_collection` function.
   * Note that iterated items can contain additional fields that are
   * not requested in the `result`. Projection (result) string.
   * @param includeTransfers If this parameter is `true` then each transaction contains field
   * `transfers` with list of transfer. See more about this structure in function description. Include `transfers` field in iterated transactions.
   */
  public static CompletableFuture<Net.RegisteredIterator> createTransactionIterator(int ctxId,
      Long startTime, Long endTime, String[] shardFilter, String[] accountsFilter, String result,
      Boolean includeTransfers) throws EverSdkException {
    return EverSdk.async(ctxId, "net.create_transaction_iterator", new Net.ParamsOfCreateTransactionIterator(startTime, endTime, shardFilter, accountsFilter, result, includeTransfers), Net.RegisteredIterator.class);
  }

  /**
   * The iterator stays exactly at the same position where the `resume_state` was caught.
   * Note that `resume_state` doesn't store the account filter. If the application requires
   * to use the same account filter as it was when the iterator was created then the application
   * must pass the account filter again in `accounts_filter` parameter.
   *
   * Application should call the `remove_iterator` when iterator is no longer required. Resumes transaction iterator.
   *
   * @param resumeState Same as value returned from `iterator_next`. Iterator state from which to resume.
   * @param accountsFilter Application can specify the list of accounts for which
   * it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates
   * transactions for all accounts that passes the shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter
   * if both are specified.
   * So it is the application's responsibility to specify the correct filter combination. Account address filter.
   */
  public static CompletableFuture<Net.RegisteredIterator> resumeTransactionIterator(int ctxId,
      JsonNode resumeState, String[] accountsFilter) throws EverSdkException {
    return EverSdk.async(ctxId, "net.resume_transaction_iterator", new Net.ParamsOfResumeTransactionIterator(resumeState, accountsFilter), Net.RegisteredIterator.class);
  }

  /**
   * In addition to available items this function returns the `has_more` flag
   * indicating that the iterator isn't reach the end of the iterated range yet.
   *
   * This function can return the empty list of available items but
   * indicates that there are more items is available.
   * This situation appears when the iterator doesn't reach iterated range
   * but database doesn't contains available items yet.
   *
   * If application requests resume state in `return_resume_state` parameter
   * then this function returns `resume_state` that can be used later to
   * resume the iteration from the position after returned items.
   *
   * The structure of the items returned depends on the iterator used.
   * See the description to the appropriated iterator creation function. Returns next available items.
   *
   * @param iterator  Iterator handle
   * @param limit If value is missing or is less than 1 the library uses 1. Maximum count of the returned items.
   * @param returnResumeState  Indicates that function must return the iterator state that can be used for resuming iteration.
   */
  public static CompletableFuture<Net.ResultOfIteratorNext> iteratorNext(int ctxId, Long iterator,
      Long limit, Boolean returnResumeState) throws EverSdkException {
    return EverSdk.async(ctxId, "net.iterator_next", new Net.ParamsOfIteratorNext(iterator, limit, returnResumeState), Net.ResultOfIteratorNext.class);
  }

  /**
   * Frees all resources allocated in library to serve iterator.
   *
   * Application always should call the `remove_iterator` when iterator
   * is no longer required. Removes an iterator
   */
  public static void removeIterator(int ctxId, Net.RegisteredIterator params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "net.remove_iterator", params);
  }

  /**
   *  Returns signature ID for configured network if it should be used in messages signature
   */
  public static CompletableFuture<Net.ResultOfGetSignatureId> getSignatureId(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "net.get_signature_id", null, Net.ResultOfGetSignatureId.class);
  }

  /**
   * @param signatureId  Signature ID for configured network if it should be used in messages signature
   */
  public record ResultOfGetSignatureId(Long signatureId) {
  }

  /**
   * @param field  Dot separated path to the field
   * @param fn  Aggregation function that must be applied to field values
   */
  public record FieldAggregation(String field, Net.AggregationFn fn) {
  }

  /**
   * @param startTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &gt;= `start_time`.
   * Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds. Starting time to iterate from.
   * @param endTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &lt; `end_time`.
   * Otherwise the iteration never stops.
   *
   * Must be specified in seconds. Optional end time to iterate for.
   * @param shardFilter If the application specifies this parameter and it is not an empty array
   * then the iteration will include items related to accounts that belongs to
   * the specified shard prefixes.
   * Shard prefix must be represented as a string "workchain:prefix".
   * Where `workchain` is a signed integer and the `prefix` if a hexadecimal
   * representation if the 64-bit unsigned integer with tagged shard prefix.
   * For example: "0:3800000000000000".
   * Account address conforms to the shard filter if
   * it belongs to the filter workchain and the first bits of address match to
   * the shard prefix. Only transactions with suitable account addresses are iterated. Shard prefix filters.
   * @param accountsFilter Application can specify the list of accounts for which
   * it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates
   * transactions for all accounts that pass the shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter
   * if both are specified.
   * So it is an application responsibility to specify the correct filter combination. Account address filter.
   * @param result List of the fields that must be returned for iterated items.
   * This field is the same as the `result` parameter of
   * the `query_collection` function.
   * Note that iterated items can contain additional fields that are
   * not requested in the `result`. Projection (result) string.
   * @param includeTransfers If this parameter is `true` then each transaction contains field
   * `transfers` with list of transfer. See more about this structure in function description. Include `transfers` field in iterated transactions.
   */
  public record ParamsOfCreateTransactionIterator(Long startTime, Long endTime,
      String[] shardFilter, String[] accountsFilter, String result, Boolean includeTransfers) {
  }

  /**
   * @param iterator  Iterator handle
   * @param limit If value is missing or is less than 1 the library uses 1. Maximum count of the returned items.
   * @param returnResumeState  Indicates that function must return the iterator state that can be used for resuming iteration.
   */
  public record ParamsOfIteratorNext(Long iterator, Long limit, Boolean returnResumeState) {
  }

  /**
   * @param results Returns an array of values. Each value corresponds to `queries` item. Result values for batched queries.
   */
  public record ResultOfBatchQuery(JsonNode[] results) {
  }

  public enum SortDirection {
    ASC,

    DESC
  }

  /**
   * @param result  Result provided by DAppServer.
   */
  public record ResultOfQuery(JsonNode result) {
  }

  /**
   * @param resumeState Same as value returned from `iterator_next`. Iterator state from which to resume.
   * @param accountsFilter Application can specify the list of accounts for which
   * it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates
   * transactions for all accounts that passes the shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter
   * if both are specified.
   * So it is the application's responsibility to specify the correct filter combination. Account address filter.
   */
  public record ParamsOfResumeTransactionIterator(JsonNode resumeState, String[] accountsFilter) {
  }

  /**
   * @param items Note that `iterator_next` can return an empty items and `has_more` equals to `true`.
   * In this case the application have to continue iteration.
   * Such situation can take place when there is no data yet but
   * the requested `end_time` is not reached. Next available items.
   * @param hasMore  Indicates that there are more available items in iterated range.
   * @param resumeState This field is returned only if the `return_resume_state` parameter
   * is specified.
   *
   * Note that `resume_state` corresponds to the iteration position
   * after the returned items. Optional iterator state that can be used for resuming iteration.
   */
  public record ResultOfIteratorNext(JsonNode[] items, Boolean hasMore, JsonNode resumeState) {
  }

  /**
   * @param handle Must be closed with `unsubscribe` Subscription handle.
   */
  public record ResultOfSubscribeCollection(Long handle) {
  }

  /**
   * @param address  Account address
   */
  public record ParamsOfFindLastShardBlock(String address) {
  }

  /**
   * @param messages  Messages.
   * @param transactions  Transactions.
   */
  public record ResultOfQueryTransactionTree(Net.MessageNode[] messages,
      Net.TransactionNode[] transactions) {
  }

  /**
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param order  Sorting order
   * @param limit  Number of documents to return
   */
  public record ParamsOfQueryCollection(String collection, JsonNode filter, String result,
      Net.OrderBy[] order, Long limit) implements ParamsOfQueryOperation {
    @JsonProperty("type")
    public String type() {
      return "QueryCollection";
    }
  }

  /**
   * @param handle Must be removed using `remove_iterator`
   * when it is no more needed for the application. Iterator handle.
   */
  public record RegisteredIterator(Long handle) {
  }

  /**
   * @param inMsg  Input message id.
   * @param abiRegistry  List of contract ABIs that will be used to decode message bodies. Library will try to decode each returned message body using any ABI from the registry.
   * @param timeout If some of the following messages and transactions are missing yet
   * The maximum waiting time is regulated by this option.
   *
   * Default value is 60000 (1 min). If `timeout` is set to 0 then function will wait infinitely
   * until the whole transaction tree is executed Timeout used to limit waiting time for the missing messages and transaction.
   * @param transactionMaxCount If transaction tree contains more transaction then this parameter then only first `transaction_max_count` transaction are awaited and returned.
   *
   * Default value is 50. If `transaction_max_count` is set to 0 then no limitation on
   * transaction count is used and all transaction are returned. Maximum transaction count to wait.
   */
  public record ParamsOfQueryTransactionTree(String inMsg, Abi.ABI[] abiRegistry, Long timeout,
      Long transactionMaxCount) {
  }

  /**
   * @param query  GraphQL query text.
   * @param variables Must be a map with named values that can be used in query. Variables used in query.
   */
  public record ParamsOfQuery(String query, JsonNode variables) {
  }

  /**
   * @param result  First found object that matches the provided criteria
   */
  public record ResultOfWaitForCollection(JsonNode result) {
  }

  /**
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param timeout  Query timeout
   */
  public record ParamsOfWaitForCollection(String collection, JsonNode filter, String result,
      Long timeout) implements ParamsOfQueryOperation {
    @JsonProperty("type")
    public String type() {
      return "WaitForCollection";
    }
  }

  /**
   * @param endpoints  List of endpoints provided by server
   */
  public record EndpointsSet(String[] endpoints) {
  }

  public enum AggregationFn {
    COUNT,

    MIN,

    MAX,

    SUM,

    AVERAGE
  }

  /**
   * @param subscription  GraphQL subscription text.
   * @param variables Must be a map with named values that can be used in query. Variables used in subscription.
   */
  public record ParamsOfSubscribe(String subscription, JsonNode variables) {
  }

  public record OrderBy(String path, Net.SortDirection direction) {
  }

  /**
   * @param id  Message id.
   * @param srcTransactionId This field is missing for an external inbound messages. Source transaction id.
   * @param dstTransactionId This field is missing for an external outbound messages. Destination transaction id.
   * @param src  Source address.
   * @param dst  Destination address.
   * @param value  Transferred tokens value.
   * @param bounce  Bounce flag.
   * @param decodedBody Library tries to decode message body using provided `params.abi_registry`.
   * This field will be missing if none of the provided abi can be used to decode. Decoded body.
   */
  public record MessageNode(String id, String srcTransactionId, String dstTransactionId, String src,
      String dst, String value, Boolean bounce, Abi.DecodedMessageBody decodedBody) {
  }

  /**
   * @param blockId  Account shard last block ID
   */
  public record ResultOfFindLastShardBlock(String blockId) {
  }

  /**
   * @param startTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &gt;= `start_time`.
   * Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds. Starting time to iterate from.
   * @param endTime If the application specifies this parameter then the iteration
   * includes blocks with `gen_utime` &lt; `end_time`.
   * Otherwise the iteration never stops.
   *
   * Must be specified in seconds. Optional end time to iterate for.
   * @param shardFilter If the application specifies this parameter and it is not the empty array
   * then the iteration will include items related to accounts that belongs to
   * the specified shard prefixes.
   * Shard prefix must be represented as a string "workchain:prefix".
   * Where `workchain` is a signed integer and the `prefix` if a hexadecimal
   * representation if the 64-bit unsigned integer with tagged shard prefix.
   * For example: "0:3800000000000000". Shard prefix filter.
   * @param result List of the fields that must be returned for iterated items.
   * This field is the same as the `result` parameter of
   * the `query_collection` function.
   * Note that iterated items can contains additional fields that are
   * not requested in the `result`. Projection (result) string.
   */
  public record ParamsOfCreateBlockIterator(Long startTime, Long endTime, String[] shardFilter,
      String result) {
  }

  /**
   * @param account  Account address
   * @param result  Projection (result) string
   * @param first  Number of counterparties to return
   * @param after  `cursor` field of the last received result
   */
  public record ParamsOfQueryCounterparties(String account, String result, Long first,
      String after) implements ParamsOfQueryOperation {
    @JsonProperty("type")
    public String type() {
      return "QueryCounterparties";
    }
  }

  /**
   * @param values Returns an array of strings. Each string refers to the corresponding `fields` item.
   * Numeric value is returned as a decimal string representations. Values for requested fields.
   */
  public record ResultOfAggregateCollection(JsonNode values) {
  }

  /**
   * @param result  Objects that match the provided criteria
   */
  public record ResultOfQueryCollection(JsonNode[] result) {
  }

  /**
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   */
  public record ParamsOfSubscribeCollection(String collection, JsonNode filter, String result) {
  }

  /**
   * @param query  Current query endpoint
   * @param endpoints  List of all endpoints used by client
   */
  public record ResultOfGetEndpoints(String query, String[] endpoints) {
  }

  /**
   * @param operations  List of query operations that must be performed per single fetch.
   */
  public record ParamsOfBatchQuery(Net.ParamsOfQueryOperation[] operations) {
  }

  /**
   * @param resumeState Same as value returned from `iterator_next`. Iterator state from which to resume.
   */
  public record ParamsOfResumeBlockIterator(JsonNode resumeState) {
  }

  public sealed interface ParamsOfQueryOperation {
  }

  /**
   * @param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param fields  Projection (result) string
   */
  public record ParamsOfAggregateCollection(String collection, JsonNode filter,
      Net.FieldAggregation[] fields) implements ParamsOfQueryOperation {
    @JsonProperty("type")
    public String type() {
      return "AggregateCollection";
    }
  }

  public enum NetErrorCode {
    QueryFailed(601),

    SubscribeFailed(602),

    WaitForFailed(603),

    GetSubscriptionResultFailed(604),

    InvalidServerResponse(605),

    ClockOutOfSync(606),

    WaitForTimeout(607),

    GraphqlError(608),

    NetworkModuleSuspended(609),

    WebsocketDisconnected(610),

    NotSupported(611),

    NoEndpointsProvided(612),

    GraphqlWebsocketInitError(613),

    NetworkModuleResumed(614),

    Unauthorized(615),

    QueryTransactionTreeTimeout(616),

    GraphqlConnectionError(617),

    WrongWebscoketProtocolSequence(618);

    private final Integer value;

    NetErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param id  Transaction id.
   * @param inMsg  In message id.
   * @param outMsgs  Out message ids.
   * @param accountAddr  Account address.
   * @param totalFees  Transactions total fees.
   * @param aborted  Aborted flag.
   * @param exitCode  Compute phase exit code.
   */
  public record TransactionNode(String id, String inMsg, String[] outMsgs, String accountAddr,
      String totalFees, Boolean aborted, Long exitCode) {
  }
}
