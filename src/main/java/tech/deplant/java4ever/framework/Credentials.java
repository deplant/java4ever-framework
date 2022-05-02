package tech.deplant.java4ever.framework;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

@Log4j2
@Value
public class Credentials {

    public final static Credentials NONE = new Credentials(
            "0000000000000000000000000000000000000000000000000000000000000000",
            "0000000000000000000000000000000000000000000000000000000000000000"
    );
    @SerializedName("public")
    String publicKey;
    @SerializedName("secret")
    String secretKey;

    private static String generateEntropy(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Crypto.generateRandomBytes(sdk.context(), 512)).bytes();
    }

    private static String generateSeedOfEntropy(Sdk sdk, Number words, String entropyBase64) throws Sdk.SdkException {
        return sdk.syncCall(Crypto.mnemonicFromEntropy(sdk.context(), Data.base64ToHexString(entropyBase64).toUpperCase(Locale.ROOT), null, words)).phrase();
    }

    private static String generateSeedOfRandom(Sdk sdk, Number words) throws Sdk.SdkException {
        return generateSeedOfEntropy(sdk, words, generateEntropy(sdk));
    }

    public static String generateSeed12(Sdk sdk) throws Sdk.SdkException {
        return generateSeedOfRandom(sdk, 12);
    }

    public static String generateSeed24(Sdk sdk) throws Sdk.SdkException {
        return generateSeedOfRandom(sdk, 24);
    }

    public static Credentials ofStored(String path) {
        String str = null;
        try {
            str = FileData.jsonFromFile(path);
        } catch (IOException e) {
            log.error("Path: {}, Error: {}", () -> path, () -> e.getMessage());
            return null;
        }
        JsonObject jsonRoot = JsonParser.parseString(ContractAbi.SAFE_MULTISIG.abiJsonString()).getAsJsonObject();
        return new Credentials(jsonRoot.get("public").getAsString(), jsonRoot.get("secret").getAsString());
    }

    /**
     * Generates new random KeyPair by using crypto.generate_random_sign_keys() method of SDK.
     */
    public static Credentials ofRandom(Sdk sdk) throws Sdk.SdkException {
        var randomPair = sdk.syncCall(Crypto.generateRandomSignKeys(sdk.context()));
        return new Credentials(randomPair.publicKey(), randomPair.secretKey());
    }

    private static Credentials ofSeed(Sdk sdk, String seed, Number words) throws Sdk.SdkException {
        if (sdk.syncCall(Crypto.mnemonicVerify(sdk.context(), seed, null, words)).valid()) {
            var pairFromSeed = sdk.syncCall(Crypto.mnemonicDeriveSignKeys(sdk.context(), seed, null, null, words));
            return new Credentials(pairFromSeed.publicKey(), pairFromSeed.secretKey());
        } else {
            throw new RuntimeException("Seed/mnemonic phrase checksum is not valid.");
        }

    }

    public static Credentials ofSeed12(Sdk sdk, String seed) throws Sdk.SdkException {
        return ofSeed(sdk, seed, 12);
    }

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
        return sdk.syncCall(Crypto.convertPublicKeyToTonSafeFormat(sdk.context(), this.publicKey)).tonPublicKey();
    }

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
