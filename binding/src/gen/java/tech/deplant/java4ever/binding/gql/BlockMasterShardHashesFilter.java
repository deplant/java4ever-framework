package tech.deplant.java4ever.binding.gql;

public record BlockMasterShardHashesFilter(BlockMasterShardHashesDescrFilter descr,
    StringFilter shard, IntFilter workchain_id, BlockMasterShardHashesFilter OR) {
}
