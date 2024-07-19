package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record InMsg(String fwd_fee, String ihr_fee, MsgEnvelope in_msg, String msg_id,
    Integer msg_type, InMsgTypeEnum msg_type_name, MsgEnvelope out_msg, String proof_created,
    String proof_delivered, String transaction_id, String transit_fee) {
  public static QueryExecutorBuilder fwd_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fwd_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder ihr_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("ihr_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder transit_fee(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("transit_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
