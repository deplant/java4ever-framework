package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
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
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>GiverV3Contract</strong> contract for Everscale blockchain.
 */
public class GiverV3Contract extends GiverContract {
  public GiverV3Contract(int contextId, String address) throws JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public GiverV3Contract(int contextId, String address, ContractAbi abi) {
    super(contextId,address,abi,Credentials.NONE);
  }

  public GiverV3Contract(int contextId, String address, Credentials credentials) throws
      JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),credentials);
  }

  @JsonCreator
  public GiverV3Contract(int contextId, String address, ContractAbi abi, Credentials credentials) {
    super(contextId,address,abi,credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"getMessages\",\"inputs\":[],\"outputs\":[{\"name\":\"messages\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"hash\",\"type\":\"uint256\"},{\"name\":\"expireAt\",\"type\":\"uint32\"}]}]},{\"name\":\"upgrade\",\"inputs\":[{\"name\":\"newcode\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"m_messages\",\"type\":\"map(uint256,uint32)\"}],\"ABI version\":2}");
  }

  public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "sendTransaction", params, null);
  }

  public FunctionHandle<ResultOfGetMessages> getMessages() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetMessages>(ResultOfGetMessages.class, contextId(), address(), abi(), credentials(), "getMessages", params, null);
  }

  public FunctionHandle<Void> upgrade(TvmCell newcode) {
    Map<String, Object> params = Map.of("newcode", newcode);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "upgrade", params, null);
  }

  public record ResultOfGetMessages(Map<String, Object>[] messages) {
  }
}
