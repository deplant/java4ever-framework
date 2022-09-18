package tech.deplant.java4ever.framework.template.tvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ByteFile;
import tech.deplant.java4ever.framework.artifact.ByteResource;
import tech.deplant.java4ever.framework.template.abi.IAbi;

import java.util.Base64;
import java.util.Map;

public record CachedTvc(byte[] bytes) implements ITvc {

	public static CachedTvc ofFile(String filePath) throws JsonProcessingException {
		return new CachedTvc(new ByteFile(filePath).get());
	}

	public static CachedTvc ofResource(String resourceName) {
		return new CachedTvc(new ByteResource(resourceName).get());
	}

	public static CachedTvc ofBase64String(String base64) {
		return new CachedTvc(Base64.getDecoder().decode(base64));
	}

	@Override
	public byte[] bytes() {
		return bytes();
	}

	@Override
	public String base64String() {
		return Base64.getEncoder().encodeToString(bytes());
	}

	@Override
	public Map<String, Object> decodeInitialData(Sdk sdk, IAbi abi) {
		return sdkDecodeInitialData(sdk, abi).initialData();
	}

	@Override
	public String decodeInitialPubkey(Sdk sdk, IAbi abi) {
		return sdkDecodeInitialData(sdk, abi).initialPubkey();
	}

	@Override
	public String code(Sdk sdk) throws Sdk.SdkException {
		return Boc.getCodeFromTvc(sdk.context(), base64String()).code();
	}

	@Override
	public ITvc withUpdatedInitialData(Sdk sdk, IAbi abi, Map<String, Object> initialData, String publicKey) {
		String str = Abi.updateInitialData(sdk.context(),
		                                   abi.ABI(),
		                                   base64String(),
		                                   initialData,
		                                   publicKey,
		                                   Boc.BocCacheType.UNPINNED).data();
		return CachedTvc.ofBase64String(str);
	}

	private Abi.ResultOfDecodeInitialData sdkDecodeInitialData(Sdk sdk, IAbi abi) {
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), base64String(), false);
	}
}