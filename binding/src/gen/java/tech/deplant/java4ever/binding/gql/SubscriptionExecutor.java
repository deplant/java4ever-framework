package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

class SubscriptionExecutor {
  public static QueryExecutorBuilder accounts(String objectFieldsTree, AccountFilter filter,
      String accessKey) {
    var builder = new QueryExecutorBuilder("accounts", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder transactions(String objectFieldsTree, TransactionFilter filter,
      String accessKey) {
    var builder = new QueryExecutorBuilder("transactions", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder messages(String objectFieldsTree, MessageFilter filter,
      String accessKey) {
    var builder = new QueryExecutorBuilder("messages", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder blocks(String objectFieldsTree, BlockFilter filter,
      String accessKey) {
    var builder = new QueryExecutorBuilder("blocks", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder blocks_signatures(String objectFieldsTree,
      BlockSignaturesFilter filter, String accessKey) {
    var builder = new QueryExecutorBuilder("blocks_signatures", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder zerostates(String objectFieldsTree, ZerostateFilter filter,
      String accessKey) {
    var builder = new QueryExecutorBuilder("zerostates", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder counterparties(String objectFieldsTree,
      CounterpartyFilter filter, String accessKey) {
    var builder = new QueryExecutorBuilder("counterparties", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder rempReceipts(String objectFieldsTree, String messageId) {
    var builder = new QueryExecutorBuilder("rempReceipts", objectFieldsTree);
    Optional.ofNullable(messageId).ifPresent(ar -> builder.addToQuery("messageId",ar));
    return builder;
  }
}
