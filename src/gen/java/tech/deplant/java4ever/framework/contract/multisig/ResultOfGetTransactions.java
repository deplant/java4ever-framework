package tech.deplant.java4ever.framework.contract.multisig;

import java.lang.Object;
import java.lang.String;
import java.util.Map;

public record ResultOfGetTransactions(Map<String, Object>[] transactions) {
}
