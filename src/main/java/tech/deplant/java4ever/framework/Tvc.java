package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.ByteFile;
import tech.deplant.java4ever.framework.artifact.ByteResource;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.util.Base64;
import java.util.Map;

public record Tvc(byte[] bytes) {

	public static Tvc ofFile(String filePath) {
		return new Tvc(new ByteFile(filePath).get());
	}

	public static Tvc ofResource(String resourceName) {
		return new Tvc(new ByteResource(resourceName).get());
	}

	public static Tvc ofBase64String(String base64) {
		return new Tvc(Base64.getDecoder().decode(base64));
	}

	public String base64String() {
		return Base64.getEncoder().encodeToString(bytes());
	}

	public JsonNode decodeInitialData(int sdk, ContractAbi abi) throws EverSdkException {
		return Abi.decodeInitialData(sdk, abi.ABI(), decode(sdk).data(), false).initialData();
	}

	public String encodeInitialData(int sdk,
	                                ContractAbi abi,
	                                Map<String, Object> initData,
	                                String pubkey) throws EverSdkException {
		return Abi.encodeInitialData(sdk, abi.ABI(), JsonContext.ABI_JSON_MAPPER().valueToTree(initData), pubkey, null).data();
	}

	public String decodeInitialPubkey(int sdk, ContractAbi abi) throws EverSdkException {
		return Abi.decodeInitialData(sdk, abi.ABI(), decode(sdk).data(), false).initialPubkey();
	}

	public String code(int sdk) throws EverSdkException {
		return decode(sdk).code();
	}

	public TvmCell codeCell(int sdk) throws EverSdkException {
		return new TvmCell(decode(sdk).code());
	}

	public Boc.ResultOfDecodeStateInit decode(int sdk) throws EverSdkException {
		return Boc.decodeStateInit(sdk, base64String(), null);
	}

	public String data(int sdk) throws EverSdkException {
		return decode(sdk).data();
	}

	public String saltedCode(int sdk, String salt) throws EverSdkException {
		return Boc.setCodeSalt(sdk, code(sdk), salt, null).code();
	}

	public String codeHash(int sdk) throws EverSdkException {
		return Boc.getBocHash(sdk, code(sdk)).hash();
	}

	public Number codeDepth(int sdk) throws EverSdkException {
		return Boc.getBocDepth(sdk, code(sdk)).depth();
	}

	public String saltedCodeHash(int sdk, String salt) throws EverSdkException {
		return Boc.getBocHash(sdk, saltedCode(sdk, salt)).hash();
	}

	public Tvc withUpdatedInitialData(int sdk,
	                                  ContractAbi abi,
	                                  Map<String, Object> initialData,
	                                  String publicKey) throws EverSdkException {
		String updatedDataString = Abi.updateInitialData(sdk,
		                                                 abi.ABI(),
		                                                 data(sdk),
		                                                 JsonContext.ABI_JSON_MAPPER().valueToTree(abi.convertInitDataInputs(initialData)),
		                                                                                           publicKey,
		                                                                                           null).data();
		var decoded = decode(sdk);
		var newTvcString = Boc.encodeStateInit(sdk,
		                                 decoded.code(),
		                                 updatedDataString,
		                                 decoded.library(),
		                                 decoded.tick(),
		                                 decoded.tock(),
		                                 decoded.splitDepth(),
		                                 null).stateInit();
		return Tvc.ofBase64String(newTvcString);
	}

	public Builder toBuilder(int sdk) throws EverSdkException {
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
		private Long splitDepth;
		private String compilerVersion;

		public Builder() {
		}

		private Builder(String code,
		                String data,
		                String library,
		                Boolean tick,
		                Boolean tock,
		                Long splitDepth,
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

		private Builder splitDepth(Long splitDepth) {
			this.splitDepth = splitDepth;
			return this;
		}

		private Builder compilerVersion(String compilerVersion) {
			this.compilerVersion = compilerVersion;
			return this;
		}

		public Tvc build(int sdk) throws EverSdkException {
			return Tvc.ofBase64String(Boc.encodeStateInit(sdk,
			                                        this.code,
			                                        this.data,
			                                        this.library,
			                                        this.tick,
			                                        this.tock,
			                                        this.splitDepth,
			                                        null).stateInit());
		}
	}
}