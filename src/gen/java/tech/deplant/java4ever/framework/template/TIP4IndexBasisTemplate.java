package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP4IndexBasis;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP4IndexBasis</strong> contract for Everscale blockchain.
 */
public record TIP4IndexBasisTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP4IndexBasis> deploy(Sdk sdk, Credentials credentials, Address _collection) {
    Map<String, Object> initialDataFields = Map.of("_collection", _collection);
    Map<String, Object> params = Map.of();
    return new DeployCall<TIP4IndexBasis>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
