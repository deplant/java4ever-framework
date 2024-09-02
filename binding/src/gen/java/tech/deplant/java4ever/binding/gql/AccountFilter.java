package tech.deplant.java4ever.binding.gql;

/**
 * # Account type
     *
     * Recall that a smart contract and an account are the same thing in the context
     * of the TON Blockchain, and that these terms can be used interchangeably, at
     * least as long as only small (or “usual”) smart contracts are considered. A large
     * smart-contract may employ several accounts lying in different shardchains of
     * the same workchain for load balancing purposes.
     *
     * An account is identified by its full address and is completely described by
     * its state. In other words, there is nothing else in an account apart from its
     * address and state.;
 */
public record AccountFilter(StringFilter id, IntFilter acc_type,
    AccountStatusEnumFilter acc_type_name, StringFilter balance,
    OtherCurrencyArrayFilter balance_other, StringFilter bits, StringFilter boc, StringFilter cells,
    StringFilter code, StringFilter code_hash, StringFilter data, StringFilter data_hash,
    StringFilter due_payment, StringFilter init_code_hash, FloatFilter last_paid,
    StringFilter last_trans_lt, StringFilter library, StringFilter library_hash,
    StringFilter prev_code_hash, StringFilter proof, StringFilter public_cells,
    IntFilter split_depth, StringFilter state_hash, BooleanFilter tick, BooleanFilter tock,
    IntFilter workchain_id, AccountFilter OR) {
}
