package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record GasLimitsPrices(String block_gas_limit, String delete_due_limit,
    String flat_gas_limit, String flat_gas_price, String freeze_due_limit, String gas_credit,
    String gas_limit, String gas_price, String special_gas_limit) {
  public static QueryExecutorBuilder block_gas_limit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("block_gas_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder delete_due_limit(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("delete_due_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder flat_gas_limit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("flat_gas_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder flat_gas_price(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("flat_gas_price", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder freeze_due_limit(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("freeze_due_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gas_credit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_credit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gas_limit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gas_price(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_price", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder special_gas_limit(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("special_gas_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
