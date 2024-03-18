package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.tip4.TIP4IndexContract;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java template class for deploy of <strong>TIP4IndexContract</strong> contract for Everscale blockchain.
 */
public record TIP4IndexTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TIP4IndexTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TIP4IndexTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"version\":\"2.2\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"collection\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getInfo\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"collection\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"nft\",\"type\":\"address\"}]},{\"name\":\"destruct\",\"inputs\":[{\"name\":\"gasReceiver\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_nft\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_nft\",\"type\":\"address\"},{\"name\":\"_collection\",\"type\":\"address\"},{\"name\":\"_owner\",\"type\":\"address\"}],\"ABI version\":2}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECIAEAA4IAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAgaK2zUfBAQkiu1TIOMDIMD/4wIgwP7jAvILHAYFHgOK7UTQ10nDAfhmifhpIds80wABn4ECANcYIPkBWPhC+RDyqN7TPwH4QyG58rQg+COBA+iogggbd0CgufK0+GPTHwHbPPI8EQ4HA3rtRNDXScMB+GYi0NMD+kAw+GmpOAD4RH9vcYIImJaAb3Jtb3Nwb3T4ZNwhxwDjAiHXDR/yvCHjAwHbPPI8GxsHAzogggujrde64wIgghAWX5bBuuMCIIIQR1ZU3LrjAhYSCARCMPhCbuMA+EbycyGT1NHQ3vpA0fhBiMjPjits1szOyds8Dh8LCQJqiCFus/LoZiBu8n/Q1PpA+kAwbBL4SfhKxwXy4GT4ACH4a/hs+kJvE9cL/5Mg+GvfMNs88gAKFwA8U2FsdCBkb2Vzbid0IGNvbnRhaW4gYW55IHZhbHVlAhjQIIs4rbNYxwWKiuIMDQEK103Q2zwNAELXTNCLL0pA1yb0BDHTCTGLL0oY1yYg10rCAZLXTZIwbeICFu1E0NdJwgGOgOMNDxoCSnDtRND0BXEhgED0Do6A34kg+Gz4a/hqgED0DvK91wv/+GJw+GMQEQECiREAQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAD/jD4RvLgTPhCbuMA0x/4RFhvdfhk0ds8I44mJdDTAfpAMDHIz4cgznHPC2FeIMjPkll+WwbOWcjOAcjOzc3NyXCOOvhEIG8TIW8S+ElVAm8RyM+EgMoAz4RAzgH6AvQAcc8LaV4gyPhEbxXPCx/OWcjOAcjOzc3NyfhEbxTi+wAaFRMBCOMA8gAUACjtRNDT/9M/MfhDWMjL/8s/zsntVAAi+ERwb3KAQG90+GT4S/hM+EoDNjD4RvLgTPhCbuMAIZPU0dDe+kDR2zww2zzyABoYFwA6+Ez4S/hK+EP4QsjL/8s/z4POWcjOAcjOzc3J7VQBMoj4SfhKxwXy6GXIz4UIzoBvz0DJgQCg+wAZACZNZXRob2QgZm9yIE5GVCBvbmx5AELtRNDT/9M/0wAx+kDU0dD6QNTR0PpA0fhs+Gv4avhj+GIACvhG8uBMAgr0pCD0oR4dABRzb2wgMC41OC4yAAAADCD4Ye0e2Q==");
  }

  public DeployHandle<TIP4IndexContract> prepareDeploy(int contextId, int workchainId,
      Credentials credentials, Address _nft, Address collection) {
    Map<String, Object> initialDataFields = Map.of("_nft", _nft);
    Map<String, Object> params = Map.of("collection", collection);
    return new DeployHandle<TIP4IndexContract>(TIP4IndexContract.class, contextId, abi(), tvc(), workchainId, credentials, initialDataFields, params, null);
  }
}
