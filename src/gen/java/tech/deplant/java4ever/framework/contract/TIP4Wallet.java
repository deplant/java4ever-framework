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
 * Java wrapper class for usage of <strong>TIP4Wallet</strong> contract for Everscale blockchain.
 */
public record TIP4Wallet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new FunctionCall<Void>(this, "sendTransaction", params, null);
  }

  public FunctionCall<Void> transferOwnership(BigInteger newOwner) {
    Map<String, Object> params = Map.of("newOwner", newOwner);
    return new FunctionCall<Void>(this, "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfOwner> owner() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfOwner>(this, "owner", params, null);
  }

  public FunctionCall<ResultOf_randomNonce> _randomNonce() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOf_randomNonce>(this, "_randomNonce", params, null);
  }

  public record ResultOfOwner(BigInteger owner) {
  }

  public record ResultOf_randomNonce(BigInteger _randomNonce) {
  }
}
