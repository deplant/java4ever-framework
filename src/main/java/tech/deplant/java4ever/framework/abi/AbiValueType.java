package tech.deplant.java4ever.framework.abi;

public enum AbiValueType {
	INT,
	UINT,
	STRING,
	BYTE,
	BYTES,
	ADDRESS,
	BOOL,
	CELL,
	SLICE,
	BUILDER,
	TUPLE
	// MAP is a special type that is unpacked during conversion
	// Address, bytes, string, bool, contract, enum, fixed bytes, integer and struct types can be used as a KeyType
	// Struct type can be used as KeyType only if it contains only integer, boolean, fixed bytes or enum types and fits ~1023 bit.
}
