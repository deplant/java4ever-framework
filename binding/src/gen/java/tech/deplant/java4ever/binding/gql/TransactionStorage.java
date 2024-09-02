package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record TransactionStorage(Integer status_change, AccountStatusChangeEnum status_change_name,
    String storage_fees_collected, String storage_fees_due) {
  public static QueryExecutorBuilder storage_fees_collected(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("storage_fees_collected", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder storage_fees_due(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("storage_fees_due", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
