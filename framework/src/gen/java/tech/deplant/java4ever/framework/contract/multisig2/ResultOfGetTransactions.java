package tech.deplant.java4ever.framework.contract.multisig2;

import java.lang.Object;
import java.lang.String;
import java.util.Map;

public record ResultOfGetTransactions(Map<String, Object>[] transactions) {
}
