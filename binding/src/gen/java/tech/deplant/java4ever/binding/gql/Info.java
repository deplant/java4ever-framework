package tech.deplant.java4ever.binding.gql;

import java.util.List;

/**
 *  GraphQL Server info;
 */
public record Info(String version, Float time, Float blocksLatency, Float messagesLatency,
    Float transactionsLatency, Float latency, Float lastBlockTime, List<String> endpoints,
    String chainOrderBoundary, Boolean rempEnabled) {
}
