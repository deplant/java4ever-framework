package tech.deplant.java4ever.binding.gql;

public record BlockLimitsBytesFilter(FloatFilter hard_limit, FloatFilter soft_limit,
    FloatFilter underload, BlockLimitsBytesFilter OR) {
}
