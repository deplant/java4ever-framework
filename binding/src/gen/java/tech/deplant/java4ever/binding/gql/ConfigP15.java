package tech.deplant.java4ever.binding.gql;

/**
 * Election parameters;
 */
public record ConfigP15(Float elections_end_before, Float elections_start_before,
    Float stake_held_for, Float validators_elected_for) {
}
