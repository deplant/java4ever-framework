package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record TransactionBounce(Integer bounce_type, BounceTypeEnum bounce_type_name,
    String fwd_fees, String msg_fees, Float msg_size_bits, Float msg_size_cells,
    String req_fwd_fees) {
  public static QueryExecutorBuilder fwd_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fwd_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder msg_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("msg_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder req_fwd_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("req_fwd_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
