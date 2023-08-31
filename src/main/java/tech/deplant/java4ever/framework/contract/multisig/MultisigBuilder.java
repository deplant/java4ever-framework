package tech.deplant.java4ever.framework.contract.multisig;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class MultisigBuilder {

	private MultisigWallet.Type type = MultisigWallet.Type.SURF;

	private int confirmations = 1; // Min confirmations for consensus

	private final Set<BigInteger> publicKeys = new HashSet<>();

	private Credentials deployKeys;

	public MultisigBuilder() {
	}

	public MultisigWallet build(Sdk sdk, Credentials deployKeys, Giver giver, BigInteger value) throws JsonProcessingException, EverSdkException {
		BigInteger[] owners = null;
		if (publicKeys.isEmpty()) {
			owners = new BigInteger[]{deployKeys.publicBigInt()};
		} else {
			owners = publicKeys.toArray(BigInteger[]::new);
		}
		return switch (type) {
			case SURF -> new SurfMultisigWalletTemplate()
					.prepareDeploy(sdk,deployKeys,owners,confirmations)
					.deployWithGiver(giver, value);
			case SAFE -> new SafeMultisigWalletTemplate()
					.prepareDeploy(sdk,deployKeys,owners,confirmations)
					.deployWithGiver(giver, value);
			case SETCODE -> new SetcodeMultisigWalletTemplate()
					.prepareDeploy(sdk,deployKeys,owners,confirmations)
					.deployWithGiver(giver, value);
		};
	}

	public Credentials deployKeys() {
		return deployKeys;
	}

	public MultisigBuilder setDeployKeys(Credentials deployKeys) {
		this.deployKeys = deployKeys;
		return this;
	}

	public MultisigBuilder addOwner(BigInteger pubkey) {
		publicKeys.add(pubkey);
		return this;
	}

	public MultisigBuilder removeOwner(BigInteger pubkey) {
		publicKeys.remove(pubkey);
		return this;
	}

	public MultisigWallet.Type type() {
		return type;
	}

	public MultisigBuilder setType(MultisigWallet.Type type) {
		this.type = type;
		return this;
	}

	public int confirmations() {
		return confirmations;
	}

	public MultisigBuilder setConfirmations(int confirmations) {
		this.confirmations = confirmations;
		return this;
	}
}
