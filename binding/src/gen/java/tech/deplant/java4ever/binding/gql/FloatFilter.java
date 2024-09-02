package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record FloatFilter(Float eq, Float ne, Float gt, Float lt, Float ge, Float le,
    List<Float> in, List<Float> notIn) {
}
