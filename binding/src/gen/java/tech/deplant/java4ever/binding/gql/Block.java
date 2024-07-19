package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * This is Block;
 */
public record Block(String id, List<BlockAccountBlocks> account_blocks, Boolean after_merge,
    Boolean after_split, Boolean before_split, String boc, String chain_order, String created_by,
    String end_lt, String file_hash, Integer flags, Float gen_catchain_seqno,
    String gen_software_capabilities, Float gen_software_version, Float gen_utime,
    String gen_utime_string, Float gen_validator_list_hash_short, Integer global_id,
    List<InMsg> in_msg_descr, Boolean key_block, BlockMaster master, ExtBlkRef master_ref,
    Float min_ref_mc_seqno, List<OutMsg> out_msg_descr, ExtBlkRef prev_alt_ref,
    Float prev_key_block_seqno, ExtBlkRef prev_ref, ExtBlkRef prev_vert_alt_ref,
    ExtBlkRef prev_vert_ref, String rand_seed, Float seq_no, String shard,
    BlockSignatures signatures, String start_lt, BlockStateUpdate state_update, Integer status,
    BlockProcessingStatusEnum status_name, Integer tr_count, BlockValueFlow value_flow,
    Float version, Float vert_seq_no, Boolean want_merge, Boolean want_split,
    Integer workchain_id) {
  public static QueryExecutorBuilder end_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("end_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gen_software_capabilities(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gen_software_capabilities", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder signatures(String objectFieldsTree, Integer timeout,
      BlockFilter when) {
    var builder = new QueryExecutorBuilder("signatures", objectFieldsTree);
    Optional.ofNullable(timeout).ifPresent(ar -> builder.addToQuery("timeout",ar));
    Optional.ofNullable(when).ifPresent(ar -> builder.addToQuery("when",ar));
    return builder;
  }

  public static QueryExecutorBuilder start_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("start_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
