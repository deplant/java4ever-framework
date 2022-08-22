package tech.deplant.java4ever.framework.crypto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.framework.Sdk;

import java.io.FileWriter;
import java.io.IOException;

public record StaticCredentials(@SerializedName("public") String publicKey,
                                @SerializedName("secret") String secretKey) implements Credentials {

    @Override
    public Abi.Signer signer() {
        return new Abi.Signer.Keys(keyPair());
    }

    @Override
    public Crypto.KeyPair keyPair() {
        return new Crypto.KeyPair(this.publicKey, this.secretKey);
    }

    //TODO Convert to Async
    @Override
    public String publicKeyTonSafe(Sdk sdk) throws Sdk.SdkException {
        return Crypto.convertPublicKeyToTonSafeFormat(sdk.context(), this.publicKey).tonPublicKey();
    }

    //TODO Remove
    @Override
    public void store(String path) throws IOException {
        FileWriter file = null;
        try {
            file = new FileWriter(path);
            file.write(new Gson().toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }
}
