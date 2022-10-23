package tech.deplant.java4ever.framework.abi;

import tech.deplant.java4ever.framework.Address;

public record AbiAddress(String addressString) implements AbiValue {

	public AbiAddress(Address address) {
		this(address.makeAddrStd());
	}

	public static Address deserialize(Object obj) {
		var str = obj.toString();
		return new Address(str);
	}

	@Override
	public Object serialize() {
		return this.addressString;
	}
}