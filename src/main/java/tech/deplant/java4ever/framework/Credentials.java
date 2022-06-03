package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.framework.artifact.Artifact;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Log4j2
public record Credentials(@SerializedName("public") String publicKey, @SerializedName("secret") String secretKey) {


    public final static Credentials NONE = new Credentials(
            "0000000000000000000000000000000000000000000000000000000000000000",
            "0000000000000000000000000000000000000000000000000000000000000000"
    );

//    private static String generateEntropyWithJava() {
//        SecureRandom sr = new SecureRandom();
//        byte[] rndBytes = new byte[512];
//        sr.nextBytes(rndBytes);
//        return sr.;
//    }

    private static String generateEntropyWithSDK(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Crypto.generateRandomBytes(sdk.context(), 512)).bytes();
    }

    private static String generateSeedOfEntropy(Sdk sdk, Number words, String entropyBase64) throws Sdk.SdkException {
        return sdk.syncCall(Crypto.mnemonicFromEntropy(sdk.context(), Data.base64ToHexString(entropyBase64).toUpperCase(Locale.ROOT), null, words)).phrase();
    }

    private static String generateSeedOfRandom(Sdk sdk, Number words) throws Sdk.SdkException {
        return generateSeedOfEntropy(sdk, words, generateEntropyWithSDK(sdk));
    }

    public static String generateSeed12(Sdk sdk) throws Sdk.SdkException {
        return generateSeedOfRandom(sdk, 12);
    }

    public static String generateSeed24(Sdk sdk) throws Sdk.SdkException {
        return generateSeedOfRandom(sdk, 24);
    }

    public static CompletableFuture<Credentials> ofArtifact(Sdk sdk, Artifact<String> artifact) {
        return CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return sdk.mapper().readValue(artifact.read(), Credentials.class);
                    } catch (JsonProcessingException e) {
                        log.error("JSON parsing error! \"" + e.getMessage() + "\"");
                        return Credentials.NONE;
                    }
                });
    }

    /**
     * Generates new random KeyPair by using crypto.generate_random_sign_keys() method of SDK.
     */
    public static CompletableFuture<Credentials> RANDOM(Sdk sdk) throws Sdk.SdkException {
        return Crypto.generateRandomSignKeys(sdk.context()).thenApply(pair -> new Credentials(pair.publicKey(), pair.secretKey()));
    }

    //TODO Convert to Async
    private static Credentials ofSeed(Sdk sdk, String seed, Number words) throws Sdk.SdkException {
        if (sdk.syncCall(Crypto.mnemonicVerify(sdk.context(), seed, null, words)).valid()) {
            var pairFromSeed = sdk.syncCall(Crypto.mnemonicDeriveSignKeys(sdk.context(), seed, null, null, words));
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

    //TODO Convert to Async
    public String publicKeyTonSafe(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Crypto.convertPublicKeyToTonSafeFormat(sdk.context(), this.publicKey)).tonPublicKey();
    }

    //TODO Remove
    public void store(String path) throws IOException {
        FileWriter file = new FileWriter(path);
        try {
            file.write(new Gson().toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }
}
