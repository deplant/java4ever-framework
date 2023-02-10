package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.GiverV2;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>GiverV2</strong> contract for Everscale blockchain.
 */
public record GiverV2Template(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<GiverV2> deploy(Sdk sdk, Credentials credentials) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of();
    return new DeployCall<GiverV2>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
