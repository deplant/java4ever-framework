package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.math.BigInteger;

public interface MultisigWallet extends Giver {

	public enum Type {
		SURF,
		SAFE,
		SETCODE;
	}

	@Override
	default FunctionHandle<Void> give(String to, BigInteger value) {
		return sendTransaction(new Address(to), value, false,
		                       1, TvmCell.EMPTY());
	}

	FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
	                                     Integer flags, TvmCell payload);

	static MultisigWallet deploySingleSig(Type type, Sdk sdk, Giver giver, Credentials keys, BigInteger value) throws JsonProcessingException, EverSdkException {
		return deployMultiSig(type,sdk, giver, keys, value, 1, keys.publicBigInt());
	}

	static MultisigWallet deployMultiSig(Type type, Sdk sdk, Giver giver, Credentials keys, BigInteger value, int confirmations, BigInteger... publicKeys) throws JsonProcessingException, EverSdkException {
		return switch (type) {
			case SURF -> new SurfMultisigWalletTemplate()
					.prepareDeploy(sdk,keys,publicKeys,confirmations)
					.deployWithGiver(giver, value);
			case SAFE -> new SafeMultisigWalletTemplate()
					.prepareDeploy(sdk,keys,publicKeys,confirmations)
					.deployWithGiver(giver, value);
			case SETCODE -> new SetcodeMultisigWalletTemplate()
					.prepareDeploy(sdk,keys,publicKeys,confirmations)
					.deployWithGiver(giver, value);
		};
	}
}
