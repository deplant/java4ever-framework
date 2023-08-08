package tech.deplant.java4ever.framework;

public record DebugOptions(boolean enabled, long timeout, boolean throwErrors, ContractAbi... treeAbis) {
}
