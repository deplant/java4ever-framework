package tech.deplant.java4ever.binding.gql;

public record BlockMasterFilter(ConfigFilter config, StringFilter config_addr,
    FloatFilter max_shard_gen_utime, FloatFilter min_shard_gen_utime,
    BlockMasterPrevBlkSignaturesArrayFilter prev_blk_signatures, InMsgFilter recover_create_msg,
    BlockMasterShardFeesArrayFilter shard_fees, BlockMasterShardHashesArrayFilter shard_hashes,
    BlockMasterFilter OR) {
}
