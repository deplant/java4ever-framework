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

	public Builder toBuilder(Sdk sdk) throws EverSdkException {
		var decoded = decode(sdk);
		return new Builder(decoded.code(),
		                   decoded.data(),
		                   decoded.library(),
		                   decoded.tick(),
		                   decoded.tock(),
		                   decoded.splitDepth(),
		                   decoded.compilerVersion());
	}

	public static class Builder {

		private String code;
		//String codeHash;
		//Number codeDepth;
		private String data;
		//String dataHash;
		//Number dataDepth;
		private String library;
		private Boolean tick;
		private Boolean tock;
		private Number splitDepth;
		private String compilerVersion;

		public Builder() {
		}

		private Builder(String code,
		                String data,
		                String library,
		                Boolean tick,
		                Boolean tock,
		                Number splitDepth,
		                String compilerVersion) {
			this.code = code;
			this.data = data;
			this.library = library;
			this.tick = tick;
			this.tock = tock;
			this.splitDepth = splitDepth;
			this.compilerVersion = compilerVersion;
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		private Builder data(String data) {
			this.data = data;
			return this;
		}

		private Builder library(String library) {
			this.library = library;
			return this;
		}

		private Builder tick(boolean tick) {
			this.tick = tick;
			return this;
		}

		private Builder tock(boolean tock) {
			this.tock = tock;
			return this;
		}

		private Builder splitDepth(Number splitDepth) {
			this.splitDepth = splitDepth;
			return this;
		}

		private Builder compilerVersion(String compilerVersion) {
			this.compilerVersion = compilerVersion;
			return this;
		}

		public ContractTvc build(Sdk sdk) throws EverSdkException {
			return ContractTvc.ofBase64String(Boc.encodeTvc(sdk.context(),
			                                                this.code,
			                                                this.data,
			                                                this.library,
			                                                this.tick,
			                                                this.tock,
			                                                this.splitDepth,
			                                                null).tvc());
		}
	}
}