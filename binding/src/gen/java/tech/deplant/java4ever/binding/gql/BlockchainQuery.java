package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record BlockchainQuery(BlockchainAccountQuery account, Node.BlockchainBlock block,
    Node.BlockchainBlock block_by_seq_no, Node.BlockchainTransaction transaction,
    Node.BlockchainMessage message, BlockchainMasterSeqNoRange master_seq_no_range,
    BlockchainBlocksConnection key_blocks, BlockchainBlocksConnection blocks,
    BlockchainTransactionsConnection transactions) {
  public static QueryExecutorBuilder account(String objectFieldsTree, String address) {
    var builder = new QueryExecutorBuilder("account", objectFieldsTree);
    Optional.ofNullable(address).ifPresent(ar -> builder.addToQuery("address",ar));
    return builder;
  }

  public static QueryExecutorBuilder block(String objectFieldsTree, String hash) {
    var builder = new QueryExecutorBuilder("block", objectFieldsTree);
    Optional.ofNullable(hash).ifPresent(ar -> builder.addToQuery("hash",ar));
    return builder;
  }

  public static QueryExecutorBuilder block_by_seq_no(String objectFieldsTree, Integer workchain,
      String thread, Float seq_no) {
    var builder = new QueryExecutorBuilder("block_by_seq_no", objectFieldsTree);
    Optional.ofNullable(workchain).ifPresent(ar -> builder.addToQuery("workchain",ar));
    Optional.ofNullable(thread).ifPresent(ar -> builder.addToQuery("thread",ar));
    Optional.ofNullable(seq_no).ifPresent(ar -> builder.addToQuery("seq_no",ar));
    return builder;
  }

  public static QueryExecutorBuilder transaction(String objectFieldsTree, String hash) {
    var builder = new QueryExecutorBuilder("transaction", objectFieldsTree);
    Optional.ofNullable(hash).ifPresent(ar -> builder.addToQuery("hash",ar));
    return builder;
  }

  public static QueryExecutorBuilder message(String objectFieldsTree, String hash) {
    var builder = new QueryExecutorBuilder("message", objectFieldsTree);
    Optional.ofNullable(hash).ifPresent(ar -> builder.addToQuery("hash",ar));
    return builder;
  }

  public static QueryExecutorBuilder master_seq_no_range(String objectFieldsTree,
      Integer time_start, Integer time_end) {
    var builder = new QueryExecutorBuilder("master_seq_no_range", objectFieldsTree);
    Optional.ofNullable(time_start).ifPresent(ar -> builder.addToQuery("time_start",ar));
    Optional.ofNullable(time_end).ifPresent(ar -> builder.addToQuery("time_end",ar));
    return builder;
  }

  public static QueryExecutorBuilder key_blocks(String objectFieldsTree,
      Boolean allow_latest_inconsistent_data, BlockchainMasterSeqNoFilter master_seq_no_range,
      Integer first, String after, Integer last, String before) {
    var builder = new QueryExecutorBuilder("key_blocks", objectFieldsTree);
    Optional.ofNullable(allow_latest_inconsistent_data).ifPresent(ar -> builder.addToQuery("allow_latest_inconsistent_data",ar));
    Optional.ofNullable(master_seq_no_range).ifPresent(ar -> builder.addToQuery("master_seq_no_range",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(last).ifPresent(ar -> builder.addToQuery("last",ar));
    Optional.ofNullable(before).ifPresent(ar -> builder.addToQuery("before",ar));
    return builder;
  }

  public static QueryExecutorBuilder blocks(String objectFieldsTree,
      Boolean allow_latest_inconsistent_data, BlockchainMasterSeqNoFilter master_seq_no_range,
      Integer workchain, String thread, Integer min_tr_count, Integer max_tr_count, Integer first,
      String after, Integer last, String before) {
    var builder = new QueryExecutorBuilder("blocks", objectFieldsTree);
    Optional.ofNullable(allow_latest_inconsistent_data).ifPresent(ar -> builder.addToQuery("allow_latest_inconsistent_data",ar));
    Optional.ofNullable(master_seq_no_range).ifPresent(ar -> builder.addToQuery("master_seq_no_range",ar));
    Optional.ofNullable(workchain).ifPresent(ar -> builder.addToQuery("workchain",ar));
    Optional.ofNullable(thread).ifPresent(ar -> builder.addToQuery("thread",ar));
    Optional.ofNullable(min_tr_count).ifPresent(ar -> builder.addToQuery("min_tr_count",ar));
    Optional.ofNullable(max_tr_count).ifPresent(ar -> builder.addToQuery("max_tr_count",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(last).ifPresent(ar -> builder.addToQuery("last",ar));
    Optional.ofNullable(before).ifPresent(ar -> builder.addToQuery("before",ar));
    return builder;
  }

  public static QueryExecutorBuilder transactions(String objectFieldsTree,
      Boolean allow_latest_inconsistent_data, BlockchainMasterSeqNoFilter master_seq_no_range,
      Integer workchain, String min_balance_delta, String max_balance_delta, Integer first,
      String after, Integer last, String before) {
    var builder = new QueryExecutorBuilder("transactions", objectFieldsTree);
    Optional.ofNullable(allow_latest_inconsistent_data).ifPresent(ar -> builder.addToQuery("allow_latest_inconsistent_data",ar));
    Optional.ofNullable(master_seq_no_range).ifPresent(ar -> builder.addToQuery("master_seq_no_range",ar));
    Optional.ofNullable(workchain).ifPresent(ar -> builder.addToQuery("workchain",ar));
    Optional.ofNullable(min_balance_delta).ifPresent(ar -> builder.addToQuery("min_balance_delta",ar));
    Optional.ofNullable(max_balance_delta).ifPresent(ar -> builder.addToQuery("max_balance_delta",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(last).ifPresent(ar -> builder.addToQuery("last",ar));
    Optional.ofNullable(before).ifPresent(ar -> builder.addToQuery("before",ar));
    return builder;
  }
}
