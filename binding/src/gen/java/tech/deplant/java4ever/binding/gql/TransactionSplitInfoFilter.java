package tech.deplant.java4ever.binding.gql;

public record TransactionSplitInfoFilter(IntFilter acc_split_depth, IntFilter cur_shard_pfx_len,
    StringFilter sibling_addr, StringFilter this_addr, TransactionSplitInfoFilter OR) {
}
