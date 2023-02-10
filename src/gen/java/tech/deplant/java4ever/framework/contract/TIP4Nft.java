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
 * Java wrapper class for usage of <strong>TIP4Nft</strong> contract for Everscale blockchain.
 */
public record TIP4Nft(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<Void> burn(Address dest) {
    Map<String, Object> params = Map.of("dest", dest);
    return new FunctionCall<Void>(this, "burn", params, null);
  }

  public FunctionCall<ResultOfIndexCode> indexCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCode>(this, "indexCode", params, null);
  }

  public FunctionCall<ResultOfIndexCodeHash> indexCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCodeHash>(this, "indexCodeHash", params, null);
  }

  public FunctionCall<ResultOfResolveIndex> resolveIndex(Integer answerId, Address collection,
      Address owner) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "collection", collection, 
        "owner", owner);
    return new FunctionCall<ResultOfResolveIndex>(this, "resolveIndex", params, null);
  }

  public FunctionCall<ResultOfGetJson> getJson(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfGetJson>(this, "getJson", params, null);
  }

  public FunctionCall<Void> transfer(Address to, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("to", to, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionCall<Void>(this, "transfer", params, null);
  }

  public FunctionCall<Void> changeOwner(Address newOwner, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newOwner", newOwner, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionCall<Void>(this, "changeOwner", params, null);
  }

  public FunctionCall<Void> changeManager(Address newManager, Address sendGasTo,
      Map<Address, Map<String, Object>> callbacks) {
    Map<String, Object> params = Map.of("newManager", newManager, 
        "sendGasTo", sendGasTo, 
        "callbacks", callbacks);
    return new FunctionCall<Void>(this, "changeManager", params, null);
  }

  public FunctionCall<ResultOfGetInfo> getInfo(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfGetInfo>(this, "getInfo", params, null);
  }

  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(this, "supportsInterface", params, null);
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
