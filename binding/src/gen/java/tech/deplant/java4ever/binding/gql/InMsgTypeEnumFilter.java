package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record InMsgTypeEnumFilter(InMsgTypeEnum eq, InMsgTypeEnum ne, InMsgTypeEnum gt,
    InMsgTypeEnum lt, InMsgTypeEnum ge, InMsgTypeEnum le, List<InMsgTypeEnum> in,
    List<InMsgTypeEnum> notIn) {
}
