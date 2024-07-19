package tech.deplant.java4ever.binding.gql;

/**
 * Election parameters;
 */
public record ConfigP15Filter(FloatFilter elections_end_before, FloatFilter elections_start_before,
    FloatFilter stake_held_for, FloatFilter validators_elected_for, ConfigP15Filter OR) {
}
