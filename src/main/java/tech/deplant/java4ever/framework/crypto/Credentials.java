package tech.deplant.java4ever.framework.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;

public record Credentials(@JsonProperty("public") String publicKey,
                          @JsonProperty("secret") String secretKey) {

	public static final Credentials NONE = new Credentials(
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000000"
	);

	/**
	 * Generates new random KeyPair by using crypto.generate_random_sign_keys() method of SDK.
	 */
	public static Credentials RANDOM(Sdk sdk) throws EverSdkException {
		var pair = Crypto.generateRandomSignKeys(sdk.context());
		return new Credentials(pair.publicKey(), pair.secretKey());
	}

	public static Credentials ofResource(String resourceName) {
		try {
			return ContextBuilder.DEFAULT_MAPPER.readValue(new JsonResource(resourceName).get(), Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	public static Credentials ofFile(String filePath) {
		try {
			return ContextBuilder.DEFAULT_MAPPER.readValue(new JsonFile(filePath).get(), Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	public static Credentials ofSeed(Sdk sdk, String seedString, int seedWords) throws EverSdkException {
		return ofSeed(sdk, new Seed(seedString, seedWords));
	}

	//TODO Convert to Async
	public static Credentials ofSeed(Sdk sdk, Seed seed) throws EverSdkException {
		if (Crypto.mnemonicVerify(sdk.context(), seed.phrase(), null, seed.words()).valid()) {
			var pairFromSeed = Crypto.mnemonicDeriveSignKeys(sdk.context(), seed.phrase(), null, null, seed.words());
			return new Credentials(pairFromSeed.publicKey(), pairFromSeed.secretKey());
		} else {
			throw new RuntimeException("Seed/mnemonic phrase checksum is not valid.");
		}
	}

	public Abi.Signer signer() {
		if (this.equals(Credentials.NONE)) {
			return new Abi.Signer.None();
		} else {
			return new Abi.Signer.Keys(keyPair());
		}
	}

	public Crypto.KeyPair keyPair() {
		return new Crypto.KeyPair(this.publicKey, this.secretKey);
	}

	public String publicKeyTonSafe(Sdk sdk) throws EverSdkException {
		return Crypto.convertPublicKeyToTonSafeFormat(sdk.context(), this.publicKey).tonPublicKey();
	}
}
