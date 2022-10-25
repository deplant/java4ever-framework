package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Tip32Root;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class Tip31RootTemplate extends ContractTemplate {

	public static final ContractTvc DEFAULT_TVC = ContractTvc.ofResource("artifacts/tip31/TokenRoot.tvc");

	public Tip31RootTemplate() throws JsonProcessingException {
		super(DEFAULT_ABI(), DEFAULT_TVC);
	}

	public Tip31RootTemplate(ContractAbi abi, ContractTvc tvc) {
		super(abi, tvc);
	}

	public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
		return ContractAbi.ofResource("artifacts/tip31/TokenRoot.abi.json");
	}

	public Tip32Root deployWithGiver(Sdk sdk,
	                                 Credentials keys,
	                                 Giver giver,
	                                 BigInteger value,
	                                 String tokenName,
	                                 String tokenTicker,
	                                 int tokenDecimalPoints,
	                                 Address rootOwner,
	                                 BigInteger totalSupply,
	                                 Address deployer,
	                                 Address initialSupplyTo,
	                                 BigInteger initialSupply,
	                                 BigInteger initialWalletValue,
	                                 boolean mintDisabled,
	                                 boolean burnByRootDisabled,
	                                 boolean burnPaused,
	                                 Address remainingGasTo) throws EverSdkException {
		var initialData = Map.of(
				"name_", tokenName,
				"symbol_", tokenTicker,
				"decimals_", tokenDecimalPoints,
				"rootOwner_", rootOwner,
				"walletCode_", Tip31WalletTemplate.DEFAULT_TVC.code(sdk),
				"totalSupply_", totalSupply,
				"randomNonce_", 0,
				"deployer_", deployer
		);
		var constructorInputs = Map.<String, Object>of(
				"initialSupplyTo", initialSupplyTo, // "name": "initialSupplyTo", "type": "address"
				"initialSupply", initialSupply, // "name": "initialSupply", "type": "uint128"
				"deployWalletValue", initialWalletValue, // "name": "deployWalletValue", "type": "uint128"
				"mintDisabled", mintDisabled, // "name": "mintDisabled", "type": "bool"
				"burnByRootDisabled", burnByRootDisabled, // "name": "burnByRootDisabled", "type": "bool"
				"burnPaused", burnPaused, // "name": "burnPaused", "type": "bool"
				"remainingGasTo", remainingGasTo); // "name": "remainingGasTo", "type": "address"
		var contract = super.deployWithGiver(sdk, giver, value, 0, initialData, keys, constructorInputs);
		return new Tip32Root(contract);
	}

}
