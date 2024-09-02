package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record ConfigP42(List<ConfigP42Payouts> payouts, String threshold) {
  public static QueryExecutorBuilder threshold(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("threshold", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
