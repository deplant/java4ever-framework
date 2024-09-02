package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record Mutation(List<String> postRequests, Integer registerAccessKeys,
    Integer revokeAccessKeys, Integer finishOperations) {
}
