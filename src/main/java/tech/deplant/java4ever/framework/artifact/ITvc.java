package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.IAbi;

import java.util.Map;

public interface ITvc {

	byte[] bytes();

	String base64String();

	public Map<String, Object> decodeInitialData(Sdk sdk, IAbi abi);

	public String decodeInitialPubkey(Sdk sdk, IAbi abi);

	String code(Sdk sdk) throws Sdk.SdkException;
}
