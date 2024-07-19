package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record MsgEnvelope(String cur_addr, String fwd_fee_remaining, String msg_id,
    String next_addr) {
  public static QueryExecutorBuilder fwd_fee_remaining(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fwd_fee_remaining", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
