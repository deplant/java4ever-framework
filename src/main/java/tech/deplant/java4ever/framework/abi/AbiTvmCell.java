package tech.deplant.java4ever.framework.abi;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;

import java.util.ArrayList;
import java.util.List;

public record AbiTvmCell(Sdk sdk, AbiValue... storeList) implements AbiValue {

	public Boc.BuilderOp[] builder() throws EverSdkException {
		List<Boc.BuilderOp> ops = new ArrayList<>();
		for (AbiValue o : this.storeList) {
			ops.add(switch (o) {
				case AbiUint intVal -> new Boc.BuilderOp.Integer(intVal.size(), intVal.serialize().toString());
				case AbiString str -> new Boc.BuilderOp.BitString(str.serialize().toString());
				case AbiAddress addr -> new Boc.BuilderOp.Address(addr.serialize().toString());
				case AbiTvmCell cell -> new Boc.BuilderOp.Cell(cell.builder());
				default -> throw new EverSdkException(new EverSdkException.ErrorResult(-305,
				                                                                       "Builder of TvmCell doesn't support this type for ABI conversion"),
				                                      new Exception());
			});
		}
		return ops.toArray(Boc.BuilderOp[]::new);
	}

	@Override
	public Object serialize() throws EverSdkException {
		return Boc.encodeBoc(this.sdk.context(), builder(), null).boc();
	}
}
