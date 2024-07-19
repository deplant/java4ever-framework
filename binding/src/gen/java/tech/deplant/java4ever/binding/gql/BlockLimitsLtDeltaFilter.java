package tech.deplant.java4ever.binding.gql;

public record BlockLimitsLtDeltaFilter(FloatFilter hard_limit, FloatFilter soft_limit,
    FloatFilter underload, BlockLimitsLtDeltaFilter OR) {
}
