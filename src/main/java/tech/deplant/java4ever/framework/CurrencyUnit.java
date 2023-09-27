package tech.deplant.java4ever.framework;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface CurrencyUnit {

	class CustomToken implements CurrencyUnit {

		private final BigDecimal factor;
		private final int decimals;

		public CustomToken(int decimals) {
			this.decimals = decimals;
			this.factor = BigDecimal.TEN.pow(decimals);
		}

		@Override
		public BigDecimal factor() {
			return this.factor;
		}

		@Override
		public int decimals() {
			return this.decimals;
		}
	}

	BigDecimal factor();

	int decimals();

	static BigInteger VALUE(CurrencyUnit unit, String amount) {
		return VALUE(unit, new BigDecimal(amount));
	}

	static BigInteger VALUE(CurrencyUnit unit, BigDecimal amount) {
		return amount.multiply(unit.factor()).toBigInteger();
	}

	default BigInteger nanos(BigDecimal decimalAmount) {
		return decimalAmount.multiply(factor()).toBigInteger();
	}

	default BigInteger nanos(String stringAmount) {
		return nanos(new BigDecimal(stringAmount));
	}

	enum Ever implements CurrencyUnit {
		NANOEVER(0),
		MICROEVER(3),
		MILLIEVER(6),
		EVER(9),
		KILOEVER(12),
		MEGAEVER(15),
		GIGAEVER(18);

		private final BigDecimal factor;

		private final int decimals;

		Ever(int decimals) {
			this.decimals = decimals;
			this.factor = BigDecimal.TEN.pow(decimals);
		}


		@Override
		public int decimals() {
			return this.decimals;
		}

		@Override
		public BigDecimal factor() {
			return this.factor;
		}
	}

	enum Tokens implements CurrencyUnit {
		QUBE(9),
		BRIDGE(9),
		LEND(9),
		WETH(18);

		private final BigDecimal factor;
		private final int decimals;

		Tokens(int decimals) {
			this.decimals = decimals;
			this.factor = BigDecimal.TEN.pow(decimals);
		}

		@Override
		public int decimals() {
			return this.decimals;
		}

		@Override
		public BigDecimal factor() {
			return this.factor;
		}
	}

}
