package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record MessageProcessingStatusEnumFilter(MessageProcessingStatusEnum eq,
    MessageProcessingStatusEnum ne, MessageProcessingStatusEnum gt, MessageProcessingStatusEnum lt,
    MessageProcessingStatusEnum ge, MessageProcessingStatusEnum le,
    List<MessageProcessingStatusEnum> in, List<MessageProcessingStatusEnum> notIn) {
}
