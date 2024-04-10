package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.Seed;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.*;
import static tech.deplant.java4ever.frtest.unit.Env.RNG_KEYS;

/**
 * Tests for currency calculations
 * @see CurrencyUnit
 * @see CurrencyUnit.Ever
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class CurrencyUnitTests {

	@Test
	public void result_of_all_aliases_should_be_correct() throws Throwable {
		assertEquals(new BigInteger("1000000000000000000"), CurrencyUnit.VALUE(GIGAEVER, "1"));
		assertEquals(CurrencyUnit.VALUE(GIGAEVER, "1"), CurrencyUnit.VALUE(MEGAEVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(MEGAEVER, "1"), CurrencyUnit.VALUE(KILOEVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(KILOEVER, "1"), CurrencyUnit.VALUE(EVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(EVER, "1"), CurrencyUnit.VALUE(MILLIEVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(MILLIEVER, "1"), CurrencyUnit.VALUE(MICROEVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(MICROEVER, "1"), CurrencyUnit.VALUE(NANOEVER, "1000"));
		assertEquals(CurrencyUnit.VALUE(NANOEVER, "1"), BigInteger.ONE);
	}

	@Test
	public void ever_equals_to_custom_token_with_9_digits() throws Throwable {
		assertEquals(CurrencyUnit.VALUE(new CustomToken(9),"1"), CurrencyUnit.VALUE(EVER, "1"));
	}

}
