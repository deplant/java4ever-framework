package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record TransactionProcessingStatusEnumFilter(TransactionProcessingStatusEnum eq,
    TransactionProcessingStatusEnum ne, TransactionProcessingStatusEnum gt,
    TransactionProcessingStatusEnum lt, TransactionProcessingStatusEnum ge,
    TransactionProcessingStatusEnum le, List<TransactionProcessingStatusEnum> in,
    List<TransactionProcessingStatusEnum> notIn) {
}
