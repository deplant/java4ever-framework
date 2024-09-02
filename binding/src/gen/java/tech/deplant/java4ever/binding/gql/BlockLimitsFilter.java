package tech.deplant.java4ever.binding.gql;

public record BlockLimitsFilter(BlockLimitsBytesFilter bytes, BlockLimitsGasFilter gas,
    BlockLimitsLtDeltaFilter lt_delta, BlockLimitsFilter OR) {
}
