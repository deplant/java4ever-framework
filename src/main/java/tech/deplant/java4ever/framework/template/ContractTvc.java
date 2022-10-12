package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
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
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), decode(sdk).data(), false).initialData();
	}

	public String encodeInitialData(Sdk sdk,
	                                ContractAbi abi,
	                                Map<String, Object> initData,
	                                String pubkey) throws EverSdkException {
		return Abi.encodeInitialData(sdk.context(), abi.ABI(), initData, pubkey, null).data();
	}

	public String decodeInitialPubkey(Sdk sdk, ContractAbi abi) throws EverSdkException {
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), decode(sdk).data(), false).initialPubkey();
	}

	public String code(Sdk sdk) throws EverSdkException {
		return decode(sdk).code();
	}

	public Boc.ResultOfDecodeTvc decode(Sdk sdk) throws EverSdkException {
		return Boc.decodeTvc(sdk.context(), base64String(), null);
	}

	public String data(Sdk sdk) throws EverSdkException {
		return decode(sdk).data();
	}

	public String saltedCode(Sdk sdk, String salt) throws EverSdkException {
		return Boc.setCodeSalt(sdk.context(), code(sdk), salt, null).code();
	}

	public String codeHash(Sdk sdk) throws EverSdkException {
		return Boc.getBocHash(sdk.context(), code(sdk)).hash();
	}

	public Number codeDepth(Sdk sdk) throws EverSdkException {
		return Boc.getBocDepth(sdk.context(), code(sdk)).depth();
	}

	public String saltedCodeHash(Sdk sdk, String salt) throws EverSdkException {
		return Boc.getBocHash(sdk.context(), saltedCode(sdk, salt)).hash();
	}

	public ContractTvc withUpdatedInitialData(Sdk sdk,
	                                          ContractAbi abi,
	                                          Map<String, Object> initialData,
	                                          String publicKey) throws EverSdkException {
		String updatedDataString = Abi.updateInitialData(sdk.context(),
		                                                 abi.ABI(),
		                                                 data(sdk),
		                                                 abi.convertInitDataInputs(initialData),
		                                                 publicKey,
		                                                 null).data();
		var decoded = decode(sdk);
		var newTvcString = Boc.encodeTvc(sdk.context(),
		                                 decoded.code(),
		                                 updatedDataString,
		                                 decoded.library(),
		                                 decoded.tick(),
		                                 decoded.tock(),
		                                 decoded.splitDepth(),
		                                 null).tvc();
		return ContractTvc.ofBase64String(newTvcString);
	}
}