package tech.deplant.java4ever.binding.gql;

/**
 * Catchain config;
 */
public record ConfigP28Filter(FloatFilter mc_catchain_lifetime, FloatFilter shard_catchain_lifetime,
    FloatFilter shard_validators_lifetime, FloatFilter shard_validators_num,
    BooleanFilter shuffle_mc_validators, ConfigP28Filter OR) {
}
