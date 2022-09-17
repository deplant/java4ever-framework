package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;

import java.util.Base64;

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
	public String code(Sdk sdk) throws Sdk.SdkException {
		return Boc.getCodeFromTvc(sdk.context(), base64String()).code();
	}
}