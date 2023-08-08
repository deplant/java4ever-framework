package tech.deplant.java4ever.framework.datatype;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TvmBuilder {
	private final AtomicInteger refCounter = new AtomicInteger(0);
	private final List<Boc.BuilderOp> operations = new ArrayList<>();

	public Boc.BuilderOp[] builders() {
		return this.operations.toArray(Boc.BuilderOp[]::new);
	}

	public void store(AbiValue... types) throws EverSdkException {
		for (var type : types) {
			this.operations.add(switch (type) {
				case Uint intVal -> new Boc.BuilderOp.Integer((long) intVal.size(), intVal.jsonValue());
				case Address addr -> new Boc.BuilderOp.Address(addr.jsonValue());
				case SolString str -> {
					incrementRefCounter();
					yield new Boc.BuilderOp.Cell(new Boc.BuilderOp[]{new Boc.BuilderOp.BitString(str.jsonValue())});
				}
				case SolBytes byt -> {
					incrementRefCounter();
					yield new Boc.BuilderOp.Cell(new Boc.BuilderOp[]{new Boc.BuilderOp.BitString(byt.jsonValue())});
				}
				case TvmBuilder builder -> {
					incrementRefCounter();
					yield new Boc.BuilderOp.Cell(builder.builders());
				}
				default -> throw new EverSdkException(new EverSdkException.ErrorResult(-305,
				                                                                       "Builder of TvmCell doesn't support this type for ABI conversion"),
				                                      new Exception());
			});
		}
	}

	private void incrementRefCounter() throws EverSdkException {
		int refCount = this.refCounter.incrementAndGet();
		if (refCount > 4) {
			throw new EverSdkException(new EverSdkException.ErrorResult(-306,
			                                                            "TvmCell can't contain more than 4 references to other TvmCells"));
		}
	}

	public TvmBuilder store(AbiType type, Object inputValue) throws EverSdkException {
		store(AbiValue.of(type, inputValue));
		return this;
	}

	public TvmCell toCell(Sdk sdk) throws EverSdkException {
		return new TvmCell(Boc.encodeBoc(sdk.context(), builders(), null).boc());
	}
}
