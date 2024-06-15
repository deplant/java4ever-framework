package tech.deplant.java4ever.framework;

/**
 * The enum Message flag.
 */
public enum MessageFlag {

	/**
	 * Sends exact 'value' amount of EVERs
	 */
	EXACT_VALUE_GAS(0),

	/**
	 * Sends all EVERs that came with this message (value is ignored)
	 */
	ALL_MESSAGE_GAS(64),

	/**
	 * Sends all remaining EVERs of this contract (value is ignored)
	 */
	ALL_BALANCE_GAS(128),

	/**
	 * Subtract extra funds from sender's balance to pay forward fee
	 */
	FEE_EXTRA(1),

	/**
	 * Any errors arising during the action phase should be ignored.
	 */
	IGNORE_ERRORS(2),

	/**
	 * Try to destroy sender contract (if balance is 0)
	 */
	SELF_DESTROY(32),

	/**
	 * Complex case: ALL_BALANCE_GAS + SELF_DESTROY + IGNORE_ERRORS
	 */
	DRAIN_AND_DESTROY(162);

	private final int flag;

	MessageFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * Flag int.
	 *
	 * @return the int
	 */
	public int flag() {
		return this.flag;
	}


	}
