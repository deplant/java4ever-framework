package tech.deplant.java4ever.binding.gql;

public record Subscription(Account accounts, Transaction transactions, Message messages,
    Block blocks, BlockSignatures blocks_signatures, Zerostate zerostates,
    Counterparty counterparties, RempReceipt rempReceipts) {
}
