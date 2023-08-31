package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;


public record TvmCell(String cellBoc) implements AbiValue<String> {

	public static final TvmCell EMPTY = new TvmCell("te6ccgEBAQEAAgAAAA==");

	public static TvmBuilder builder() {
		return new TvmBuilder();
	}

	public JsonNode decode(Sdk sdk, Abi.AbiParam[] types) throws EverSdkException {
		return Abi.decodeBoc(sdk.context(), types, cellBoc(), true).data();
	}

	public JsonNode decode(Sdk sdk, String[] types) throws EverSdkException {
		Abi.AbiParam[] params = new Abi.AbiParam[types.length];
		for (int i = 0; i < types.length; i++) {
			params[i] = new Abi.AbiParam(String.valueOf(i), types[i], null);
		}
		return decode(sdk, params);
	}

	public JsonNode decode(Sdk sdk, AbiType[] types) throws EverSdkException {
		Abi.AbiParam[] params = new Abi.AbiParam[types.length];
		for (int i = 0; i < types.length; i++) {
			params[i] = new Abi.AbiParam(String.valueOf(i), types[i].abiName(), null);
		}
		return decode(sdk, params);
	}

	public JsonNode decodeAndGet(Sdk sdk, String[] types, int index) throws EverSdkException {
		var result = decode(sdk, types);
		return result.get(String.valueOf(index));
	}

	@Override
	public String toString() {
		return cellBoc();
	}

	@Override
	public String toJava() {
		return toString();
	}

	@JsonValue
	public String toABI() {
		return cellBoc();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.STRING,0,false);
	}

}
