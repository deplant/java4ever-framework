package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record StringFilter(String eq, String ne, String gt, String lt, String ge, String le,
    List<String> in, List<String> notIn) {
}
