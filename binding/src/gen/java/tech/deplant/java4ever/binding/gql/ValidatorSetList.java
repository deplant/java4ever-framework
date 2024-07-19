package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record ValidatorSetList(String adnl_addr, String public_key, String weight) {
  public static QueryExecutorBuilder weight(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("weight", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
