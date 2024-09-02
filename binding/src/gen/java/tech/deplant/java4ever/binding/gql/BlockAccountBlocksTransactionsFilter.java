package tech.deplant.java4ever.binding.gql;

public record BlockAccountBlocksTransactionsFilter(StringFilter lt, StringFilter total_fees,
    OtherCurrencyArrayFilter total_fees_other, StringFilter transaction_id,
    BlockAccountBlocksTransactionsFilter OR) {
}
