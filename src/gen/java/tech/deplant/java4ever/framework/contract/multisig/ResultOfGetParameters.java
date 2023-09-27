package tech.deplant.java4ever.framework.contract.multisig;

import java.lang.Integer;
import java.math.BigInteger;

public record ResultOfGetParameters(Integer maxQueuedTransactions, Integer maxCustodianCount,
    BigInteger expirationTime, BigInteger minValue, Integer requiredTxnConfirms,
    Integer requiredUpdConfirms) {
}
