package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record OutMsgTypeEnumFilter(OutMsgTypeEnum eq, OutMsgTypeEnum ne, OutMsgTypeEnum gt,
    OutMsgTypeEnum lt, OutMsgTypeEnum ge, OutMsgTypeEnum le, List<OutMsgTypeEnum> in,
    List<OutMsgTypeEnum> notIn) {
}
