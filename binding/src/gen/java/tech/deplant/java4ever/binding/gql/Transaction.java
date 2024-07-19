package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * TON Transaction;
 */
public record Transaction(String id, Boolean aborted, Account account, String account_addr,
    TransactionAction action, String balance_delta, List<OtherCurrency> balance_delta_other,
    Block block, String block_id, String boc, TransactionBounce bounce, String chain_order,
    TransactionCompute compute, TransactionCredit credit, Boolean credit_first, Boolean destroyed,
    Integer end_status, AccountStatusEnum end_status_name, String ext_in_msg_fee,
    Message in_message, String in_msg, Boolean installed, String lt, String new_hash, Float now,
    String now_string, String old_hash, Integer orig_status, AccountStatusEnum orig_status_name,
    List<Message> out_messages, List<String> out_msgs, Integer outmsg_cnt,
    String prepare_transaction, String prev_trans_hash, String prev_trans_lt, String proof,
    TransactionSplitInfo split_info, Integer status, TransactionProcessingStatusEnum status_name,
    TransactionStorage storage, String total_fees, List<OtherCurrency> total_fees_other,
    Integer tr_type, TransactionTypeEnum tr_type_name, String tt, Integer workchain_id) {
  public static QueryExecutorBuilder account(String objectFieldsTree, Integer timeout,
      TransactionFilter when) {
    var builder = new QueryExecutorBuilder("account", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder balance_delta(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("balance_delta", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder block(String objectFieldsTree, Integer timeout,
      TransactionFilter when) {
    var builder = new QueryExecutorBuilder("block", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder ext_in_msg_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("ext_in_msg_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder in_message(String objectFieldsTree, Integer timeout,
      TransactionFilter when) {
    var builder = new QueryExecutorBuilder("in_message", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder out_messages(String objectFieldsTree, Integer timeout,
      TransactionFilter when) {
    var builder = new QueryExecutorBuilder("out_messages", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder prev_trans_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("prev_trans_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder total_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
