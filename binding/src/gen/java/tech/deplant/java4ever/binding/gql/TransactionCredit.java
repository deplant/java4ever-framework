package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record TransactionCredit(String credit, List<OtherCurrency> credit_other,
    String due_fees_collected) {
  public static QueryExecutorBuilder credit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("credit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder due_fees_collected(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("due_fees_collected", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
