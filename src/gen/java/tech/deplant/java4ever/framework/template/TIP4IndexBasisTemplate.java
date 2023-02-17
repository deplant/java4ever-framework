package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployCall;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.TIP4IndexBasis;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java template class for deploy of <strong>TIP4IndexBasis</strong> contract for Everscale blockchain.
 */
public record TIP4IndexBasisTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TIP4IndexBasisTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TIP4IndexBasisTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getInfo\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"collection\",\"type\":\"address\"}]},{\"name\":\"destruct\",\"inputs\":[{\"name\":\"gasReceiver\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_collection\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_collection\",\"type\":\"address\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECFwEAApEAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gsUBQQWA4rtRNDXScMB+GaJ+Gkh2zzTAAGfgQIA1xgg+QFY+EL5EPKo3tM/AfhDIbnytCD4I4ED6KiCCBt3QKC58rT4Y9MfAds88jwNCgYDeu1E0NdJwwH4ZiLQ0wP6QDD4aak4APhEf29xggiYloBvcm1vc3BvdPhk3CHHAOMCIdcNH/K8IeMDAds88jwTEwYDOiCCC6Ot17rjAiCCEGi1Xz+64wIgghBswcwMuuMCDgkHA+Iw+Eby4Ez4Qm7jANMf+ERYb3X4ZNHbPCGOHSPQ0wH6QDAxyM+HIM5xzwthAcjPk7MHMDLOzclwjjH4RCBvEyFvEvhJVQJvEcjPhIDKAM+EQM4B+gL0AHHPC2kByPhEbxXPCx/Ozcn4RG8U4vsA4wDyABIIDwAa+ERwb3KAQG90+GT4SgM2MPhCbuMA+Ebyc9GI+En4SscF8uhl+ADbPPIAChEPAhbtRNDXScIBjoDjDQsSAT5w7UTQ9AVxIYBA9A6OgN/4aoBA9A7yvdcL//hicPhjDAECiQ0AQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABADNjD4RvLgTPhCbuMAIZPU0dDe+kDR2zww2zzyABIQDwAi+Er4Q/hCyMv/yz/Pg87J7VQBNoj4SfhKxwXy6GX4AMjPhQjOgG/PQMmBAKD7ABEANE1ldGhvZCBmb3IgY29sbGVjdGlvbiBvbmx5ACbtRNDT/9M/0wAx+kDR+Gr4Y/hiAAr4RvLgTAIK9KQg9KEWFQAUc29sIDAuNTguMgAA");
  }

  public DeployCall<TIP4IndexBasis> prepareDeploy(Sdk sdk, Credentials credentials,
      Address _collection) {
    Map<String, Object> initialDataFields = Map.of("_collection", _collection);
    Map<String, Object> params = Map.of();
    return new DeployCall<TIP4IndexBasis>(TIP4IndexBasis.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
