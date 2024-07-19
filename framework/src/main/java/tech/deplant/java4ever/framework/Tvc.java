package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.artifact.ByteFile;
import tech.deplant.java4ever.framework.artifact.ByteResource;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.util.Base64;
import java.util.Map;

/**
 * The type Tvc.
 */
public record Tvc(byte[] bytes) {

	/**
	 * Of file tvc.
	 *
	 * @param filePath the file path
	 * @return the tvc
	 */
	public static Tvc ofFile(String filePath) {
		return new Tvc(new ByteFile(filePath).get());
	}

	/**
	 * Of resource tvc.
	 *
	 * @param resourceName the resource name
	 * @return the tvc
	 */
	public static Tvc ofResource(String resourceName) {
		return new Tvc(new ByteResource(resourceName).get());
	}

	/**
	 * Of base 64 string tvc.
	 *
	 * @param base64 the base 64
	 * @return the tvc
	 */
	public static Tvc ofBase64String(String base64) {
		return new Tvc(Base64.getDecoder().decode(base64));
	}

	/**
	 * Base 64 string string.
	 *
	 * @return the string
	 */
	public String base64String() {
		return Base64.getEncoder().encodeToString(bytes());
	}

	/**
	 * Decode initial data json node.
	 *
	 * @param sdk the sdk
	 * @param abi the abi
	 * @return the json node
	 * @throws EverSdkException the ever sdk exception
	 */
	public JsonNode decodeInitialData(int sdk, ContractAbi abi) throws EverSdkException {
		return EverSdk.await(Abi.decodeInitialData(sdk, abi.ABI(), decode(sdk).data(), false)).initialData();
	}

	/**
	 * Encode initial data string.
	 *
	 * @param sdk      the sdk
	 * @param abi      the abi
	 * @param initData the init data
	 * @param pubkey   the pubkey
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String encodeInitialData(int sdk,
	                                ContractAbi abi,
	                                Map<String, Object> initData,
	                                String pubkey) throws EverSdkException {
		return EverSdk.await(Abi.encodeInitialData(sdk,
		                                           abi.ABI(),
		                                           JsonContext.ABI_JSON_MAPPER().valueToTree(initData),
		                                           pubkey,
		                                           null))
		              .data();
	}

	/**
	 * Decode initial pubkey string.
	 *
	 * @param sdk the sdk
	 * @param abi the abi
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String decodeInitialPubkey(int sdk, ContractAbi abi) throws EverSdkException {
		return EverSdk.await(Abi.decodeInitialData(sdk, abi.ABI(), decode(sdk).data(), false)).initialPubkey();
	}

	/**
	 * Code string.
	 *
	 * @param sdk the sdk
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String code(int sdk) throws EverSdkException {
		return decode(sdk).code();
	}

	/**
	 * Code cell tvm cell.
	 *
	 * @param sdk the sdk
	 * @return the tvm cell
	 * @throws EverSdkException the ever sdk exception
	 */
	public TvmCell codeCell(int sdk) throws EverSdkException {
		return new TvmCell(decode(sdk).code());
	}

	/**
	 * Decode boc . result of decode state init.
	 *
	 * @param sdk the sdk
	 * @return the boc . result of decode state init
	 * @throws EverSdkException the ever sdk exception
	 */
	public Boc.ResultOfDecodeStateInit decode(int sdk) throws EverSdkException {
		return EverSdk.await(Boc.decodeStateInit(sdk, base64String(), null));
	}

	/**
	 * Data string.
	 *
	 * @param sdk the sdk
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String data(int sdk) throws EverSdkException {
		return decode(sdk).data();
	}

	/**
	 * Salted code string.
	 *
	 * @param sdk  the sdk
	 * @param salt the salt
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String saltedCode(int sdk, String salt) throws EverSdkException {
		return EverSdk.await(Boc.setCodeSalt(sdk, code(sdk), salt, null)).code();
	}

	/**
	 * Code hash string.
	 *
	 * @param sdk the sdk
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String codeHash(int sdk) throws EverSdkException {
		return EverSdk.await(Boc.getBocHash(sdk, code(sdk))).hash();
	}

	/**
	 * Code depth number.
	 *
	 * @param sdk the sdk
	 * @return the number
	 * @throws EverSdkException the ever sdk exception
	 */
	public Number codeDepth(int sdk) throws EverSdkException {
		return EverSdk.await(Boc.getBocDepth(sdk, code(sdk))).depth();
	}

	/**
	 * Salted code hash string.
	 *
	 * @param sdk  the sdk
	 * @param salt the salt
	 * @return the string
	 * @throws EverSdkException the ever sdk exception
	 */
	public String saltedCodeHash(int sdk, String salt) throws EverSdkException {
		return EverSdk.await(Boc.getBocHash(sdk, saltedCode(sdk, salt))).hash();
	}

	/**
	 * With updated initial data tvc.
	 *
	 * @param sdk         the sdk
	 * @param abi         the abi
	 * @param initialData the initial data
	 * @param publicKey   the public key
	 * @return the tvc
	 * @throws EverSdkException the ever sdk exception
	 */
	public Tvc withUpdatedInitialData(int sdk,
	                                  ContractAbi abi,
	                                  Map<String, Object> initialData,
	                                  String publicKey) throws EverSdkException {
		String updatedDataString = EverSdk.await(Abi.updateInitialData(sdk,
		                                                               abi.ABI(),
		                                                               data(sdk),
		                                                               JsonContext.ABI_JSON_MAPPER()
		                                                                          .valueToTree(abi.convertInitDataInputs(
				                                                                          initialData)),
		                                                               publicKey,
		                                                               null)).data();
		var decoded = decode(sdk);
		var newTvcString = EverSdk.await(Boc.encodeStateInit(sdk,
		                                                     decoded.code(),
		                                                     updatedDataString,
		                                                     decoded.library(),
		                                                     decoded.tick(),
		                                                     decoded.tock(),
		                                                     decoded.splitDepth(),
		                                                     null)).stateInit();
		return Tvc.ofBase64String(newTvcString);
	}

	/**
	 * To builder builder.
	 *
	 * @param sdk the sdk
	 * @return the builder
	 * @throws EverSdkException the ever sdk exception
	 */
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

	/**
	 * The type Builder.
	 */
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

		/**
		 * Instantiates a new Builder.
		 */
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

		/**
		 * Code builder.
		 *
		 * @param code the code
		 * @return the builder
		 */
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

		/**
		 * Build tvc.
		 *
		 * @param sdk the sdk
		 * @return the tvc
		 * @throws EverSdkException the ever sdk exception
		 */
		public Tvc build(int sdk) throws EverSdkException {
			return Tvc.ofBase64String(EverSdk.await(Boc.encodeStateInit(sdk,
			                                                            this.code,
			                                                            this.data,
			                                                            this.library,
			                                                            this.tick,
			                                                            this.tock,
			                                                            this.splitDepth,
			                                                            null)).stateInit());
		}
	}
}