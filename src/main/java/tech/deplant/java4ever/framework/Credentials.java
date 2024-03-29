package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.commons.Numbers;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;

import java.math.BigInteger;

/**
 * {@snippet :
 *  var keys = Credentials.RANDOM(sdk);
 * 	String sk = keys.secretKey();
 * 	String pk = keys.publicKey();
 *}
 *
 * @param publicKey
 * @param secretKey
 */
public record Credentials(@JsonProperty("public") String publicKey, @JsonProperty("secret") String secretKey) {

	public static final Credentials NONE = new Credentials(
			"0000000000000000000000000000000000000000000000000000000000000000",
			"0000000000000000000000000000000000000000000000000000000000000000");

	/**
	 * Generates new random KeyPair by using crypto.generate_random_sign_keys() method of SDK.
	 */
	public static Credentials RANDOM(int contextId) throws EverSdkException {
		var pair = Crypto.generateRandomSignKeys(contextId);
		return new Credentials(pair.publicKey(), pair.secretKey());
	}

	public static Credentials ofResource(String resourceName) {
		try {
			return JsonContext.ABI_JSON_MAPPER().readValue(new JsonResource(resourceName).get(),
			                                               Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	public static Credentials ofFile(String filePath) {
		try {
			return JsonContext.ABI_JSON_MAPPER().readValue(new JsonFile(filePath).get(), Credentials.class);
		} catch (JsonProcessingException e) {
			return NONE;
		}
	}

	public static Credentials ofSeed(int contextId, String seedString, int seedWords) throws EverSdkException {
		return ofSeed(contextId, new Seed(seedString, seedWords));
	}

	//TODO Convert to Async
	public static Credentials ofSeed(int contextId, Seed seed) throws EverSdkException {
		if (Crypto.mnemonicVerify(contextId, seed.phrase(), null, seed.words()).valid()) {
			var pairFromSeed = Crypto.mnemonicDeriveSignKeys(contextId, seed.phrase(), null, null, seed.words());
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

	public String publicKeyTonSafe(int contextId) throws EverSdkException {
		return Crypto.convertPublicKeyToTonSafeFormat(contextId, this.publicKey).tonPublicKey();
	}

	public BigInteger publicBigInt() {
		return Numbers.hexStringToBigInt(publicKey());
	}

	public BigInteger secretBigInt() {
		return Numbers.hexStringToBigInt(secretKey());
	}
}
