package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP4Wallet;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP4Wallet</strong> contract for Everscale blockchain.
 */
public record TIP4WalletTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP4Wallet> deploy(Sdk sdk, Credentials credentials, BigInteger _randomNonce) {
    Map<String, Object> initialDataFields = Map.of("_randomNonce", _randomNonce);
    Map<String, Object> params = Map.of();
    return new DeployCall<TIP4Wallet>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
