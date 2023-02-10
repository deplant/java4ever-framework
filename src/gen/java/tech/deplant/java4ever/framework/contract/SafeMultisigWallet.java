package tech.deplant.java4ever.framework.contract;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java wrapper class for usage of <strong>SafeMultisigWallet</strong> contract for Everscale blockchain.
 */
public record SafeMultisigWallet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<Void> acceptTransfer(String payload) {
    Map<String, Object> params = Map.of("payload", payload);
    return new FunctionCall<Void>(this, "acceptTransfer", params, null);
  }

  public FunctionCall<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new FunctionCall<Void>(this, "sendTransaction", params, null);
  }

  public FunctionCall<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
      Boolean bounce, Boolean allBalance, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "allBalance", allBalance, 
        "payload", payload);
    return new FunctionCall<ResultOfSubmitTransaction>(this, "submitTransaction", params, null);
  }

  public FunctionCall<Void> confirmTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionCall<Void>(this, "confirmTransaction", params, null);
  }

  public FunctionCall<ResultOfIsConfirmed> isConfirmed(Integer mask, Integer index) {
    Map<String, Object> params = Map.of("mask", mask, 
        "index", index);
    return new FunctionCall<ResultOfIsConfirmed>(this, "isConfirmed", params, null);
  }

  public FunctionCall<ResultOfGetParameters> getParameters() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetParameters>(this, "getParameters", params, null);
  }

  public FunctionCall<ResultOfGetTransaction> getTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionCall<ResultOfGetTransaction>(this, "getTransaction", params, null);
  }

  public FunctionCall<ResultOfGetTransactions> getTransactions() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetTransactions>(this, "getTransactions", params, null);
  }

  public FunctionCall<ResultOfGetTransactionIds> getTransactionIds() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetTransactionIds>(this, "getTransactionIds", params, null);
  }

  public FunctionCall<ResultOfGetCustodians> getCustodians() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetCustodians>(this, "getCustodians", params, null);
  }

  public record ResultOfSubmitTransaction(Long transId) {
  }

  public record ResultOfIsConfirmed(Boolean confirmed) {
  }

  public record ResultOfGetParameters(Integer maxQueuedTransactions, Integer maxCustodianCount,
      Long expirationTime, BigInteger minValue, Integer requiredTxnConfirms) {
  }

  public record ResultOfGetTransaction(Map<String, Object> trans) {
  }

  public record ResultOfGetTransactions(Map<String, Object>[] transactions) {
  }

  public record ResultOfGetTransactionIds(Long[] ids) {
  }

  public record ResultOfGetCustodians(Map<String, Object>[] custodians) {
  }
}
