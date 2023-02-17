package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;

public interface TIP3 {

	interface TIP31Root {

		/**
		 * Returns the name of the token - e.g. "MyToken".
		 */
		String name();

		/**
		 * Returns the symbol of the token. E.g. “HIX”.
		 */
		String symbol();

		/**
		 * Returns the number of decimals the token uses - e.g. 8, means to divide the token amount by 100000000 to get its user representation.
		 */
		Integer decimals();

		/**
		 * Returns the token wallet code.
		 */
		BigInteger totalSupply();

		/**
		 * Returns the symbol of the token. E.g. “HIX”.
		 */
		TvmCell walletCode();
	}

}
