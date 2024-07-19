package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record ConfigP18(String bit_price_ps, String cell_price_ps, String mc_bit_price_ps,
    String mc_cell_price_ps, Float utime_since, String utime_since_string) {
  public static QueryExecutorBuilder bit_price_ps(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("bit_price_ps", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder cell_price_ps(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("cell_price_ps", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder mc_bit_price_ps(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("mc_bit_price_ps", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder mc_cell_price_ps(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("mc_cell_price_ps", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
