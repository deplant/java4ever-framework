package tech.deplant.java4ever.binding.gql;

/**
 * This is Block;
 */
public record BlockFilter(StringFilter id, BlockAccountBlocksArrayFilter account_blocks,
    BooleanFilter after_merge, BooleanFilter after_split, BooleanFilter before_split,
    StringFilter boc, StringFilter chain_order, StringFilter created_by, StringFilter end_lt,
    StringFilter file_hash, IntFilter flags, FloatFilter gen_catchain_seqno,
    StringFilter gen_software_capabilities, FloatFilter gen_software_version, FloatFilter gen_utime,
    FloatFilter gen_validator_list_hash_short, IntFilter global_id, InMsgArrayFilter in_msg_descr,
    BooleanFilter key_block, BlockMasterFilter master, ExtBlkRefFilter master_ref,
    FloatFilter min_ref_mc_seqno, OutMsgArrayFilter out_msg_descr, ExtBlkRefFilter prev_alt_ref,
    FloatFilter prev_key_block_seqno, ExtBlkRefFilter prev_ref, ExtBlkRefFilter prev_vert_alt_ref,
    ExtBlkRefFilter prev_vert_ref, StringFilter rand_seed, FloatFilter seq_no, StringFilter shard,
    BlockSignaturesFilter signatures, StringFilter start_lt, BlockStateUpdateFilter state_update,
    IntFilter status, BlockProcessingStatusEnumFilter status_name, IntFilter tr_count,
    BlockValueFlowFilter value_flow, FloatFilter version, FloatFilter vert_seq_no,
    BooleanFilter want_merge, BooleanFilter want_split, IntFilter workchain_id, BlockFilter OR) {
}
