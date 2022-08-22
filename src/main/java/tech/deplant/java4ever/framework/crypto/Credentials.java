package tech.deplant.java4ever.framework.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.framework.Data;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Artifact;

import java.io.IOException;
import java.util.Locale;

public interface Credentials {
    Credentials NONE = new StaticCredentials(
            "0000000000000000000000000000000000000000000000000000000000000000",
            "0000000000000000000000000000000000000000000000000000000000000000"
    );

    /**
     * Generates new random KeyPair by using crypto.generate_random_sign_keys() method of SDK.
     */
    static Credentials RANDOM(Sdk sdk) throws Sdk.SdkException {
        var pair = Crypto.generateRandomSignKeys(sdk.context());
        return new StaticCredentials(pair.publicKey(), pair.secretKey());
    }

    private static String generateEntropyWithSDK(Sdk sdk) throws Sdk.SdkException {
        return Crypto.generateRandomBytes(sdk.context(), 512).bytes();
    }

    private static String generateSeedOfEntropy(Sdk sdk, Number words, String entropyBase64) {
        return Crypto.mnemonicFromEntropy(sdk.context(), Data.base64ToHexString(entropyBase64).toUpperCase(Locale.ROOT), null, words).phrase();
    }

    private static String generateSeedOfRandom(Sdk sdk, Number words) {
        return generateSeedOfEntropy(sdk, words, generateEntropyWithSDK(sdk));
    }

    static String generateSeed12(Sdk sdk) {
        return generateSeedOfRandom(sdk, 12);
    }

    static String generateSeed24(Sdk sdk) {
        return generateSeedOfRandom(sdk, 24);
    }

    static Credentials ofArtifact(Sdk sdk, Artifact<String> artifact) {
        try {
            return sdk.mapper().readValue(artifact.read(), Credentials.class);
        } catch (JsonProcessingException e) {
            return Credentials.NONE;
        }
    }

    //TODO Convert to Async
    private static Credentials ofSeed(Sdk sdk, String seed, Number words) throws Sdk.SdkException {
        if (Crypto.mnemonicVerify(sdk.context(), seed, null, words).valid()) {
            var pairFromSeed = Crypto.mnemonicDeriveSignKeys(sdk.context(), seed, null, null, words);
            return new StaticCredentials(pairFromSeed.publicKey(), pairFromSeed.secretKey());
        } else {
            throw new RuntimeException("Seed/mnemonic phrase checksum is not valid.");
        }

    }

    //TODO Convert to Async
    static Credentials ofSeed12(Sdk sdk, String seed) throws Sdk.SdkException {
        return ofSeed(sdk, seed, 12);
    }

    //TODO Convert to Async
    static Credentials ofSeed24(Sdk sdk, String seed) throws Sdk.SdkException {
        return ofSeed(sdk, seed, 24);
    }

    String publicKey();

    String secretKey();

    Abi.Signer signer();

    public Crypto.KeyPair keyPair();

    public String publicKeyTonSafe(Sdk sdk);

    public void store(String path) throws IOException;

}