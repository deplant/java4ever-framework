package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP3TokenWallet</strong> contract for Everscale blockchain.
 */
public record TIP3TokenWalletTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP3TokenWallet> deploy(Sdk sdk, Credentials credentials, Address root_,
      Address owner_) {
    Map<String, Object> initialDataFields = Map.of("root_", root_, 
        "owner_", owner_);
    Map<String, Object> params = Map.of();
    return new DeployCall<TIP3TokenWallet>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
