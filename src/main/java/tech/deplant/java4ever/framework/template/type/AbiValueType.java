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
    TUPLE // tuple
    // tuple[]
    // map(address,tuple)
    // Address, bytes, string, bool, contract, enum, fixed bytes, integer and struct types can be used as a KeyType
    // Struct type can be used as KeyType only if it contains only integer, boolean, fixed bytes or enum types and fits ~1023 bit.
}
