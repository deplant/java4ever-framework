package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * Set of validator's signatures for the Block with correspond id;
 */
public record BlockSignatures(String id, Block block, Float catchain_seqno, Float gen_utime,
    String gen_utime_string, String proof, Float seq_no, String shard, String sig_weight,
    List<BlockSignaturesSignatures> signatures, Float validator_list_hash_short,
    Integer workchain_id) {
  public static QueryExecutorBuilder block(String objectFieldsTree, Integer timeout,
      BlockSignaturesFilter when) {
    var builder = new QueryExecutorBuilder("block", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder sig_weight(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("sig_weight", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
