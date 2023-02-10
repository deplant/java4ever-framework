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
 * Java wrapper class for usage of <strong>TIP3TokenRoot</strong> contract for Everscale blockchain.
 */
public record TIP3TokenRoot(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(this, "supportsInterface", params, null);
  }

  public FunctionCall<ResultOfDisableMint> disableMint(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDisableMint>(this, "disableMint", params, null);
  }

  public FunctionCall<ResultOfMintDisabled> mintDisabled(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfMintDisabled>(this, "mintDisabled", params, null);
  }

  public FunctionCall<Void> burnTokens(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(this, "burnTokens", params, null);
  }

  public FunctionCall<ResultOfDisableBurnByRoot> disableBurnByRoot(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDisableBurnByRoot>(this, "disableBurnByRoot", params, null);
  }

  public FunctionCall<ResultOfBurnByRootDisabled> burnByRootDisabled(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfBurnByRootDisabled>(this, "burnByRootDisabled", params, null);
  }

  public FunctionCall<ResultOfBurnPaused> burnPaused(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfBurnPaused>(this, "burnPaused", params, null);
  }

  public FunctionCall<ResultOfSetBurnPaused> setBurnPaused(Integer answerId, Boolean paused) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "paused", paused);
    return new FunctionCall<ResultOfSetBurnPaused>(this, "setBurnPaused", params, null);
  }

  public FunctionCall<Void> transferOwnership(Address newOwner, Address remainingGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newOwner", newOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbacks", callbacks);
    return new FunctionCall<Void>(this, "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfName> name(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfName>(this, "name", params, null);
  }

  public FunctionCall<ResultOfSymbol> symbol(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfSymbol>(this, "symbol", params, null);
  }

  public FunctionCall<ResultOfDecimals> decimals(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDecimals>(this, "decimals", params, null);
  }

  public FunctionCall<ResultOfTotalSupply> totalSupply(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfTotalSupply>(this, "totalSupply", params, null);
  }

  public FunctionCall<ResultOfWalletCode> walletCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfWalletCode>(this, "walletCode", params, null);
  }

  public FunctionCall<ResultOfRootOwner> rootOwner(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfRootOwner>(this, "rootOwner", params, null);
  }

  public FunctionCall<ResultOfWalletOf> walletOf(Integer answerId, Address walletOwner) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "walletOwner", walletOwner);
    return new FunctionCall<ResultOfWalletOf>(this, "walletOf", params, null);
  }

  public FunctionCall<ResultOfDeployWallet> deployWallet(Integer answerId, Address walletOwner,
      BigInteger deployWalletValue) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "walletOwner", walletOwner, 
        "deployWalletValue", deployWalletValue);
    return new FunctionCall<ResultOfDeployWallet>(this, "deployWallet", params, null);
  }

  public FunctionCall<Void> mint(BigInteger amount, Address recipient, BigInteger deployWalletValue,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipient", recipient, 
        "deployWalletValue", deployWalletValue, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(this, "mint", params, null);
  }

  public FunctionCall<Void> acceptBurn(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(this, "acceptBurn", params, null);
  }

  public FunctionCall<Void> sendSurplusGas(Address to) {
    Map<String, Object> params = Map.of("to", to);
    return new FunctionCall<Void>(this, "sendSurplusGas", params, null);
  }

  public record ResultOfSupportsInterface(Boolean value0) {
  }

  public record ResultOfDisableMint(Boolean value0) {
  }

  public record ResultOfMintDisabled(Boolean value0) {
  }

  public record ResultOfDisableBurnByRoot(Boolean value0) {
  }

  public record ResultOfBurnByRootDisabled(Boolean value0) {
  }

  public record ResultOfBurnPaused(Boolean value0) {
  }

  public record ResultOfSetBurnPaused(Boolean value0) {
  }

  public record ResultOfName(String value0) {
  }

  public record ResultOfSymbol(String value0) {
  }

  public record ResultOfDecimals(Integer value0) {
  }

  public record ResultOfTotalSupply(BigInteger value0) {
  }

  public record ResultOfWalletCode(TvmCell value0) {
  }

  public record ResultOfRootOwner(Address value0) {
  }

  public record ResultOfWalletOf(Address value0) {
  }

  public record ResultOfDeployWallet(Address tokenWallet) {
  }
}
