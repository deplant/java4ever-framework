package tech.deplant.java4ever.binding.gql;

public record BlockMasterPrevBlkSignaturesFilter(StringFilter node_id, StringFilter r,
    StringFilter s, BlockMasterPrevBlkSignaturesFilter OR) {
}
