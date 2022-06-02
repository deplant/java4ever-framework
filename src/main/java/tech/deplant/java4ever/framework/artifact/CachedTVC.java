package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;

import java.util.Base64;

public record CachedTVC(byte[] tvc) implements ITvc {

    @Override
    public String base64String() {
        return Base64.getEncoder().encodeToString(this.tvc);
    }

    @Override
    public String code(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Boc.getCodeFromTvc(sdk.context(), base64String())).code();
    }
}
