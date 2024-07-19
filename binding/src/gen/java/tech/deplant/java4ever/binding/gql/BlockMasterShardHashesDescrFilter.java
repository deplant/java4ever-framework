package tech.deplant.java4ever.binding.gql;

/**
 * Shard description;
 */
public record BlockMasterShardHashesDescrFilter(BooleanFilter before_merge,
    BooleanFilter before_split, StringFilter end_lt, StringFilter fees_collected,
    OtherCurrencyArrayFilter fees_collected_other, StringFilter file_hash, IntFilter flags,
    StringFilter funds_created, OtherCurrencyArrayFilter funds_created_other, FloatFilter gen_utime,
    FloatFilter min_ref_mc_seqno, FloatFilter next_catchain_seqno,
    StringFilter next_validator_shard, BooleanFilter nx_cc_updated, FloatFilter reg_mc_seqno,
    StringFilter root_hash, FloatFilter seq_no, FloatFilter split, IntFilter split_type,
    SplitTypeEnumFilter split_type_name, StringFilter start_lt, BooleanFilter want_merge,
    BooleanFilter want_split, BlockMasterShardHashesDescrFilter OR) {
}
