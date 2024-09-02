package tech.deplant.java4ever.binding.gql;

/**
 * Set of validator's signatures for the Block with correspond id;
 */
public record BlockSignaturesFilter(StringFilter id, BlockFilter block, FloatFilter catchain_seqno,
    FloatFilter gen_utime, StringFilter proof, FloatFilter seq_no, StringFilter shard,
    StringFilter sig_weight, BlockSignaturesSignaturesArrayFilter signatures,
    FloatFilter validator_list_hash_short, IntFilter workchain_id, BlockSignaturesFilter OR) {
}
