package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record OutMsg(String import_block_lt, InMsg imported, String msg_env_hash, String msg_id,
    Integer msg_type, OutMsgTypeEnum msg_type_name, String next_addr_pfx, Integer next_workchain,
    MsgEnvelope out_msg, InMsg reimport, String transaction_id) {
  public static QueryExecutorBuilder import_block_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("import_block_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder next_addr_pfx(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("next_addr_pfx", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
