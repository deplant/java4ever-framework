package tech.deplant.java4ever.framework.gql;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Transaction status.
 */
public enum TransactionStatus {
	/**
	 * Unknown transaction status.
	 */
// 0 – unknown
	//1 – preliminary
	//2 – proposed
	//3 – finalized
	//4 – refused
	UNKNOWN(0),
	/**
	 * Preliminary transaction status.
	 */
	PRELIMINARY(1),
	/**
	 * Proposed transaction status.
	 */
	PROPOSED(2),
	/**
	 * Finalized transaction status.
	 */
	FINALIZED(3),
	/**
	 * Refused transaction status.
	 */
	REFUSED(4);

	private final int value;

	TransactionStatus(int value) {
		this.value = value;
	}

	/**
	 * Value int.
	 *
	 * @return the int
	 */
	@JsonValue
	public int value() {
		return this.value;
	}
}
