package tech.deplant.java4ever.framework.contract.tip3;

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
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP3TokenRoot</strong> contract for Everscale blockchain.
 */
public record TIP3TokenRoot(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP3TokenRoot(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TIP3TokenRoot(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TIP3TokenRoot(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"initialSupplyTo\",\"type\":\"address\"},{\"name\":\"initialSupply\",\"type\":\"uint128\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"},{\"name\":\"mintDisabled\",\"type\":\"bool\"},{\"name\":\"burnByRootDisabled\",\"type\":\"bool\"},{\"name\":\"burnPaused\",\"type\":\"bool\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"disableMint\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"mintDisabled\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnTokens\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"disableBurnByRoot\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnByRootDisabled\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"burnPaused\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"setBurnPaused\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"paused\",\"type\":\"bool\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbacks\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"payload\",\"type\":\"cell\"}]}],\"outputs\":[]},{\"name\":\"name\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"string\"}]},{\"name\":\"symbol\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"string\"}]},{\"name\":\"decimals\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint8\"}]},{\"name\":\"totalSupply\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"walletCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"cell\"}]},{\"name\":\"rootOwner\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"walletOf\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"walletOwner\",\"type\":\"address\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"deployWallet\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"}],\"outputs\":[{\"name\":\"tokenWallet\",\"type\":\"address\"}]},{\"name\":\"mint\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"recipient\",\"type\":\"address\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptBurn\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"walletOwner\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[],\"id\":\"0x192B51B1\"},{\"name\":\"sendSurplusGas\",\"inputs\":[{\"name\":\"to\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"name_\",\"type\":\"string\"},{\"key\":2,\"name\":\"symbol_\",\"type\":\"string\"},{\"key\":3,\"name\":\"decimals_\",\"type\":\"uint8\"},{\"key\":4,\"name\":\"rootOwner_\",\"type\":\"address\"},{\"key\":5,\"name\":\"walletCode_\",\"type\":\"cell\"},{\"key\":6,\"name\":\"randomNonce_\",\"type\":\"uint256\"},{\"key\":7,\"name\":\"deployer_\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"symbol_\",\"type\":\"string\"},{\"name\":\"decimals_\",\"type\":\"uint8\"},{\"name\":\"rootOwner_\",\"type\":\"address\"},{\"name\":\"walletCode_\",\"type\":\"cell\"},{\"name\":\"totalSupply_\",\"type\":\"uint128\"},{\"name\":\"burnPaused_\",\"type\":\"bool\"},{\"name\":\"burnByRootDisabled_\",\"type\":\"bool\"},{\"name\":\"mintDisabled_\",\"type\":\"bool\"},{\"name\":\"randomNonce_\",\"type\":\"uint256\"},{\"name\":\"deployer_\",\"type\":\"address\"}]}");
  }

  public FunctionHandle<ResultOfSupportsInterface> supportsInterface(Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "interfaceID", interfaceID);
    return new FunctionHandle<ResultOfSupportsInterface>(ResultOfSupportsInterface.class, sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
  }

  public FunctionHandle<ResultOfDisableMint> disableMint() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfDisableMint>(ResultOfDisableMint.class, sdk(), address(), abi(), credentials(), "disableMint", params, null);
  }

  public FunctionHandle<ResultOfMintDisabled> mintDisabled() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfMintDisabled>(ResultOfMintDisabled.class, sdk(), address(), abi(), credentials(), "mintDisabled", params, null);
  }

  public FunctionHandle<Void> burnTokens(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "burnTokens", params, null);
  }

  public FunctionHandle<ResultOfDisableBurnByRoot> disableBurnByRoot() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfDisableBurnByRoot>(ResultOfDisableBurnByRoot.class, sdk(), address(), abi(), credentials(), "disableBurnByRoot", params, null);
  }

  public FunctionHandle<ResultOfBurnByRootDisabled> burnByRootDisabled() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfBurnByRootDisabled>(ResultOfBurnByRootDisabled.class, sdk(), address(), abi(), credentials(), "burnByRootDisabled", params, null);
  }

  public FunctionHandle<ResultOfBurnPaused> burnPaused() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfBurnPaused>(ResultOfBurnPaused.class, sdk(), address(), abi(), credentials(), "burnPaused", params, null);
  }

  public FunctionHandle<ResultOfSetBurnPaused> setBurnPaused(Boolean paused) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "paused", paused);
    return new FunctionHandle<ResultOfSetBurnPaused>(ResultOfSetBurnPaused.class, sdk(), address(), abi(), credentials(), "setBurnPaused", params, null);
  }

  public FunctionHandle<Void> transferOwnership(Address newOwner, Address remainingGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newOwner", newOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbacks", callbacks);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "transferOwnership", params, null);
  }

  public FunctionHandle<ResultOfName> name() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfName>(ResultOfName.class, sdk(), address(), abi(), credentials(), "name", params, null);
  }

  public FunctionHandle<ResultOfSymbol> symbol() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfSymbol>(ResultOfSymbol.class, sdk(), address(), abi(), credentials(), "symbol", params, null);
  }

  public FunctionHandle<ResultOfDecimals> decimals() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfDecimals>(ResultOfDecimals.class, sdk(), address(), abi(), credentials(), "decimals", params, null);
  }

  public FunctionHandle<ResultOfTotalSupply> totalSupply() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfTotalSupply>(ResultOfTotalSupply.class, sdk(), address(), abi(), credentials(), "totalSupply", params, null);
  }

  public FunctionHandle<ResultOfWalletCode> walletCode() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfWalletCode>(ResultOfWalletCode.class, sdk(), address(), abi(), credentials(), "walletCode", params, null);
  }

  public FunctionHandle<ResultOfRootOwner> rootOwner() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfRootOwner>(ResultOfRootOwner.class, sdk(), address(), abi(), credentials(), "rootOwner", params, null);
  }

  public FunctionHandle<ResultOfWalletOf> walletOf(Address walletOwner) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "walletOwner", walletOwner);
    return new FunctionHandle<ResultOfWalletOf>(ResultOfWalletOf.class, sdk(), address(), abi(), credentials(), "walletOf", params, null);
  }

  public FunctionHandle<ResultOfDeployWallet> deployWallet(Address walletOwner,
      BigInteger deployWalletValue) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "walletOwner", walletOwner, 
        "deployWalletValue", deployWalletValue);
    return new FunctionHandle<ResultOfDeployWallet>(ResultOfDeployWallet.class, sdk(), address(), abi(), credentials(), "deployWallet", params, null);
  }

  public FunctionHandle<Void> mint(BigInteger amount, Address recipient,
      BigInteger deployWalletValue, Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipient", recipient, 
        "deployWalletValue", deployWalletValue, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "mint", params, null);
  }

  public FunctionHandle<Void> acceptBurn(BigInteger amount, Address walletOwner,
      Address remainingGasTo, Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "walletOwner", walletOwner, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptBurn", params, null);
  }

  public FunctionHandle<Void> sendSurplusGas(Address to) {
    Map<String, Object> params = Map.of("to", to);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "sendSurplusGas", params, null);
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
