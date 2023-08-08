package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.EverSdkException;

public sealed interface AbiValue permits Address, Bool, SolBytes, SolString, SolStruct, TvmCell, Uint {

	static AbiValue of(AbiType type, Object value) throws EverSdkException {
		return switch (type.prefix()) {
			case UINT, INT -> new Uint(type.size(), value);
			case STRING-> new SolString(value);
			case BYTES, BYTE -> SolBytes.fromJava(value);
			case ADDRESS -> Address.fromJava(value);
			case BOOL -> Bool.fromJava(value);
			case CELL, SLICE, BUILDER -> TvmCell.fromJava(value);
			case TUPLE, OPTIONAL -> {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-301,
				                                                               "ABI Parsing unexpected! Shouldn't get here!"),
				                              new RuntimeException());
				System.getLogger(AbiValue.class.getName()).log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		};
	}

	static AbiValue ofABI(AbiType type, Object inputValue) throws EverSdkException {
		return switch (type.prefix()) {
			case UINT, INT -> Uint.fromJava(type.size(), inputValue);
			case STRING -> SolString.fromABI(inputValue.toString());
			case BYTES, BYTE -> SolBytes.fromABI(inputValue.toString());
			case ADDRESS -> Address.fromABI(inputValue);
			case BOOL -> Bool.fromJava(inputValue);
			case CELL, SLICE, BUILDER -> TvmCell.fromJava(inputValue);
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
