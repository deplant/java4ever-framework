package tech.deplant.java4ever.framework.contract.tip3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP3TokenWalletContract</strong> contract for Everscale blockchain.
 */
public class TIP3TokenWalletContract extends AbstractContract {
  public TIP3TokenWalletContract(Sdk sdk, String address) throws JsonProcessingException {
    super(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TIP3TokenWalletContract(Sdk sdk, String address, ContractAbi abi) {
    super(sdk,address,abi,Credentials.NONE);
  }

  public TIP3TokenWalletContract(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    super(sdk,address,DEFAULT_ABI(),credentials);
  }

  @JsonCreator
  public TIP3TokenWalletContract(Sdk sdk, String address, ContractAbi abi,
      Credentials credentials) {
    super(sdk,address,abi,credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]},{\"name\":\"destroy\",\"inputs\":[{\"name\":\"remainingGasTo\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"burnByRoot\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"burn\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"callbackTo\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"balance\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"owner\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"root\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"walletCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"cell\"}]},{\"name\":\"transfer\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"recipient\",\"type\":\"address\"},{\"name\":\"deployWalletValue\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"transferToWallet\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"recipientTokenWallet\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptTransfer\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"sender\",\"type\":\"address\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[],\"id\":\"0x67A0B95F\"},{\"name\":\"acceptMint\",\"inputs\":[{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"remainingGasTo\",\"type\":\"address\"},{\"name\":\"notify\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[],\"id\":\"0x4384F298\"},{\"name\":\"sendSurplusGas\",\"inputs\":[{\"name\":\"to\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"root_\",\"type\":\"address\"},{\"key\":2,\"name\":\"owner_\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"root_\",\"type\":\"address\"},{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"balance_\",\"type\":\"uint128\"}]}");
  }

  public FunctionHandle<ResultOfSupportsInterface> supportsInterface(Long interfaceID) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "interfaceID", interfaceID);
    return new FunctionHandle<ResultOfSupportsInterface>(ResultOfSupportsInterface.class, sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
  }

  public FunctionHandle<Void> destroy(Address remainingGasTo) {
    Map<String, Object> params = Map.of("remainingGasTo", remainingGasTo);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "destroy", params, null);
  }

  public FunctionHandle<Void> burnByRoot(BigInteger amount, Address remainingGasTo,
      Address callbackTo, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "burnByRoot", params, null);
  }

  public FunctionHandle<Void> burn(BigInteger amount, Address remainingGasTo, Address callbackTo,
      TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "callbackTo", callbackTo, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "burn", params, null);
  }

  public FunctionHandle<ResultOfBalance> balance() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfBalance>(ResultOfBalance.class, sdk(), address(), abi(), credentials(), "balance", params, null);
  }

  public FunctionHandle<ResultOfOwner> owner() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfOwner>(ResultOfOwner.class, sdk(), address(), abi(), credentials(), "owner", params, null);
  }

  public FunctionHandle<ResultOfRoot> root() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfRoot>(ResultOfRoot.class, sdk(), address(), abi(), credentials(), "root", params, null);
  }

  public FunctionHandle<ResultOfWalletCode> walletCode() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfWalletCode>(ResultOfWalletCode.class, sdk(), address(), abi(), credentials(), "walletCode", params, null);
  }

  public FunctionHandle<Void> transfer(BigInteger amount, Address recipient,
      BigInteger deployWalletValue, Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipient", recipient, 
        "deployWalletValue", deployWalletValue, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "transfer", params, null);
  }

  public FunctionHandle<Void> transferToWallet(BigInteger amount, Address recipientTokenWallet,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "recipientTokenWallet", recipientTokenWallet, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "transferToWallet", params, null);
  }

  public FunctionHandle<Void> acceptTransfer(BigInteger amount, Address sender,
      Address remainingGasTo, Boolean notify, TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "sender", sender, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptTransfer", params, null);
  }

  public FunctionHandle<Void> acceptMint(BigInteger amount, Address remainingGasTo, Boolean notify,
      TvmCell payload) {
    Map<String, Object> params = Map.of("amount", amount, 
        "remainingGasTo", remainingGasTo, 
        "notify", notify, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptMint", params, null);
  }

  public FunctionHandle<Void> sendSurplusGas(Address to) {
    Map<String, Object> params = Map.of("to", to);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "sendSurplusGas", params, null);
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
