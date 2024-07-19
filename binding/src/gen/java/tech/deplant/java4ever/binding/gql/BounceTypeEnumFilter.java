package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record BounceTypeEnumFilter(BounceTypeEnum eq, BounceTypeEnum ne, BounceTypeEnum gt,
    BounceTypeEnum lt, BounceTypeEnum ge, BounceTypeEnum le, List<BounceTypeEnum> in,
    List<BounceTypeEnum> notIn) {
}
