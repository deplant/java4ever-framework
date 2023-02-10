package tech.deplant.java4ever.framework.template;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.SetcodeMultisigWallet;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>SetcodeMultisigWallet</strong> contract for Everscale blockchain.
 */
public record SetcodeMultisigWalletTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<SetcodeMultisigWallet> deploy(Sdk sdk, Credentials credentials,
      BigInteger[] owners, Integer reqConfirms) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("owners", owners, 
        "reqConfirms", reqConfirms);
    return new DeployCall<SetcodeMultisigWallet>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
