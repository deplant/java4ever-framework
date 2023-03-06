package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java wrapper class for usage of <strong>TIP4IndexBasis</strong> contract for Everscale blockchain.
 */
public record TIP4IndexBasis(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP4IndexBasis(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TIP4IndexBasis(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TIP4IndexBasis(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getInfo\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"collection\",\"type\":\"address\"}]},{\"name\":\"destruct\",\"inputs\":[{\"name\":\"gasReceiver\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_collection\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_collection\",\"type\":\"address\"}]}");
  }

  public FunctionHandle<ResultOfGetInfo> getInfo() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetInfo>(sdk(), address(), abi(), credentials(), "getInfo", params, null);
  }

  public FunctionHandle<Void> destruct(Address gasReceiver) {
    Map<String, Object> params = Map.of("gasReceiver", gasReceiver);
    return new FunctionHandle<Void>(sdk(), address(), abi(), credentials(), "destruct", params, null);
  }

  public record ResultOfGetInfo(Address collection) {
  }
}
