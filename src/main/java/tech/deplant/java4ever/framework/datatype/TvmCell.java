package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.Sdk;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Map;


public record TvmCell(String cellBoc) implements AbiType<String, String> {

	public static TvmCell EMPTY() {
		return TvmCell.fromJava("te6ccgEBAQEAAgAAAA==");
	}

	public static TvmBuilder builder() {
		return new TvmBuilder();
	}

	public static TvmCell fromJava(Object input) {
		return switch (input) {
			case TvmCell cell -> cell;
			case String s -> new TvmCell(s);
			default -> throw new IllegalStateException(
					"Unexpected value: " + input + " class: " + input.getClass().getName());
		};
	}

	public Map<String, Object> decode(Sdk sdk, Abi.AbiParam[] types) throws EverSdkException {
		return Abi.decodeBoc(sdk.context(), types, cellBoc(), true).data();
	}

	public Map<String, Object> decode(Sdk sdk, String[] types) throws EverSdkException {
		Abi.AbiParam[] params = new Abi.AbiParam[types.length];
		for (int i = 0; i < types.length; i++) {
			String type = types[i];
			String name = String.valueOf(i);
			params[i] = new Abi.AbiParam(name, type, null);
		}
		return decode(sdk, params);
	}

	public Object decodeAndGet(Sdk sdk, String[] types, int index) throws EverSdkException {
		var result = decode(sdk, types);
		return result.get(String.valueOf(index));
	}

	@Override
	public Abi.AbiParam toAbiParam(String name) {
		return new Abi.AbiParam(name, abiTypeName(), null);
	}

	@Override
	public String abiTypeName() {
		return "cell";
	}

	@Override
	public String toJava() {
		return cellBoc();
	}

	@Override
	public String toABI() {
		return cellBoc();
	}

}
