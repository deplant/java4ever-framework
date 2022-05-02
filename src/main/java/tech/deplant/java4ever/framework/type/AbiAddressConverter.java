package tech.deplant.java4ever.framework.type;

import tech.deplant.java4ever.framework.Address;

public class AbiAddressConverter implements AbiValueConverter {

    public static Object convert(Object o) {
        return switch (o) {
            case String s -> s;
            case Address a -> a.makeAddrStd();
            default -> throw new RuntimeException("Address should be String or Address.");
        };
    }
}
