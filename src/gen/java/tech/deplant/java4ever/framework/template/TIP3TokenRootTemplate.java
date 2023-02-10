package tech.deplant.java4ever.framework.template;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.TIP3TokenRoot;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java template class for deploy of <strong>TIP3TokenRoot</strong> contract for Everscale blockchain.
 */
public record TIP3TokenRootTemplate(ContractAbi abi, ContractTvc tvc) implements Template {
  public DeployCall<TIP3TokenRoot> deploy(Sdk sdk, Credentials credentials, String name_,
      String symbol_, Integer decimals_, Address rootOwner_, TvmCell walletCode_,
      BigInteger randomNonce_, Address deployer_, Address initialSupplyTo, BigInteger initialSupply,
      BigInteger deployWalletValue, Boolean mintDisabled, Boolean burnByRootDisabled,
      Boolean burnPaused, Address remainingGasTo) {
    Map<String, Object> initialDataFields = Map.of("name_", name_, 
        "symbol_", symbol_, 
        "decimals_", decimals_, 
        "rootOwner_", rootOwner_, 
        "walletCode_", walletCode_, 
        "randomNonce_", randomNonce_, 
        "deployer_", deployer_);
    Map<String, Object> params = Map.of("initialSupplyTo", initialSupplyTo, 
        "initialSupply", initialSupply, 
        "deployWalletValue", deployWalletValue, 
        "mintDisabled", mintDisabled, 
        "burnByRootDisabled", burnByRootDisabled, 
        "burnPaused", burnPaused, 
        "remainingGasTo", remainingGasTo);
    return new DeployCall<TIP3TokenRoot>(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
