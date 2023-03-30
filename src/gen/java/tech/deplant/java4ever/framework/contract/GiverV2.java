package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
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
 * Java wrapper class for usage of <strong>GiverV2</strong> contract for Everscale blockchain.
 */
public record GiverV2(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements EverOSGiver {
  public GiverV2(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public GiverV2(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public GiverV2(Sdk sdk, String address, Credentials credentials) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"upgrade\",\"inputs\":[{\"name\":\"newcode\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"getMessages\",\"inputs\":[],\"outputs\":[{\"name\":\"messages\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"hash\",\"type\":\"uint256\"},{\"name\":\"expireAt\",\"type\":\"uint64\"}]}]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]}],\"events\":[]}");
  }

  public FunctionHandle<Void> upgrade(TvmCell newcode) {
    Map<String, Object> params = Map.of("newcode", newcode);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "upgrade", params, null);
  }

  public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "sendTransaction", params, null);
  }

  public FunctionHandle<ResultOfGetMessages> getMessages() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetMessages>(ResultOfGetMessages.class, sdk(), address(), abi(), credentials(), "getMessages", params, null);
  }

  public record ResultOfGetMessages(Map<String, Object>[] messages) {
  }
}
