package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.java4ever.utils.Convert;

import java.math.BigInteger;
import java.util.Map;

import static java.util.Objects.requireNonNullElse;

public record Address(int wid, BigInteger value) implements AbiType<String, String> {

	public static final Address ZERO = new Address(0, BigInteger.ZERO);

	@JsonCreator
	public Address(String address) {
		this(Integer.parseInt(address.split(":")[0]), new BigInteger(address.split(":")[1], 16));
	}

	public Address(BigInteger value) {
		this(0, value);
	}

	public static Address fromJava(Object input) {
		return switch (input) {
			case Address a -> a;
			case String s -> new Address(s);
			case BigInteger bi -> new Address(0, bi);
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	public static Address fromABI(Object output) {
		return new Address(output.toString());
	}

	public static Address ofNullable(Object nullableObject) {
		return (null == nullableObject) ? Address.ZERO : new Address(nullableObject.toString());
	}

	public static String ofFutureDeploy(Sdk sdk,
	                                    Template template,
	                                    int workchainId,
	                                    Map<String, Object> initialData,
	                                    Credentials credentials) throws EverSdkException {
		return Abi.encodeMessage(
				sdk.context(),
				template.abi().ABI(),
				null,
				new Abi.DeploySet(template.tvc().base64String(),
								  null,
								  null,
				                  workchainId,
				                  initialData,
				                  requireNonNullElse(credentials, Credentials.NONE).publicKey()),
				null,
				requireNonNullElse(credentials, Credentials.NONE).signer(),
				null,
				null
		).address();
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

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "address";
	}

	@Override
	public String toJava() {
		return makeAddrStd();
	}

	@Override
	public String toABI() {
		return makeAddrStd();
	}
}
