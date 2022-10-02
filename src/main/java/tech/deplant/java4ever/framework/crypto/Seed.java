package tech.deplant.java4ever.framework.crypto;

import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;

public record Seed(String phrase, int words) {

	public final static int DEFAULT_WORD_COUNT = 12; //Mnemonic word count
	public final static int DICTIONARY_ENGLISH = 1; //Dictionary identifier

	public final static String HD_PATH = "m/44'/396'/0'/0/0";

	private static String generateMnemonicPhraseFromRandom(Sdk sdk, int words) throws EverSdkException {
		return Crypto.mnemonicFromRandom(sdk.context(), DICTIONARY_ENGLISH, words).phrase();
	}

	/**
	 * Generates random 12-words seed phrase
	 *
	 * @param sdk
	 * @return
	 */
	public static Seed RANDOM(Sdk sdk) throws EverSdkException {
		return new Seed(generateMnemonicPhraseFromRandom(sdk, DEFAULT_WORD_COUNT), DEFAULT_WORD_COUNT);
	}

	public static Seed RANDOM24(Sdk sdk) throws EverSdkException {
		return new Seed(generateMnemonicPhraseFromRandom(sdk, 24), 24);
	}

}
