package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractTemplate;

import java.math.BigInteger;
import java.util.Map;

public record Address(int wid, BigInteger value) {

	public static final Address ZERO = new Address(0, BigInteger.ZERO);

	public Address(String address) {
		this(Integer.parseInt(address.split(":")[0]), new BigInteger(address.split(":")[1], 16));
	}

	public Address(BigInteger value) {
		this(0, value);
	}

	public static Address ofNullable(Object nullableObject) {
		return (null == nullableObject) ? Address.ZERO : new Address(nullableObject.toString());
	}

	public static Address ofFutureDeploy(Sdk sdk,
	                                     ContractTemplate template,
	                                     int workchainId,
	                                     Map<String, Object> initialData,
	                                     Credentials credentials) throws EverSdkException {
		return new Address(Abi.encodeMessage(
				sdk.context(),
				template.abi().ABI(),
				null,
				new Abi.DeploySet(template.tvc().base64String(), workchainId, initialData, credentials.publicKey()),
				null,
				credentials.signer(),
				null
		).address());
	}

	public boolean isNull() {
		return this.value.equals(BigInteger.ZERO);
	}

	//TODO Add definition of different address types

	/**
	 * @return Returns type of the address:
	 * 0 - addr_none 1 - addr_extern 2 - addr_std
	 */
	public int getType() {
		return 0;
	}

	public String makeAddrStd() {
		return
				wid() +
				":" +
				Convert.padLeftZeros(value().toString(16), 64);
	}
}
