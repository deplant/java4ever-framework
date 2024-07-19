package tech.deplant.java4ever.binding.gql;

public record BlockAccountBlocksFilter(StringFilter account_addr, StringFilter new_hash,
    StringFilter old_hash, IntFilter tr_count,
    BlockAccountBlocksTransactionsArrayFilter transactions, BlockAccountBlocksFilter OR) {
}
