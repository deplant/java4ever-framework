package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.Sdk;

public interface ITvc {

	byte[] bytes();

	String base64String();

	String code(Sdk sdk) throws Sdk.SdkException;
}
