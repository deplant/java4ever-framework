package tech.deplant.java4ever.binding.gql;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockStateUpdate(@JsonProperty("new") String _new, Integer new_depth, String new_hash,
    String old, Integer old_depth, String old_hash) {
}
