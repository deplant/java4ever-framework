package tech.deplant.java4ever.binding.gql;

public record GasLimitsPricesFilter(StringFilter block_gas_limit, StringFilter delete_due_limit,
    StringFilter flat_gas_limit, StringFilter flat_gas_price, StringFilter freeze_due_limit,
    StringFilter gas_credit, StringFilter gas_limit, StringFilter gas_price,
    StringFilter special_gas_limit, GasLimitsPricesFilter OR) {
}
