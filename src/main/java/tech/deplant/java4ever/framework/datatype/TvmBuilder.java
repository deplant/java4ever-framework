package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TvmBuilder {
	private final AtomicInteger refCounter = new AtomicInteger(0);
	private final List<Boc.BuilderOp> operations = new ArrayList<>();

	public Boc.BuilderOp[] builders() {
		return this.operations.toArray(Boc.BuilderOp[]::new);
	}

	public TvmBuilder store(AbiType type) throws EverSdkException {
		this.operations.add(switch (type) {
			case Uint(int size, BigInteger bigInteger) intVal -> new Boc.BuilderOp.Integer(size, intVal.toABI());
			case ByteString str -> new Boc.BuilderOp.Cell(new Boc.BuilderOp[]{new Boc.BuilderOp.BitString(str.toABI())});
			case Address addr -> new Boc.BuilderOp.Address(addr.toABI());
			case TvmBuilder builder -> {
				int refCount = this.refCounter.incrementAndGet();
				if (refCount > 4) {
					throw new EverSdkException(new EverSdkException.ErrorResult(-306,
					                                                            "TvmCell can't contain more than 4 references to other TvmCells"));
				}
				yield new Boc.BuilderOp.Cell(builder.builders());
			}
			default -> throw new EverSdkException(new EverSdkException.ErrorResult(-305,
			                                                                       "Builder of TvmCell doesn't support this type for ABI conversion"),
			                                      new Exception());
		});
		return this;
	}

	public TvmBuilder store(TypePrefix prefix, int size, Object inputValue) throws EverSdkException {
		store(AbiType.of(prefix, size, inputValue));
		return this;
	}

	public TvmCell toCell(Sdk sdk) throws EverSdkException {
		return new TvmCell(Boc.encodeBoc(sdk.context(), builders(), null).boc());
	}
}
