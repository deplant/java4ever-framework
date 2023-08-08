package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.EverSdkException;

public sealed interface AbiValue permits Address, Bool, SolArray, SolBytes, SolString, SolStruct, TvmBuilder, TvmCell, Uint {

	static AbiValue of(AbiType type, Object value) throws EverSdkException {
		return switch (type.prefix()) {
			case UINT, INT -> Uint.of(type.size(), value);
			case STRING-> new SolString(String.valueOf(value));
			case BYTES, BYTE -> new SolBytes(type.size(), String.valueOf(value));
			case ADDRESS -> new Address(String.valueOf(value));
			case BOOL -> new Bool(String.valueOf(value));
			case CELL, SLICE, BUILDER -> new TvmCell(String.valueOf(value));
			case TUPLE, OPTIONAL -> {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-301,
				                                                               "ABI Parsing unexpected! Shouldn't get here!"),
				                              new RuntimeException());
				System.getLogger(AbiValue.class.getName()).log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		};
	}

	Object jsonValue();

	AbiType type();

}
