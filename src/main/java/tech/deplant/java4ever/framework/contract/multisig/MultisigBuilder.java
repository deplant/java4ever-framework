package tech.deplant.java4ever.framework.contract.multisig;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class MultisigBuilder {

	private final Set<BigInteger> publicKeys = new HashSet<>();
	private MultisigContract.Type type = MultisigContract.Type.SURF;
	private int confirmations = 1; // Min confirmations for consensus
	private Credentials deployKeys;

	public MultisigBuilder() {
	}

	public DeployHandle<? extends MultisigContract> prepare(int contextId,
	                                                        Credentials deployKeys) throws JsonProcessingException {
		BigInteger[] owners = null;
		if (publicKeys.isEmpty()) {
			owners = new BigInteger[]{deployKeys.publicBigInt()};
		} else {
			owners = publicKeys.toArray(BigInteger[]::new);
		}
		return switch (type) {
			case SURF -> new SurfMultisigWalletTemplate()
					.prepareDeploy(contextId,
					               (int) EverSdk.getDefaultWorkchainId(contextId), deployKeys, owners, confirmations);
			case SAFE -> new SafeMultisigWalletTemplate()
					.prepareDeploy(contextId,
					               (int) EverSdk.getDefaultWorkchainId(contextId), deployKeys, owners, confirmations);
			case SETCODE -> new SetcodeMultisigWalletTemplate()
					.prepareDeploy(contextId,
					               (int) EverSdk.getDefaultWorkchainId(contextId), deployKeys, owners, confirmations);
		};
	}

	public MultisigContract prepareAndDeploy(int contextId,
	                                         Credentials deployKeys,
	                                         GiverContract giver,
	                                         BigInteger value) throws JsonProcessingException, EverSdkException {
		BigInteger[] owners = null;
		if (publicKeys.isEmpty()) {
			owners = new BigInteger[]{deployKeys.publicBigInt()};
		} else {
			owners = publicKeys.toArray(BigInteger[]::new);
		}
		return prepare(contextId, deployKeys).deployWithGiver(giver, value);
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

	public MultisigContract.Type type() {
		return type;
	}

	public MultisigBuilder setType(MultisigContract.Type type) {
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
