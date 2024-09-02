package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record OtherCurrency(Float currency, String value) {
  public static QueryExecutorBuilder value(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("value", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
