package tech.deplant.java4ever.framework.abi.type;

public enum AbiValueType {
    // map(address,uint128)
    // tuple[]
    ADDRESS, // address
    UNSIGNED_INTEGER, // uint, uintXX
    SIGNED_INTEGER, // int, intXX
    BYTES, // byte, byteXX, bytes, string
    TVM_CELL,
    TVM_SLICE,
    TVM_BUILDER,
    TUPLE,
    BOOLEAN
}
