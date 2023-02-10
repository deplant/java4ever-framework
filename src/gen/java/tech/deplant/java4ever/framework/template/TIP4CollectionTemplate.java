package tech.deplant.java4ever.framework.template;

import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP4Collection;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP4Collection</strong> contract for Everscale blockchain.
 */
public record TIP4CollectionTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP4Collection> deploy(Sdk sdk, Credentials credentials, TvmCell codeNft,
      TvmCell codeIndex, TvmCell codeIndexBasis, BigInteger ownerPubkey, String json,
      BigInteger mintingFee) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("codeNft", codeNft, 
        "codeIndex", codeIndex, 
        "codeIndexBasis", codeIndexBasis, 
        "ownerPubkey", ownerPubkey, 
        "json", json, 
        "mintingFee", mintingFee);
    return new DeployCall<TIP4Collection>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
