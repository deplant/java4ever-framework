package tech.deplant.java4ever.binding.gql;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockStateUpdateFilter(@JsonProperty("new") StringFilter _new, IntFilter new_depth,
    StringFilter new_hash, StringFilter old, IntFilter old_depth, StringFilter old_hash,
    BlockStateUpdateFilter OR) {
}
