package tech.deplant.java4ever.framework.abi.datatype;

import tech.deplant.java4ever.binding.EverSdkException;

public interface AbiType<JAVA_TYPE, JSON_TYPE> {

	static AbiType of(TypePrefix prefix, int size, Object inputValue) throws EverSdkException {
		return switch (prefix) {
			case UINT, INT -> Uint.fromJava(size, inputValue);
			case STRING, BYTES, BYTE -> ByteString.fromJava(inputValue);
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

	JAVA_TYPE toJava();

	JSON_TYPE toABI();

}
