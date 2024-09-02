package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record TransactionTypeEnumFilter(TransactionTypeEnum eq, TransactionTypeEnum ne,
    TransactionTypeEnum gt, TransactionTypeEnum lt, TransactionTypeEnum ge, TransactionTypeEnum le,
    List<TransactionTypeEnum> in, List<TransactionTypeEnum> notIn) {
}
