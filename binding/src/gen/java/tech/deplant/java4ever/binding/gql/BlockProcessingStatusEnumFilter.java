package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record BlockProcessingStatusEnumFilter(BlockProcessingStatusEnum eq,
    BlockProcessingStatusEnum ne, BlockProcessingStatusEnum gt, BlockProcessingStatusEnum lt,
    BlockProcessingStatusEnum ge, BlockProcessingStatusEnum le, List<BlockProcessingStatusEnum> in,
    List<BlockProcessingStatusEnum> notIn) {
}
