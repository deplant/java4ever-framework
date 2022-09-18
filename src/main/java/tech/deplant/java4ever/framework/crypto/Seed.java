package tech.deplant.java4ever.framework.crypto;

import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.framework.Data;
import tech.deplant.java4ever.framework.Sdk;

import java.util.Locale;

public record Seed(String phrase, int words) {

	public static Seed ofBase64StringEntropy(Sdk sdk, int words, String entropy) {
		return new Seed(generateSeedOfEntropy(sdk, words, entropy), words);
	}

	private static String generateEntropyWithSDK(Sdk sdk) throws Sdk.SdkException {
		return Crypto.generateRandomBytes(sdk.context(), 512).bytes();
	}

	private static String generateSeedOfEntropy(Sdk sdk, int words, String entropyBase64) {
		return Crypto.mnemonicFromEntropy(sdk.context(),
		                                  Data.base64ToHexString(entropyBase64).toUpperCase(Locale.ROOT),
		                                  null,
		                                  words).phrase();
	}

	private static String generateSeedOfRandom(Sdk sdk, int words) {
		return generateSeedOfEntropy(sdk, words, generateEntropyWithSDK(sdk));
	}

	public static Seed RANDOM12(Sdk sdk) {
		return new Seed(generateSeedOfRandom(sdk, 12), 12);
	}

	public static Seed RANDOM24(Sdk sdk) {
		return new Seed(generateSeedOfRandom(sdk, 24), 24);
	}

}
