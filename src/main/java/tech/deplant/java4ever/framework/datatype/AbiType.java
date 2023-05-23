package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;

public interface AbiType<JAVA_TYPE, JSON_TYPE> {

	static AbiType of(TypePrefix prefix, int size, Object inputValue) throws EverSdkException {
		return switch (prefix) {
			case UINT, INT -> Uint.fromJava(size, inputValue);
			case STRING-> SolString.fromJava(inputValue);
			case BYTES, BYTE -> SolBytes.fromJava(inputValue);
			case ADDRESS -> Address.fromJava(inputValue);
			case BOOL -> Bool.fromJava(inputValue);
			case CELL, SLICE, BUILDER -> TvmCell.fromJava(inputValue);
			case TUPLE -> {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-301,
				                                                               "ABI Parsing unexpected! Shouldn't get here!"),
				                              new RuntimeException());
				System.getLogger(AbiType.class.getName()).log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		};
	}

	static AbiType ofABI(TypePrefix prefix, int size, Object inputValue) throws EverSdkException {
		return switch (prefix) {
			case UINT, INT -> Uint.fromJava(size, inputValue);
			case STRING -> SolString.fromABI(inputValue.toString());
			case BYTES, BYTE -> SolBytes.fromABI(inputValue.toString());
			case ADDRESS -> Address.fromABI(inputValue);
			case BOOL -> Bool.fromJava(inputValue);
			case CELL, SLICE, BUILDER -> TvmCell.fromJava(inputValue);
			case TUPLE -> {
				var ex = new EverSdkException(new EverSdkException.ErrorResult(-301,
				                                                               "ABI Parsing unexpected! Shouldn't get here!"),
				                              new RuntimeException());
				System.getLogger(AbiType.class.getName()).log(System.Logger.Level.WARNING, () -> ex.toString());
				throw ex;
			}
		};
	}

	Abi.AbiParam toAbiParam(String name);

	String abiTypeName();

	JAVA_TYPE toJava();

	JSON_TYPE toABI();

}
