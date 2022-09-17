package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.IAbi;

import java.util.Base64;
import java.util.Map;

public record CachedTvc(byte[] bytes) implements ITvc {

	public static CachedTvc ofResource(String resourceName) {
		return new CachedTvc(new ByteResource(resourceName).get());
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

	private Abi.ResultOfDecodeInitialData sdkDecodeInitialData(Sdk sdk, IAbi abi) {
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), base64String(), false);
	}
}