package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP4Index;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP4Index</strong> contract for Everscale blockchain.
 */
public record TIP4IndexTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP4Index> deploy(Sdk sdk, Credentials credentials, Address _nft,
      Address collection) {
    Map<String, Object> initialDataFields = Map.of("_nft", _nft);
    Map<String, Object> params = Map.of("collection", collection);
    return new DeployCall<TIP4Index>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
