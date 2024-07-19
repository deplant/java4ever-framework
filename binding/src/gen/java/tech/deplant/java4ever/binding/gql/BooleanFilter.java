package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record BooleanFilter(Boolean eq, Boolean ne, Boolean gt, Boolean lt, Boolean ge, Boolean le,
    List<Boolean> in, List<Boolean> notIn) {
}
