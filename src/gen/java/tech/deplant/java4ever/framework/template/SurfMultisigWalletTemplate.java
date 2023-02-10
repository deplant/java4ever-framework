package tech.deplant.java4ever.framework.template;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.SurfMultisigWallet;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>SurfMultisigWallet</strong> contract for Everscale blockchain.
 */
public record SurfMultisigWalletTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<SurfMultisigWallet> deploy(Sdk sdk, Credentials credentials,
      BigInteger[] owners, Integer reqConfirms) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("owners", owners, 
        "reqConfirms", reqConfirms);
    return new DeployCall<SurfMultisigWallet>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
