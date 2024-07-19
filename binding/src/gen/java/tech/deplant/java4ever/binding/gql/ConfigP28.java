package tech.deplant.java4ever.binding.gql;

/**
 * Catchain config;
 */
public record ConfigP28(Float mc_catchain_lifetime, Float shard_catchain_lifetime,
    Float shard_validators_lifetime, Float shard_validators_num, Boolean shuffle_mc_validators) {
}
