package tech.deplant.java4ever.framework;

import java.math.BigDecimal;

public interface CurrencyUnit {

	BigDecimal factor();

	enum Ever implements CurrencyUnit {
		NANOEVER(0),
		MICROEVER(3),
		MILLIEVER(6),
		EVER(9),
		KILOEVER(12),
		MEGAEVER(15),
		GIGAEVER(18);

		private BigDecimal factor;

		Ever(int factor) {
			this.factor = BigDecimal.TEN.pow(factor);
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

		private BigDecimal factor;

		Tokens(int factor) {
			this.factor = BigDecimal.TEN.pow(factor);
		}

		@Override
		public BigDecimal factor() {
			return this.factor;
		}
	}

}