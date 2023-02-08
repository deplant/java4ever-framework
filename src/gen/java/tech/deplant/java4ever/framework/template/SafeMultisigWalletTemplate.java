package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.contract.CallHandle;

import java.math.BigInteger;
import java.util.Map;

/**
 * Java wrapper class for usage of <strong>SafeMultisigWallet</strong> contract for Everscale blockchain.
 */
public record SafeMultisigWalletTemplate() {
	public SafeMultisigWallet deploy(BigInteger[] owners, Integer reqConfirms) {
		Map<String, Object> params = Map.of("owners", owners,
		                                    "reqConfirms", reqConfirms);
		return new CallHandle<Void>(this, "constructor", params, null);
	}
}
