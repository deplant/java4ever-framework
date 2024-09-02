package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record BlockAccountBlocksTransactions(String lt, String total_fees,
    List<OtherCurrency> total_fees_other, String transaction_id) {
  public static QueryExecutorBuilder lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder total_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
