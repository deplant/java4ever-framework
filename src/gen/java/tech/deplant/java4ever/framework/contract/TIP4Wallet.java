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
 * Java wrapper class for usage of <strong>TIP4Wallet</strong> contract for Everscale blockchain.
 */
public record TIP4Wallet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP4Wallet(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"owner\",\"inputs\":[],\"outputs\":[{\"name\":\"owner\",\"type\":\"uint256\"}]},{\"name\":\"_randomNonce\",\"inputs\":[],\"outputs\":[{\"name\":\"_randomNonce\",\"type\":\"uint256\"}]}],\"events\":[{\"name\":\"OwnershipTransferred\",\"inputs\":[{\"name\":\"previousOwner\",\"type\":\"uint256\"},{\"name\":\"newOwner\",\"type\":\"uint256\"}]}],\"data\":[{\"key\":1,\"name\":\"_randomNonce\",\"type\":\"uint256\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"owner\",\"type\":\"uint256\"},{\"name\":\"_randomNonce\",\"type\":\"uint256\"}]}");
  }

  public FunctionCall<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "sendTransaction", params, null);
  }

  public FunctionCall<Void> transferOwnership(BigInteger newOwner) {
    Map<String, Object> params = Map.of("newOwner", newOwner);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfOwner> owner() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfOwner>(sdk(), address(), abi(), credentials(), "owner", params, null);
  }

  public FunctionCall<ResultOf_randomNonce> _randomNonce() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOf_randomNonce>(sdk(), address(), abi(), credentials(), "_randomNonce", params, null);
  }

  public record ResultOfOwner(BigInteger owner) {
  }

  public record ResultOf_randomNonce(BigInteger _randomNonce) {
  }
}
