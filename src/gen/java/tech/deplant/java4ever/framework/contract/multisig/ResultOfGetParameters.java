package tech.deplant.java4ever.framework.contract.multisig;

import java.lang.Integer;
import java.lang.Long;
import java.math.BigInteger;

public record ResultOfGetParameters(Integer maxQueuedTransactions, Integer maxQueuedLimits,
    Integer maxCustodianCount, Long maxLimitPeriod, BigInteger expirationTime, BigInteger minValue,
    Integer requiredTxnConfirms, Integer requiredLimConfirms, Integer requiredUpdConfirms) {
}
