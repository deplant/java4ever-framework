package tech.deplant.java4ever.framework.datatype;

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

	@Override
	public String toJava() {
		return cellBoc();
	}

	@Override
	public String toABI() {
		return cellBoc();
	}
}
