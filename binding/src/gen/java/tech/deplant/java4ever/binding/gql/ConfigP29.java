package tech.deplant.java4ever.binding.gql;

/**
 * Consensus config;
 */
public record ConfigP29(Float attempt_duration, Float catchain_max_deps, Float consensus_timeout_ms,
    Float fast_attempts, Float max_block_bytes, Float max_collated_bytes, Boolean new_catchain_ids,
    Float next_candidate_delay_ms, Float round_candidates) {
}
