package tech.deplant.java4ever.binding.gql;

public record ValidatorSetFilter(ValidatorSetListArrayFilter list, IntFilter main, IntFilter total,
    StringFilter total_weight, FloatFilter utime_since, FloatFilter utime_until,
    ValidatorSetFilter OR) {
}
