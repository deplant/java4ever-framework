package tech.deplant.java4ever.framework.datatype;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type Bool.
 */
public record Bool(Boolean value) implements AbiValue<Boolean> {

	/**
	 * Instantiates a new Bool.
	 *
	 * @param stringValue the string value
	 */
	public Bool(String stringValue) {
		this(Boolean.valueOf(stringValue));
	}

	/**
	 * To boolean boolean.
	 *
	 * @return the boolean
	 */
	public boolean toBoolean() {
		return value();
	}

	@Override
	public Boolean toJava() {
		return toBoolean();
	}

	@JsonValue
	public Boolean toABI() {
		return value();
	}

	@Override
	public AbiType type() {
		return new AbiType(AbiTypePrefix.BOOL,0,false);
	}
}
