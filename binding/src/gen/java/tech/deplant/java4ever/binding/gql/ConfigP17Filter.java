package tech.deplant.java4ever.binding.gql;

/**
 * Validator stake parameters;
 */
public record ConfigP17Filter(StringFilter max_stake, FloatFilter max_stake_factor,
    StringFilter min_stake, StringFilter min_total_stake, ConfigP17Filter OR) {
}
