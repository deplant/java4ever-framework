package tech.deplant.java4ever.binding.gql;

public record TransactionStorageFilter(IntFilter status_change,
    AccountStatusChangeEnumFilter status_change_name, StringFilter storage_fees_collected,
    StringFilter storage_fees_due, TransactionStorageFilter OR) {
}
