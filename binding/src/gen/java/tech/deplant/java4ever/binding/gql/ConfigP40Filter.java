package tech.deplant.java4ever.binding.gql;

public record ConfigP40Filter(FloatFilter collations_score_weight, FloatFilter min_samples_count,
    FloatFilter min_slashing_protection_score, FloatFilter resend_mc_blocks_count,
    FloatFilter signing_score_weight, FloatFilter slashing_period_mc_blocks_count,
    FloatFilter z_param_denominator, FloatFilter z_param_numerator, ConfigP40Filter OR) {
}
