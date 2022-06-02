package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.Sdk;

public interface ITvc {

    public String base64String();
    public String code(Sdk sdk) throws Sdk.SdkException;
}
