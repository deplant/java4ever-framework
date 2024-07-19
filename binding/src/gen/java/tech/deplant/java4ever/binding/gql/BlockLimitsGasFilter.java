package tech.deplant.java4ever.binding.gql;

public record BlockLimitsGasFilter(FloatFilter hard_limit, FloatFilter soft_limit,
    FloatFilter underload, BlockLimitsGasFilter OR) {
}
