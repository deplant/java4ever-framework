package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record MsgForwardPrices(String bit_price, String cell_price, Integer first_frac,
    Float ihr_price_factor, String lump_price, Integer next_frac) {
  public static QueryExecutorBuilder bit_price(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("bit_price", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder cell_price(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("cell_price", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder lump_price(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("lump_price", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
