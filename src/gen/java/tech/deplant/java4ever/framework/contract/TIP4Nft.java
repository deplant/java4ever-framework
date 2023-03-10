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
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP4Nft</strong> contract for Everscale blockchain.
 */
public record TIP4Nft(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP4Nft(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TIP4Nft(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TIP4Nft(Sdk sdk, String address, Credentials credentials) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"sendGasTo\",\"type\":\"address\"},{\"name\":\"remainOnNft\",\"type\":\"uint128\"},{\"name\":\"json\",\"type\":\"string\"},{\"name\":\"indexDeployValue\",\"type\":\"uint128\"},{\"name\":\"indexDestroyValue\",\"type\":\"uint128\"},{\"name\":\"codeIndex\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"burn\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"indexCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"indexCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"hash\",\"type\":\"uint256\"}]},{\"name\":\"resolveIndex\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"collection\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"}],\"outputs\":[{\"name\":\"index\",\"type\":\"address\"}]},{\"name\":\"getJson\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"json\",\"type\":\"string\"}]},{\"name\":\"transfer\",\"inputs\":[{\"name\":\"to\",\"type\":\"address\"},{\"name\":\"sendGasTo\",\"type\":\"address\"},{\"name\":\"callbacks\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"payload\",\"type\":\"cell\"}]}],\"outputs\":[]},{\"name\":\"changeOwner\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"address\"},{\"name\":\"sendGasTo\",\"type\":\"address\"},{\"name\":\"callbacks\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"payload\",\"type\":\"cell\"}]}],\"outputs\":[]},{\"name\":\"changeManager\",\"inputs\":[{\"name\":\"newManager\",\"type\":\"address\"},{\"name\":\"sendGasTo\",\"type\":\"address\"},{\"name\":\"callbacks\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"payload\",\"type\":\"cell\"}]}],\"outputs\":[]},{\"name\":\"getInfo\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"},{\"name\":\"collection\",\"type\":\"address\"}]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]}],\"events\":[{\"name\":\"NftCreated\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"},{\"name\":\"collection\",\"type\":\"address\"}]},{\"name\":\"OwnerChanged\",\"inputs\":[{\"name\":\"oldOwner\",\"type\":\"address\"},{\"name\":\"newOwner\",\"type\":\"address\"}]},{\"name\":\"ManagerChanged\",\"inputs\":[{\"name\":\"oldManager\",\"type\":\"address\"},{\"name\":\"newManager\",\"type\":\"address\"}]},{\"name\":\"NftBurned\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"},{\"name\":\"collection\",\"type\":\"address\"}]}],\"data\":[{\"key\":1,\"name\":\"_id\",\"type\":\"uint256\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_supportedInterfaces\",\"type\":\"optional(cell)\"},{\"name\":\"_id\",\"type\":\"uint256\"},{\"name\":\"_collection\",\"type\":\"address\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_manager\",\"type\":\"address\"},{\"name\":\"_json\",\"type\":\"string\"},{\"name\":\"_indexDeployValue\",\"type\":\"uint128\"},{\"name\":\"_indexDestroyValue\",\"type\":\"uint128\"},{\"name\":\"_codeIndex\",\"type\":\"cell\"}]}");
  }

  public FunctionHandle<Void> burn(Address dest) {
    Map<String, Object> params = Map.of("dest", dest);
    return new FunctionHandle<Void>(sdk(), address(), abi(), credentials(), "burn", params, null);
  }

  public FunctionHandle<ResultOfIndexCode> indexCode() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfIndexCode>(sdk(), address(), abi(), credentials(), "indexCode", params, null);
  }

  public FunctionHandle<ResultOfIndexCodeHash> indexCodeHash() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfIndexCodeHash>(sdk(), address(), abi(), credentials(), "indexCodeHash", params, null);
  }

  public FunctionHandle<ResultOfResolveIndex> resolveIndex(Address collection, Address owner) {
    Map<String, Object> params = Map.of("collection", collection, 
        "owner", owner);
    return new FunctionHandle<ResultOfResolveIndex>(sdk(), address(), abi(), credentials(), "resolveIndex", params, null);
  }

  public FunctionHandle<ResultOfGetJson> getJson() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetJson>(sdk(), address(), abi(), credentials(), "getJson", params, null);
  }

  public FunctionHandle<Void> transfer(Address to, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("to", to, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionHandle<Void>(sdk(), address(), abi(), credentials(), "transfer", params, null);
  }

  public FunctionHandle<Void> changeOwner(Address newOwner, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newOwner", newOwner, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionHandle<Void>(sdk(), address(), abi(), credentials(), "changeOwner", params, null);
  }

  public FunctionHandle<Void> changeManager(Address newManager, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newManager", newManager, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionHandle<Void>(sdk(), address(), abi(), credentials(), "changeManager", params, null);
  }

  public FunctionHandle<ResultOfGetInfo> getInfo() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetInfo>(sdk(), address(), abi(), credentials(), "getInfo", params, null);
  }

  public FunctionHandle<ResultOfSupportsInterface> supportsInterface(Integer interfaceID) {
    Map<String, Object> params = Map.of("interfaceID", interfaceID);
    return new FunctionHandle<ResultOfSupportsInterface>(sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
  }

  public record ResultOfIndexCode(TvmCell code) {
  }

  public record ResultOfIndexCodeHash(BigInteger hash) {
  }

  public record ResultOfResolveIndex(Address index) {
  }

  public record ResultOfGetJson(String json) {
  }

  public record ResultOfGetInfo(BigInteger id, Address owner, Address manager, Address collection) {
  }

  public record ResultOfSupportsInterface(Boolean value0) {
  }
}
