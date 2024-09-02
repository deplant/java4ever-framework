package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

class QueryExecutor {
  public static QueryExecutorBuilder accounts(String objectFieldsTree, AccountFilter filter,
      List<QueryOrderBy> orderBy, Integer limit, Float timeout, String accessKey,
      String operationId) {
    var builder = new QueryExecutorBuilder("accounts", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder transactions(String objectFieldsTree, TransactionFilter filter,
      List<QueryOrderBy> orderBy, Integer limit, Float timeout, String accessKey,
      String operationId) {
    var builder = new QueryExecutorBuilder("transactions", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder messages(String objectFieldsTree, MessageFilter filter,
      List<QueryOrderBy> orderBy, Integer limit, Float timeout, String accessKey,
      String operationId) {
    var builder = new QueryExecutorBuilder("messages", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder blocks(String objectFieldsTree, BlockFilter filter,
      List<QueryOrderBy> orderBy, Integer limit, Float timeout, String accessKey,
      String operationId) {
    var builder = new QueryExecutorBuilder("blocks", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder blocks_signatures(String objectFieldsTree,
      BlockSignaturesFilter filter, List<QueryOrderBy> orderBy, Integer limit, Float timeout,
      String accessKey, String operationId) {
    var builder = new QueryExecutorBuilder("blocks_signatures", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder zerostates(String objectFieldsTree, ZerostateFilter filter,
      List<QueryOrderBy> orderBy, Integer limit, Float timeout, String accessKey,
      String operationId) {
    var builder = new QueryExecutorBuilder("zerostates", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(orderBy).ifPresent(ar -> builder.addToQuery("orderBy",ar));
    Optional.ofNullable(limit).ifPresent(ar -> builder.addToQuery("limit",ar));
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    Optional.ofNullable(operationId).ifPresent(ar -> builder.addToQuery("operationId",ar));
    return builder;
  }

  public static QueryExecutorBuilder counterparties(String objectFieldsTree, String account,
      Integer first, String after, String accessKey) {
    var builder = new QueryExecutorBuilder("counterparties", objectFieldsTree);
    Optional.ofNullable(account).ifPresent(ar -> builder.addToQuery("account",ar));
    Optional.ofNullable(first).ifPresent(ar -> builder.addToQuery("first",ar));
    Optional.ofNullable(after).ifPresent(ar -> builder.addToQuery("after",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder aggregateAccounts(String objectFieldsTree,
      AccountFilter filter, List<FieldAggregation> fields, String accessKey) {
    var builder = new QueryExecutorBuilder("aggregateAccounts", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(fields).ifPresent(ar -> builder.addToQuery("fields",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder aggregateTransactions(String objectFieldsTree,
      TransactionFilter filter, List<FieldAggregation> fields, String accessKey) {
    var builder = new QueryExecutorBuilder("aggregateTransactions", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(fields).ifPresent(ar -> builder.addToQuery("fields",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder aggregateMessages(String objectFieldsTree,
      MessageFilter filter, List<FieldAggregation> fields, String accessKey) {
    var builder = new QueryExecutorBuilder("aggregateMessages", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(fields).ifPresent(ar -> builder.addToQuery("fields",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder aggregateBlocks(String objectFieldsTree, BlockFilter filter,
      List<FieldAggregation> fields, String accessKey) {
    var builder = new QueryExecutorBuilder("aggregateBlocks", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(fields).ifPresent(ar -> builder.addToQuery("fields",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder aggregateBlockSignatures(String objectFieldsTree,
      BlockSignaturesFilter filter, List<FieldAggregation> fields, String accessKey) {
    var builder = new QueryExecutorBuilder("aggregateBlockSignatures", objectFieldsTree);
    Optional.ofNullable(filter).ifPresent(ar -> builder.addToQuery("filter",ar));
    Optional.ofNullable(fields).ifPresent(ar -> builder.addToQuery("fields",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }
}
