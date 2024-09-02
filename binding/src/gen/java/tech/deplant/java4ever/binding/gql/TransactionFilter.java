package tech.deplant.java4ever.binding.gql;

/**
 * TON Transaction;
 */
public record TransactionFilter(StringFilter id, BooleanFilter aborted, AccountFilter account,
    StringFilter account_addr, TransactionActionFilter action, StringFilter balance_delta,
    OtherCurrencyArrayFilter balance_delta_other, BlockFilter block, StringFilter block_id,
    StringFilter boc, TransactionBounceFilter bounce, StringFilter chain_order,
    TransactionComputeFilter compute, TransactionCreditFilter credit, BooleanFilter credit_first,
    BooleanFilter destroyed, IntFilter end_status, AccountStatusEnumFilter end_status_name,
    StringFilter ext_in_msg_fee, MessageFilter in_message, StringFilter in_msg,
    BooleanFilter installed, StringFilter lt, StringFilter new_hash, FloatFilter now,
    StringFilter old_hash, IntFilter orig_status, AccountStatusEnumFilter orig_status_name,
    MessageArrayFilter out_messages, StringArrayFilter out_msgs, IntFilter outmsg_cnt,
    StringFilter prepare_transaction, StringFilter prev_trans_hash, StringFilter prev_trans_lt,
    StringFilter proof, TransactionSplitInfoFilter split_info, IntFilter status,
    TransactionProcessingStatusEnumFilter status_name, TransactionStorageFilter storage,
    StringFilter total_fees, OtherCurrencyArrayFilter total_fees_other, IntFilter tr_type,
    TransactionTypeEnumFilter tr_type_name, StringFilter tt, IntFilter workchain_id,
    TransactionFilter OR) {
}
