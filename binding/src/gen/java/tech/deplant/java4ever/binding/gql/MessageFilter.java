package tech.deplant.java4ever.binding.gql;

/**
 * # Message type
     *
     * Message layout queries.  A message consists of its header followed by its
     * body or payload. The body is essentially arbitrary, to be interpreted by the
     * destination smart contract. It can be queried with the following fields:;
 */
public record MessageFilter(StringFilter id, BlockFilter block, StringFilter block_id,
    StringFilter boc, StringFilter body, StringFilter body_hash, BooleanFilter bounce,
    BooleanFilter bounced, StringFilter chain_order, StringFilter code, StringFilter code_hash,
    FloatFilter created_at, StringFilter created_lt, StringFilter data, StringFilter data_hash,
    StringFilter dst, AccountFilter dst_account, TransactionFilter dst_transaction,
    IntFilter dst_workchain_id, StringFilter fwd_fee, BooleanFilter ihr_disabled,
    StringFilter ihr_fee, StringFilter import_fee, StringFilter library, StringFilter library_hash,
    IntFilter msg_type, MessageTypeEnumFilter msg_type_name, StringFilter proof,
    IntFilter split_depth, StringFilter src, AccountFilter src_account,
    TransactionFilter src_transaction, IntFilter src_workchain_id, IntFilter status,
    MessageProcessingStatusEnumFilter status_name, BooleanFilter tick, BooleanFilter tock,
    StringFilter value, OtherCurrencyArrayFilter value_other, MessageFilter OR) {
}
