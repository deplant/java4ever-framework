package tech.deplant.java4ever.framework.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;
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
	public static Credentials RANDOM(Sdk sdk) throws Sdk.SdkException {
		var pair = Crypto.generateRandomSignKeys(sdk.context());
		return new Credentials(pair.publicKey(), pair.secretKey());
	}

	public static Credentials ofResource(String resourceName) {
		try {
			return Sdk.DEFAULT_MAPPER.readValue(new JsonResource(resourceName).get(), Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	public static Credentials ofFile(String filePath) {
		try {
			return Sdk.DEFAULT_MAPPER.readValue(new JsonFile(filePath).get(), Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	//TODO Convert to Async
	private static Credentials ofSeed(Sdk sdk, Seed seed) throws Sdk.SdkException {
		if (Crypto.mnemonicVerify(sdk.context(), seed.phrase(), null, seed.words()).valid()) {
			var pairFromSeed = Crypto.mnemonicDeriveSignKeys(sdk.context(), seed, null, null, words);
			return new Credentials(pairFromSeed.publicKey(), pairFromSeed.secretKey());
		} else {
			throw new RuntimeException("Seed/mnemonic phrase checksum is not valid.");
		}

	}

	//TODO Convert to Async
	public static Credentials ofSeed12(Sdk sdk, String seed) throws Sdk.SdkException {
		return ofSeed(sdk, seed, 12);
	}

	//TODO Convert to Async
	public static Credentials ofSeed24(Sdk sdk, String seed) throws Sdk.SdkException {
		return ofSeed(sdk, seed, 24);
	}

	public Abi.Signer signer() {
		return new Abi.Signer.Keys(keyPair());
	}

	public Crypto.KeyPair keyPair() {
		return new Crypto.KeyPair(this.publicKey, this.secretKey);
	}

	public String publicKeyTonSafe(Sdk sdk) throws Sdk.SdkException {
		return Crypto.convertPublicKeyToTonSafeFormat(sdk.context(), this.publicKey).tonPublicKey();
	}
}
