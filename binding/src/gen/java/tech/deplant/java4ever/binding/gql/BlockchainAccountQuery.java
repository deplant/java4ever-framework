package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record BlockchainAccountQuery(Node.BlockchainAccount info,
    BlockchainTransactionsConnection transactions, BlockchainMessagesConnection messages) {
  public static QueryExecutorBuilder transactions(String objectFieldsTree,
      Boolean allow_latest_inconsistent_data, BlockchainMasterSeqNoFilter master_seq_no_range,
      Boolean aborted, String min_balance_delta, String max_balance_delta, Integer first,
      String after, Integer last, String before) {
    var builder = new QueryExecutorBuilder("transactions", objectFieldsTree);
    Optional.ofNullable(allow_latest_inconsistent_data).ifPresent(ar -> builder.addToQuery("allow_latest_inconsistent_data",ar));
    Optional.ofNullable(master_seq_no_range).ifPresent(ar -> builder.addToQuery("master_seq_no_range",ar));
    Optional.ofNullable(aborted).ifPresent(ar -> builder.addToQuery("aborted",ar));
    Optional.ofNullable(min_balance_delta).ifPresent(ar -> builder.addToQuery("min_balance_delta",ar));
    Optional.ofNullable(max_balance_delta).ifPresent(ar -> builder.addToQuery("max_balance_delta",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(last).ifPresent(ar -> builder.addToQuery("last",ar));
    Optional.ofNullable(before).ifPresent(ar -> builder.addToQuery("before",ar));
    return builder;
  }

  public static QueryExecutorBuilder messages(String objectFieldsTree,
      Boolean allow_latest_inconsistent_data, BlockchainMasterSeqNoFilter master_seq_no_range,
      List<String> counterparties, List<BlockchainMessageTypeFilterEnum> msg_type, String min_value,
      Integer first, String after, Integer last, String before) {
    var builder = new QueryExecutorBuilder("messages", objectFieldsTree);
    Optional.ofNullable(allow_latest_inconsistent_data).ifPresent(ar -> builder.addToQuery("allow_latest_inconsistent_data",ar));
    Optional.ofNullable(master_seq_no_range).ifPresent(ar -> builder.addToQuery("master_seq_no_range",ar));
    Optional.ofNullable(counterparties).ifPresent(ar -> builder.addToQuery("counterparties",ar));
    Optional.ofNullable(msg_type).ifPresent(ar -> builder.addToQuery("msg_type",ar));
    Optional.ofNullable(min_value).ifPresent(ar -> builder.addToQuery("min_value",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(last).ifPresent(ar -> builder.addToQuery("last",ar));
    Optional.ofNullable(before).ifPresent(ar -> builder.addToQuery("before",ar));
    return builder;
  }
}
