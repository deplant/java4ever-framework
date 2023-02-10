package tech.deplant.java4ever.framework.contract;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java wrapper class for usage of <strong>TIP4Index</strong> contract for Everscale blockchain.
 */
public record TIP4Index(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<ResultOfGetInfo> getInfo(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfGetInfo>(this, "getInfo", params, null);
  }

  public FunctionCall<Void> destruct(Address gasReceiver) {
    Map<String, Object> params = Map.of("gasReceiver", gasReceiver);
    return new FunctionCall<Void>(this, "destruct", params, null);
  }

  public record ResultOfGetInfo(Address collection, Address owner, Address nft) {
  }
}
