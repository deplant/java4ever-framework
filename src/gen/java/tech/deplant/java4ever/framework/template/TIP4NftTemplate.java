package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP4Nft;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP4Nft</strong> contract for Everscale blockchain.
 */
public record TIP4NftTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP4Nft> deploy(Sdk sdk, Credentials credentials, BigInteger _id, Address owner,
      Address sendGasTo, BigInteger remainOnNft, String json, BigInteger indexDeployValue,
      BigInteger indexDestroyValue, TvmCell codeIndex) {
    Map<String, Object> initialDataFields = Map.of("_id", _id);
    Map<String, Object> params = Map.of("owner", owner, 
        "sendGasTo", sendGasTo, 
        "remainOnNft", remainOnNft, 
        "json", json, 
        "indexDeployValue", indexDeployValue, 
        "indexDestroyValue", indexDestroyValue, 
        "codeIndex", codeIndex);
    return new DeployCall<TIP4Nft>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
