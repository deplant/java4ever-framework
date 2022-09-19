package tech.deplant.java4ever.framework.template.type;

public enum AbiValueType {
    ADDRESS, // address
    UNSIGNED_INTEGER, // uint, uintXX
    SIGNED_INTEGER, // int, intXX
    BYTE, // byte, byteXX, bytes, string
    TVM_CELL, //cell
    TVM_SLICE,
    TVM_BUILDER,
    BOOLEAN, //bool
    MAP, // map(address,uint128)
    TUPLE // tuple[]
}
