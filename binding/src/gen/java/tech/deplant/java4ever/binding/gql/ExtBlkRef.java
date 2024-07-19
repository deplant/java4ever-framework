package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record ExtBlkRef(String end_lt, String file_hash, String root_hash, Float seq_no) {
  public static QueryExecutorBuilder end_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("end_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
