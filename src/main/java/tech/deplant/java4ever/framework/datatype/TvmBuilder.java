package tech.deplant.java4ever.framework.datatype;

import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record TvmBuilder(AtomicInteger refCounter, List<Boc.BuilderOp> operations) implements AbiValue {

	public TvmBuilder() {
		this(new AtomicInteger(0), new ArrayList<>());
	}

	public Boc.BuilderOp[] builders() {
		return this.operations.toArray(Boc.BuilderOp[]::new);
	}

	public TvmBuilder store(AbiValue... types) throws EverSdkException {
		for (var type : types) {
			this.operations.add(switch (type) {
				case Uint intVal -> new Boc.BuilderOp.Integer((long) intVal.size(), intVal.toABI());
				case Address addr -> new Boc.BuilderOp.Address(addr.toABI());
				case SolString str -> {
					incrementRefCounter();
					yield new Boc.BuilderOp.Cell(new Boc.BuilderOp[]{new Boc.BuilderOp.BitString(Strings.toHexString(str.toABI()))});
				}
				case SolBytes byt -> {
					incrementRefCounter();
					yield new Boc.BuilderOp.Cell(new Boc.BuilderOp[]{new Boc.BuilderOp.BitString(byt.toABI())});
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
		return this;
	}

	private void incrementRefCounter() throws EverSdkException {
		int refCount = this.refCounter.incrementAndGet();
		if (refCount > 4) {
			throw new EverSdkException(new EverSdkException.ErrorResult(-306,
			                                                            "TvmCell can't contain more than 4 references to other TvmCells"));
		}
	}

	public TvmBuilder store(AbiType type, Object inputValue) throws EverSdkException {
		return store(AbiValue.of(type, inputValue));
	}

	public TvmCell toCell(Sdk sdk) throws EverSdkException {
		return new TvmCell(Boc.encodeBoc(sdk.context(), builders(), null).boc());
	}

	@Override
	public Object toJava() {
		return this;
	}

	@Override
	public Object toABI() {
		return this;
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BUILDER,0,false);
	}
}
