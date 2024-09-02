package tech.deplant.java4ever.binding.gql;

public record BlockMasterShardFeesFilter(StringFilter create, OtherCurrencyArrayFilter create_other,
    StringFilter fees, OtherCurrencyArrayFilter fees_other, StringFilter shard,
    IntFilter workchain_id, BlockMasterShardFeesFilter OR) {
}
