package tech.deplant.java4ever.framework;

/**
 * The type Debug options.
 */
public record DebugOptions(boolean enabled, long timeout, boolean throwErrors, long transactionMaxCount, ContractAbi... treeAbis) {
}
