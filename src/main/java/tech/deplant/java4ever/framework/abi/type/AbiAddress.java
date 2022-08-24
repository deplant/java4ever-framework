package tech.deplant.java4ever.framework.abi.type;

import tech.deplant.java4ever.framework.type.Address;

public record AbiAddress(String addressString) implements AbiValue {

    public AbiAddress(Address address) {
        this(address.makeAddrStd());
    }

    @Override
    public Object serialize() {
        return this.addressString;
    }
}
