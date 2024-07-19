package tech.deplant.java4ever.binding.gql;

import java.util.List;

public record Query(Info info, BlockchainQuery blockchain, List<Account> accounts,
    List<Transaction> transactions, List<Message> messages, List<Block> blocks,
    List<BlockSignatures> blocks_signatures, List<Zerostate> zerostates,
    List<Counterparty> counterparties, List<String> aggregateAccounts,
    List<String> aggregateTransactions, List<String> aggregateMessages,
    List<String> aggregateBlocks, List<String> aggregateBlockSignatures,
    String getManagementAccessKey) {
}
