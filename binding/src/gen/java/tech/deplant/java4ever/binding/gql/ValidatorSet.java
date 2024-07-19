package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record ValidatorSet(List<ValidatorSetList> list, Integer main, Integer total,
    String total_weight, Float utime_since, String utime_since_string, Float utime_until,
    String utime_until_string) {
  public static QueryExecutorBuilder total_weight(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_weight", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
