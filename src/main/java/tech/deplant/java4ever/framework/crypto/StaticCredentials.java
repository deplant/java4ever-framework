package tech.deplant.java4ever.framework.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Crypto;
import tech.deplant.java4ever.framework.Sdk;

import java.io.FileWriter;
import java.io.IOException;

public record StaticCredentials(@JsonProperty("public") String publicKey,
                                @JsonProperty("secret") String secretKey) implements Credentials {

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
            file.write(Sdk.DEFAULT_MAPPER.writeValueAsString(this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }
}
