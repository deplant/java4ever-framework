package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * Shard description;
 */
public record BlockMasterShardHashesDescr(Boolean before_merge, Boolean before_split, String end_lt,
    String fees_collected, List<OtherCurrency> fees_collected_other, String file_hash,
    Integer flags, String funds_created, List<OtherCurrency> funds_created_other, Float gen_utime,
    String gen_utime_string, Float min_ref_mc_seqno, Float next_catchain_seqno,
    String next_validator_shard, Boolean nx_cc_updated, Float reg_mc_seqno, String root_hash,
    Float seq_no, Float split, Integer split_type, SplitTypeEnum split_type_name, String start_lt,
    Boolean want_merge, Boolean want_split) {
  public static QueryExecutorBuilder end_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("end_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder fees_collected(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fees_collected", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder funds_created(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("funds_created", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder start_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("start_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
