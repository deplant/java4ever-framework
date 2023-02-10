package tech.deplant.java4ever.framework.contract;

import java.lang.Boolean;
import java.lang.Integer;
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
 * Java wrapper class for usage of <strong>TIP3TokenWallet</strong> contract for Everscale blockchain.
 */
public record TIP3TokenWallet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(this, "supportsInterface", params, null);
  }

  public FunctionCall<Void> destroy(Address remainingGasTo) {
    Map<String, Object> params = Map.of("remainingGasTo", remainingGasTo);
    return new FunctionCall<Void>(this, "destroy", params, null);
  }

  public FunctionCall<Void> burnByRoot(BigInteger amount, Address remainingGasTo,
      Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(this, "burnByRoot", params, null);
  }

  public FunctionCall<Void> burn(BigInteger amount, Address remainingGasTo, Address callbackTo,
      TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(this, "burn", params, null);
  }

  public FunctionCall<ResultOfBalance> balance(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfBalance>(this, "balance", params, null);
  }

  public FunctionCall<ResultOfOwner> owner(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfOwner>(this, "owner", params, null);
  }

  public FunctionCall<ResultOfRoot> root(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfRoot>(this, "root", params, null);
  }

  public FunctionCall<ResultOfWalletCode> walletCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfWalletCode>(this, "walletCode", params, null);
  }

  public FunctionCall<Void> transfer(BigInteger amount, Address recipient,
      BigInteger deployWalletValue, Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipient", recipient, 
        "deployWalletValue", deployWalletValue, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(this, "transfer", params, null);
  }

  public FunctionCall<Void> transferToWallet(BigInteger amount, Address recipientTokenWallet,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipientTokenWallet", recipientTokenWallet, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(this, "transferToWallet", params, null);
  }

  public FunctionCall<Void> acceptTransfer(BigInteger amount, Address sender,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "sender", sender, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(this, "acceptTransfer", params, null);
  }

  public FunctionCall<Void> acceptMint(BigInteger amount, Address remainingGasTo, Boolean notify,
      TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(this, "acceptMint", params, null);
  }

  public FunctionCall<Void> sendSurplusGas(Address to) {
    Map<String, Object> params = Map.of("to", to);
    return new FunctionCall<Void>(this, "sendSurplusGas", params, null);
  }

  public record ResultOfSupportsInterface(Boolean value0) {
  }

  public record ResultOfBalance(BigInteger value0) {
  }

  public record ResultOfOwner(Address value0) {
  }

  public record ResultOfRoot(Address value0) {
  }

  public record ResultOfWalletCode(TvmCell value0) {
  }
}
