package tech.deplant.java4ever.binding.gql;

public record BlockValueFlowFilter(StringFilter created, OtherCurrencyArrayFilter created_other,
    StringFilter exported, OtherCurrencyArrayFilter exported_other, StringFilter fees_collected,
    OtherCurrencyArrayFilter fees_collected_other, StringFilter fees_imported,
    OtherCurrencyArrayFilter fees_imported_other, StringFilter from_prev_blk,
    OtherCurrencyArrayFilter from_prev_blk_other, StringFilter imported,
    OtherCurrencyArrayFilter imported_other, StringFilter minted,
    OtherCurrencyArrayFilter minted_other, StringFilter to_next_blk,
    OtherCurrencyArrayFilter to_next_blk_other, BlockValueFlowFilter OR) {
}
