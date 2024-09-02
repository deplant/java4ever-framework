package tech.deplant.java4ever.framework.datatype;

/**
 * The enum Abi type prefix.
 */
public enum AbiTypePrefix {
	/**
	 * Int abi type prefix.
	 */
	INT,
	/**
	 * Uint abi type prefix.
	 */
	UINT,
	/**
	 * String abi type prefix.
	 */
	STRING,
	/**
	 * Byte abi type prefix.
	 */
	BYTE,
	/**
	 * Bytes abi type prefix.
	 */
	BYTES,
	/**
	 * Address abi type prefix.
	 */
	ADDRESS,
	/**
	 * Bool abi type prefix.
	 */
	BOOL,
	/**
	 * Cell abi type prefix.
	 */
	CELL,
	/**
	 * Slice abi type prefix.
	 */
	SLICE,
	/**
	 * Builder abi type prefix.
	 */
	BUILDER,
	/**
	 * Optional abi type prefix.
	 */
	OPTIONAL,
	/**
	 * Tuple abi type prefix.
	 */
	TUPLE,
	// MAP is a special type that is unpacked during conversion
	// Address, bytes, string, bool, contract, enum, fixed bytes, integer and struct types can be used as a KeyType
	// Struct type can be used as KeyType only if it contains only integer, boolean, fixed bytes or enum types and fits ~1023 bit.
}
