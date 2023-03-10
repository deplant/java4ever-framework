package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.binding.EverSdkException;

public record Seed(String phrase, int words) {

	public final static int DEFAULT_WORD_COUNT = 12; //Mnemonic word count
	public final static int DICTIONARY_ENGLISH = 1; //Dictionary identifier

	public final static String HD_PATH = "m/44'/396'/0'/0/0";

	private static String generateMnemonicPhraseFromRandom(Sdk sdk, int words) throws EverSdkException {
		return Crypto.mnemonicFromRandom(sdk.context(), Crypto.MnemonicDictionary.English, words).phrase();
	}

	/**
	 * Generates random 12-words seed phrase
	 *
	 * @param sdk
	 * @return
	 */
	public static Seed RANDOM(Sdk sdk) throws EverSdkException {
		return new Seed(Crypto.mnemonicFromRandom(sdk.context(), Crypto.MnemonicDictionary.English, DEFAULT_WORD_COUNT).phrase(), DEFAULT_WORD_COUNT);
	}

	public static Seed RANDOM_TON(Sdk sdk) throws EverSdkException {
		return new Seed(Crypto.mnemonicFromRandom(sdk.context(), Crypto.MnemonicDictionary.Ton, 24).phrase(), 24);
	}

}
