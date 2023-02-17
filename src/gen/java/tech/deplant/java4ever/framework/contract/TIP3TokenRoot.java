package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionCall;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP3TokenRoot</strong> contract for Everscale blockchain.
 */
public record TIP3TokenRoot(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP3TokenRoot(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"initialSupplyTo\",\"type\":\"address\"},{\"name\":\"initialSupply\",\"type\":\"uint128\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"},{\"name\":\"mintDisabled\",\"type\":\"bool\"},{\"name\":\"burnByRootDisabled\",\"type\":\"bool\"},{\"name\":\"burnPaused\",\"type\":\"bool\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"disableMint\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"mintDisabled\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnTokens\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"disableBurnByRoot\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnByRootDisabled\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnPaused\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"setBurnPaused\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"paused\",\"type\":\"bool\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbacks\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"payload\",\"type\":\"cell\"}]}],\"outputs\":[]},{\"name\":\"name\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"string\"}]},{\"name\":\"symbol\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"string\"}]},{\"name\":\"decimals\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint8\"}]},{\"name\":\"totalSupply\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"walletCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"cell\"}]},{\"name\":\"rootOwner\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"walletOf\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"walletOwner\",\"type\":\"address\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"deployWallet\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"}],\"outputs\":[{\"name\":\"tokenWallet\",\"type\":\"address\"}]},{\"name\":\"mint\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"recipient\",\"type\":\"address\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptBurn\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[],\"id\":\"0x192B51B1\"},{\"name\":\"sendSurplusGas\",\"inputs\":[{\"name\":\"to\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"name_\",\"type\":\"string\"},{\"key\":2,\"name\":\"symbol_\",\"type\":\"string\"},{\"key\":3,\"name\":\"decimals_\",\"type\":\"uint8\"},{\"key\":4,\"name\":\"rootOwner_\",\"type\":\"address\"},{\"key\":5,\"name\":\"walletCode_\",\"type\":\"cell\"},{\"key\":6,\"name\":\"randomNonce_\",\"type\":\"uint256\"},{\"key\":7,\"name\":\"deployer_\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"symbol_\",\"type\":\"string\"},{\"name\":\"decimals_\",\"type\":\"uint8\"},{\"name\":\"rootOwner_\",\"type\":\"address\"},{\"name\":\"walletCode_\",\"type\":\"cell\"},{\"name\":\"totalSupply_\",\"type\":\"uint128\"},{\"name\":\"burnPaused_\",\"type\":\"bool\"},{\"name\":\"burnByRootDisabled_\",\"type\":\"bool\"},{\"name\":\"mintDisabled_\",\"type\":\"bool\"},{\"name\":\"randomNonce_\",\"type\":\"uint256\"},{\"name\":\"deployer_\",\"type\":\"address\"}]}");
  }

  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
  }

  public FunctionCall<ResultOfDisableMint> disableMint(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDisableMint>(sdk(), address(), abi(), credentials(), "disableMint", params, null);
  }

  public FunctionCall<ResultOfMintDisabled> mintDisabled(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfMintDisabled>(sdk(), address(), abi(), credentials(), "mintDisabled", params, null);
  }

  public FunctionCall<Void> burnTokens(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "burnTokens", params, null);
  }

  public FunctionCall<ResultOfDisableBurnByRoot> disableBurnByRoot(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDisableBurnByRoot>(sdk(), address(), abi(), credentials(), "disableBurnByRoot", params, null);
  }

  public FunctionCall<ResultOfBurnByRootDisabled> burnByRootDisabled(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfBurnByRootDisabled>(sdk(), address(), abi(), credentials(), "burnByRootDisabled", params, null);
  }

  public FunctionCall<ResultOfBurnPaused> burnPaused(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfBurnPaused>(sdk(), address(), abi(), credentials(), "burnPaused", params, null);
  }

  public FunctionCall<ResultOfSetBurnPaused> setBurnPaused(Integer answerId, Boolean paused) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "paused", paused);
    return new FunctionCall<ResultOfSetBurnPaused>(sdk(), address(), abi(), credentials(), "setBurnPaused", params, null);
  }

  public FunctionCall<Void> transferOwnership(Address newOwner, Address remainingGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newOwner", newOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbacks", callbacks);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfName> name(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfName>(sdk(), address(), abi(), credentials(), "name", params, null);
  }

  public FunctionCall<ResultOfSymbol> symbol(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfSymbol>(sdk(), address(), abi(), credentials(), "symbol", params, null);
  }

  public FunctionCall<ResultOfDecimals> decimals(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfDecimals>(sdk(), address(), abi(), credentials(), "decimals", params, null);
  }

  public FunctionCall<ResultOfTotalSupply> totalSupply(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfTotalSupply>(sdk(), address(), abi(), credentials(), "totalSupply", params, null);
  }

  public FunctionCall<ResultOfWalletCode> walletCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfWalletCode>(sdk(), address(), abi(), credentials(), "walletCode", params, null);
  }

  public FunctionCall<ResultOfRootOwner> rootOwner(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfRootOwner>(sdk(), address(), abi(), credentials(), "rootOwner", params, null);
  }

  public FunctionCall<ResultOfWalletOf> walletOf(Integer answerId, Address walletOwner) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "walletOwner", walletOwner);
    return new FunctionCall<ResultOfWalletOf>(sdk(), address(), abi(), credentials(), "walletOf", params, null);
  }

  public FunctionCall<ResultOfDeployWallet> deployWallet(Integer answerId, Address walletOwner,
      BigInteger deployWalletValue) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "walletOwner", walletOwner, 
        "deployWalletValue", deployWalletValue);
    return new FunctionCall<ResultOfDeployWallet>(sdk(), address(), abi(), credentials(), "deployWallet", params, null);
  }

  public FunctionCall<Void> mint(BigInteger amount, Address recipient, BigInteger deployWalletValue,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipient", recipient, 
        "deployWalletValue", deployWalletValue, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "mint", params, null);
  }

  public FunctionCall<Void> acceptBurn(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "acceptBurn", params, null);
  }

  public FunctionCall<Void> sendSurplusGas(Address to) {
    Map<String, Object> params = Map.of("to", to);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "sendSurplusGas", params, null);
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
