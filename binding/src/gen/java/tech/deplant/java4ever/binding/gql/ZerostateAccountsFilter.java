package tech.deplant.java4ever.binding.gql;

public record ZerostateAccountsFilter(StringFilter id, IntFilter acc_type,
    AccountStatusEnumFilter acc_type_name, StringFilter balance,
    OtherCurrencyArrayFilter balance_other, StringFilter bits, StringFilter boc, StringFilter cells,
    StringFilter code, StringFilter code_hash, StringFilter data, StringFilter data_hash,
    StringFilter due_payment, StringFilter init_code_hash, FloatFilter last_paid,
    StringFilter last_trans_lt, StringFilter library, StringFilter library_hash,
    StringFilter prev_code_hash, StringFilter proof, StringFilter public_cells,
    IntFilter split_depth, StringFilter state_hash, BooleanFilter tick, BooleanFilter tock,
    IntFilter workchain_id, ZerostateAccountsFilter OR) {
}
