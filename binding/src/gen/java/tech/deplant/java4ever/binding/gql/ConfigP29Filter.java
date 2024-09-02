package tech.deplant.java4ever.binding.gql;

/**
 * Consensus config;
 */
public record ConfigP29Filter(FloatFilter attempt_duration, FloatFilter catchain_max_deps,
    FloatFilter consensus_timeout_ms, FloatFilter fast_attempts, FloatFilter max_block_bytes,
    FloatFilter max_collated_bytes, BooleanFilter new_catchain_ids,
    FloatFilter next_candidate_delay_ms, FloatFilter round_candidates, ConfigP29Filter OR) {
}
