package tech.deplant.java4ever.framework.contract;

import java.lang.Boolean;
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
 * Java wrapper class for usage of <strong>GiverV2</strong> contract for Everscale blockchain.
 */
public record GiverV2(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<Void> upgrade(TvmCell newcode) {
    Map<String, Object> params = Map.of("newcode", newcode);
    return new FunctionCall<Void>(this, "upgrade", params, null);
  }

  public FunctionCall<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce);
    return new FunctionCall<Void>(this, "sendTransaction", params, null);
  }

  public FunctionCall<ResultOfGetMessages> getMessages() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetMessages>(this, "getMessages", params, null);
  }

  public record ResultOfGetMessages(Map<String, Object>[] messages) {
  }
}
