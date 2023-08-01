package tech.deplant.java4ever.framework.gql;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionStatus {
	// 0 – unknown
	//1 – preliminary
	//2 – proposed
	//3 – finalized
	//4 – refused
	UNKNOWN(0),
	PRELIMINARY(1),
	PROPOSED(2),
	FINALIZED(3),
	REFUSED(4);

	private final int value;

	TransactionStatus(int value) {
		this.value = value;
	}

	@JsonValue
	public int value() {
		return this.value;
	}
}
