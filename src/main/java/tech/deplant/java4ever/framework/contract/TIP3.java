package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;

import java.math.BigInteger;
import java.util.List;

public interface TIP3 {

	static TIP3TokenRoot deployRoot(Sdk sdk, Credentials deployKeys, BigInteger nonce, Contract owner, Giver giver, String name, String symbol, int decimals) throws JsonProcessingException, EverSdkException {
		var ownerAddr = new Address(owner.address());
		var deployStatement = new TIP3TokenRootTemplate().prepareDeploy(sdk,
		                                                                deployKeys,
		                                                                name,
		                                                                symbol,
		                                                                decimals,
		                                                                ownerAddr,
		                                                                TIP3TokenWalletTemplate.DEFAULT_TVC().codeCell(sdk),
		                                                                nonce,
		                                                                Address.ZERO,
		                                                                Address.ZERO,
		                                                                BigInteger.ZERO,
		                                                                BigInteger.ZERO,
		                                                                false,
		                                                                false,
		                                                                false,
		                                                                ownerAddr);
		return deployStatement.deployWithGiver(giver, CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER,"5"));
	}

	static TIP3TokenWallet deployWallet(Sdk sdk, TIP3TokenRoot root, MultisigWallet rootOwner, Address tokenOwnerAddress) throws JsonProcessingException, EverSdkException {
		var payload = root.deployWallet(tokenOwnerAddress,CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER,"1")).toPayload();
		var result = rootOwner.sendTransaction(new Address(root.address()),
		                      CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER,"2"),
		                      true,
		                      MessageFlag.FEE_EXTRA.flag(),
		                      payload).callTree(true, TIP3TokenRootTemplate.DEFAULT_ABI(),TIP3TokenWalletTemplate.DEFAULT_ABI());
		var walletAddress = result.extractDeployAddress(root.address());
		return new TIP3TokenWallet(sdk,walletAddress);
	}

	interface TIP31Root {

		/**
		 * Returns the name of the token - e.g. "MyToken".
		 */
		String name();

		/**
		 * Returns the symbol of the token. E.g. “HIX”.
		 */
		String symbol();

		/**
		 * Returns the number of decimals the token uses - e.g. 8, means to divide the token amount by 100000000 to get its user representation.
		 */
		Integer decimals();

		/**
		 * Returns the token wallet code.
		 */
		BigInteger totalSupply();

		/**
		 * Returns the symbol of the token. E.g. “HIX”.
		 */
		TvmCell walletCode();
	}

}
