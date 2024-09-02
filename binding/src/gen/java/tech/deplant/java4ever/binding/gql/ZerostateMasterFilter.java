package tech.deplant.java4ever.binding.gql;

public record ZerostateMasterFilter(ConfigFilter config, StringFilter config_addr,
    StringFilter global_balance, OtherCurrencyArrayFilter global_balance_other,
    FloatFilter validator_list_hash_short, ZerostateMasterFilter OR) {
}
