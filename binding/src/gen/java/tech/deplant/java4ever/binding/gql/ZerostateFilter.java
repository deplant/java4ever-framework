package tech.deplant.java4ever.binding.gql;

/**
 * The initial state of the workchain before first block was generated;
 */
public record ZerostateFilter(StringFilter id, ZerostateAccountsArrayFilter accounts,
    StringFilter boc, StringFilter file_hash, IntFilter global_id,
    ZerostateLibrariesArrayFilter libraries, ZerostateMasterFilter master, StringFilter root_hash,
    StringFilter total_balance, OtherCurrencyArrayFilter total_balance_other,
    IntFilter workchain_id, ZerostateFilter OR) {
}
