package tech.deplant.java4ever.binding.gql;

public record TransactionSplitInfo(Integer acc_split_depth, Integer cur_shard_pfx_len,
    String sibling_addr, String this_addr) {
}
