package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ByteFile;
import tech.deplant.java4ever.framework.artifact.ByteResource;

import java.util.Base64;
import java.util.Map;

public record ContractTvc(byte[] bytes) {

	public static ContractTvc ofFile(String filePath) {
		return new ContractTvc(new ByteFile(filePath).get());
	}

	public static ContractTvc ofResource(String resourceName) {
		return new ContractTvc(new ByteResource(resourceName).get());
	}

	public static ContractTvc ofBase64String(String base64) {
		return new ContractTvc(Base64.getDecoder().decode(base64));
	}

	public String base64String() {
		return Base64.getEncoder().encodeToString(bytes());
	}

	public Map<String, Object> decodeInitialData(Sdk sdk, ContractAbi abi) throws EverSdkException {
		return sdkDecodeInitialData(sdk, abi).initialData();
	}

	public String decodeInitialPubkey(Sdk sdk, ContractAbi abi) throws EverSdkException {
		return sdkDecodeInitialData(sdk, abi).initialPubkey();
	}

	public String code(Sdk sdk) throws EverSdkException {
		return Boc.getCodeFromTvc(sdk.context(), base64String()).code();
	}

	public ContractTvc withUpdatedInitialData(Sdk sdk,
	                                          ContractAbi abi,
	                                          Map<String, Object> initialData,
	                                          String publicKey) throws EverSdkException {
		String str = Abi.updateInitialData(sdk.context(),
		                                   abi.ABI(),
		                                   base64String(),
		                                   initialData,
		                                   publicKey,
		                                   Boc.BocCacheType.UNPINNED).data();
		return ContractTvc.ofBase64String(str);
	}

	private Abi.ResultOfDecodeInitialData sdkDecodeInitialData(Sdk sdk, ContractAbi abi) throws EverSdkException {
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), base64String(), false);
	}
}