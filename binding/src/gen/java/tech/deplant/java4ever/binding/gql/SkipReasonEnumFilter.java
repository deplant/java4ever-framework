package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record SkipReasonEnumFilter(SkipReasonEnum eq, SkipReasonEnum ne, SkipReasonEnum gt,
    SkipReasonEnum lt, SkipReasonEnum ge, SkipReasonEnum le, List<SkipReasonEnum> in,
    List<SkipReasonEnum> notIn) {
}
