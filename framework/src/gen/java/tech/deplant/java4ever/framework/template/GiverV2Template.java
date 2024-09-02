package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.GiverV2Contract;

/**
 * Java template class for deploy of <strong>GiverV2Contract</strong> contract for Everscale blockchain.
 */
public record GiverV2Template(ContractAbi abi, Tvc tvc) implements Template {
  public GiverV2Template(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"upgrade\",\"inputs\":[{\"name\":\"newcode\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"getMessages\",\"inputs\":[],\"outputs\":[{\"name\":\"messages\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"hash\",\"type\":\"uint256\"},{\"name\":\"expireAt\",\"type\":\"uint64\"}]}]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]}],\"events\":[],\"ABI version\":2}");
  }

  public DeployHandle<GiverV2Contract> prepareDeploy(int contextId, int workchainId,
      Credentials credentials) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of();
    return new DeployHandle<GiverV2Contract>(GiverV2Contract.class, contextId, abi(), tvc(), workchainId, credentials, initialDataFields, params, null);
  }
}
