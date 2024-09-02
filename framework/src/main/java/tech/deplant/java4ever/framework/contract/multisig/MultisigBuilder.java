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

/**
 * The type Multisig builder.
 */
public class MultisigBuilder {

	private final Set<BigInteger> publicKeys = new HashSet<>();
	private MultisigContract.Type type = MultisigContract.Type.SURF;
	private int confirmations = 1; // Min confirmations for consensus
	private Credentials deployKeys;

	/**
	 * Instantiates a new Multisig builder.
	 */
	public MultisigBuilder() {
	}

	/**
	 * Prepare deploy handle.
	 *
	 * @param contextId  the context id
	 * @param deployKeys the deploy keys
	 * @return the deploy handle
	 * @throws JsonProcessingException the json processing exception
	 */
	public DeployHandle<? extends MultisigContract> prepare(int contextId,
	                                                        Credentials deployKeys) throws JsonProcessingException {
		BigInteger[] owners = null;
		if (publicKeys.isEmpty()) {
			owners = new BigInteger[]{deployKeys.publicKeyBigInt()};
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

	/**
	 * Prepare and deploy multisig contract.
	 *
	 * @param contextId  the context id
	 * @param deployKeys the deploy keys
	 * @param giver      the giver
	 * @param value      the value
	 * @return the multisig contract
	 * @throws JsonProcessingException the json processing exception
	 * @throws EverSdkException        the ever sdk exception
	 */
	public MultisigContract prepareAndDeploy(int contextId,
	                                         Credentials deployKeys,
	                                         GiverContract giver,
	                                         BigInteger value) throws JsonProcessingException, EverSdkException {
		BigInteger[] owners = null;
		if (publicKeys.isEmpty()) {
			owners = new BigInteger[]{deployKeys.publicKeyBigInt()};
		} else {
			owners = publicKeys.toArray(BigInteger[]::new);
		}
		return prepare(contextId, deployKeys).deployWithGiver(giver, value);
	}

	/**
	 * Deploy keys credentials.
	 *
	 * @return the credentials
	 */
	public Credentials deployKeys() {
		return deployKeys;
	}

	/**
	 * Sets deploy keys.
	 *
	 * @param deployKeys the deploy keys
	 * @return the deploy keys
	 */
	public MultisigBuilder setDeployKeys(Credentials deployKeys) {
		this.deployKeys = deployKeys;
		return this;
	}

	/**
	 * Add owner multisig builder.
	 *
	 * @param pubkey the pubkey
	 * @return the multisig builder
	 */
	public MultisigBuilder addOwner(BigInteger pubkey) {
		publicKeys.add(pubkey);
		return this;
	}

	/**
	 * Remove owner multisig builder.
	 *
	 * @param pubkey the pubkey
	 * @return the multisig builder
	 */
	public MultisigBuilder removeOwner(BigInteger pubkey) {
		publicKeys.remove(pubkey);
		return this;
	}

	/**
	 * Type multisig contract . type.
	 *
	 * @return the multisig contract . type
	 */
	public MultisigContract.Type type() {
		return type;
	}

	/**
	 * Sets type.
	 *
	 * @param type the type
	 * @return the type
	 */
	public MultisigBuilder setType(MultisigContract.Type type) {
		this.type = type;
		return this;
	}

	/**
	 * Confirmations int.
	 *
	 * @return the int
	 */
	public int confirmations() {
		return confirmations;
	}

	/**
	 * Sets confirmations.
	 *
	 * @param confirmations the confirmations
	 * @return the confirmations
	 */
	public MultisigBuilder setConfirmations(int confirmations) {
		this.confirmations = confirmations;
		return this;
	}
}
