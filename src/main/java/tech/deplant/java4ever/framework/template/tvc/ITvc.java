package tech.deplant.java4ever.framework.template.tvc;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.template.abi.IAbi;

import java.util.Map;

public interface ITvc {

	byte[] bytes();

	String base64String();

	public Map<String, Object> decodeInitialData(Sdk sdk, IAbi abi);

	public String decodeInitialPubkey(Sdk sdk, IAbi abi);

	String code(Sdk sdk) throws Sdk.SdkException;

	public ITvc withUpdatedInitialData(Sdk sdk, IAbi abi, Map<String, Object> initialData, String publicKey);
}
