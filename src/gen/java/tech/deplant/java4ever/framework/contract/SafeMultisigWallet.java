package tech.deplant.java4ever.framework.contract;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
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
public record SafeMultisigWallet(ContractHandle contract) {
  public SafeMultisigWallet(Sdk sdk, String address, ContractAbi abi, Credentials credentials) {
    this(new ContractHandle(sdk, address, abi, credentials));
  }

  public CallHandle acceptTransfer(String payload) {
    Map<String, Object> params = Map.of("payload", payload);
    return new CallHandle(contract(), "acceptTransfer", params, null);
  }

  public CallHandle sendTransaction(Address dest, BigInteger value, Boolean bounce, Integer flags,
      TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new CallHandle(contract(), "sendTransaction", params, null);
  }

  public CallHandle submitTransaction(Address dest, BigInteger value, Boolean bounce,
      Boolean allBalance, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "allBalance", allBalance, 
        "payload", payload);
    return new CallHandle(contract(), "submitTransaction", params, null);
  }

  public CallHandle confirmTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new CallHandle(contract(), "confirmTransaction", params, null);
  }

  public CallHandle isConfirmed(Integer mask, Integer index) {
    Map<String, Object> params = Map.of("mask", mask, 
        "index", index);
    return new CallHandle(contract(), "isConfirmed", params, null);
  }

  public CallHandle getParameters() {
    Map<String, Object> params = Map.of();
    return new CallHandle(contract(), "getParameters", params, null);
  }

  public CallHandle getTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new CallHandle(contract(), "getTransaction", params, null);
  }

  public CallHandle getTransactions() {
    Map<String, Object> params = Map.of();
    return new CallHandle(contract(), "getTransactions", params, null);
  }

  public CallHandle getTransactionIds() {
    Map<String, Object> params = Map.of();
    return new CallHandle(contract(), "getTransactionIds", params, null);
  }

  public CallHandle getCustodians() {
    Map<String, Object> params = Map.of();
    return new CallHandle(contract(), "getCustodians", params, null);
  }
}
