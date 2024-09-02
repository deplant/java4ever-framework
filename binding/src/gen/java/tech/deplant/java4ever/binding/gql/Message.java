package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * # Message type
     *
     * Message layout queries.  A message consists of its header followed by its
     * body or payload. The body is essentially arbitrary, to be interpreted by the
     * destination smart contract. It can be queried with the following fields:;
 */
public record Message(String id, Block block, String block_id, String boc, String body,
    String body_hash, Boolean bounce, Boolean bounced, String chain_order, String code,
    String code_hash, Float created_at, String created_at_string, String created_lt, String data,
    String data_hash, String dst, Account dst_account, Transaction dst_transaction,
    Integer dst_workchain_id, String fwd_fee, Boolean ihr_disabled, String ihr_fee,
    String import_fee, String library, String library_hash, Integer msg_type,
    MessageTypeEnum msg_type_name, String proof, Integer split_depth, String src,
    Account src_account, Transaction src_transaction, Integer src_workchain_id, Integer status,
    MessageProcessingStatusEnum status_name, Boolean tick, Boolean tock, String value,
    List<OtherCurrency> value_other) {
  public static QueryExecutorBuilder block(String objectFieldsTree, Integer timeout,
      MessageFilter when) {
    var builder = new QueryExecutorBuilder("block", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder created_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("created_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder dst_account(String objectFieldsTree, Integer timeout,
      MessageFilter when) {
    var builder = new QueryExecutorBuilder("dst_account", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder dst_transaction(String objectFieldsTree, Integer timeout,
      MessageFilter when) {
    var builder = new QueryExecutorBuilder("dst_transaction", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder fwd_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fwd_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder ihr_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("ihr_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder import_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("import_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder src_account(String objectFieldsTree, Integer timeout,
      MessageFilter when) {
    var builder = new QueryExecutorBuilder("src_account", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder src_transaction(String objectFieldsTree, Integer timeout,
      MessageFilter when) {
    var builder = new QueryExecutorBuilder("src_transaction", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder value(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("value", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
