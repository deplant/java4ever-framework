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
    Credentials credentials) implements ContractHandle {
  public CallHandle<Void> acceptTransfer(String payload) {
    Map<String, Object> params = Map.of("payload", payload);
    return new CallHandle<Void>(this, "acceptTransfer", params, null);
  }

  public CallHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new CallHandle<Void>(this, "sendTransaction", params, null);
  }

  public CallHandle<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
      Boolean bounce, Boolean allBalance, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "allBalance", allBalance, 
        "payload", payload);
    return new CallHandle<ResultOfSubmitTransaction>(this, "submitTransaction", params, null);
  }

  public CallHandle<Void> confirmTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new CallHandle<Void>(this, "confirmTransaction", params, null);
  }

  public CallHandle<ResultOfIsConfirmed> isConfirmed(Integer mask, Integer index) {
    Map<String, Object> params = Map.of("mask", mask, 
        "index", index);
    return new CallHandle<ResultOfIsConfirmed>(this, "isConfirmed", params, null);
  }

  public CallHandle<ResultOfGetParameters> getParameters() {
    Map<String, Object> params = Map.of();
    return new CallHandle<ResultOfGetParameters>(this, "getParameters", params, null);
  }

  public CallHandle<ResultOfGetTransaction> getTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new CallHandle<ResultOfGetTransaction>(this, "getTransaction", params, null);
  }

  public CallHandle<ResultOfGetTransactions> getTransactions() {
    Map<String, Object> params = Map.of();
    return new CallHandle<ResultOfGetTransactions>(this, "getTransactions", params, null);
  }

  public CallHandle<ResultOfGetTransactionIds> getTransactionIds() {
    Map<String, Object> params = Map.of();
    return new CallHandle<ResultOfGetTransactionIds>(this, "getTransactionIds", params, null);
  }

  public CallHandle<ResultOfGetCustodians> getCustodians() {
    Map<String, Object> params = Map.of();
    return new CallHandle<ResultOfGetCustodians>(this, "getCustodians", params, null);
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
