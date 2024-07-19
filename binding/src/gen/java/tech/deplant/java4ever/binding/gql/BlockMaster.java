package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record BlockMaster(Config config, String config_addr, Float max_shard_gen_utime,
    String max_shard_gen_utime_string, Float min_shard_gen_utime, String min_shard_gen_utime_string,
    List<BlockMasterPrevBlkSignatures> prev_blk_signatures, InMsg recover_create_msg,
    List<BlockMasterShardFees> shard_fees, List<BlockMasterShardHashes> shard_hashes) {
}
