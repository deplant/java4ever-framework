package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Seed;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.frtest.unit.Env.RNG_KEYS;
import static tech.deplant.java4ever.frtest.unit.Env.RNG_SEED;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class CredentialsTests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void all_random_keys_should_be_different() throws Throwable {
		var keys1 = RNG_KEYS();
		var keys2 = RNG_KEYS();
		var keys3 = RNG_KEYS();
		var keys4 = RNG_KEYS();
		var keys5 = RNG_KEYS();
		assertNotEquals(keys1.toString(), keys2.toString());
		assertNotEquals(keys2.toString(), keys3.toString());
		assertNotEquals(keys3.toString(), keys4.toString());
		assertNotEquals(keys4.toString(), keys5.toString());
		assertNotEquals(keys5.toString(), keys1.toString());
	}

	@Test
	public void all_random_seeds_should_be_different() throws Throwable {
		var seed1 = RNG_SEED();
		var seed2 = RNG_SEED();
		var seed3 = RNG_SEED();
		var seed4 = RNG_SEED();
		var seed5 = RNG_SEED();
		assertNotEquals(seed1.toString(), seed2.toString());
		assertNotEquals(seed2.toString(), seed3.toString());
		assertNotEquals(seed3.toString(), seed4.toString());
		assertNotEquals(seed4.toString(), seed5.toString());
		assertNotEquals(seed5.toString(), seed1.toString());
	}

	@Test
	public void make_seed_and_keys() throws Throwable {
		Env.LOG.log(System.Logger.Level.DEBUG, Seed.RANDOM(Env.SDK_EMPTY).toString());
		Env.LOG.log(System.Logger.Level.DEBUG, Credentials.ofSeed(Env.SDK_EMPTY,
		                                                          new Seed(
				                                                      "object burger primary dish harbor luxury morning mystery sausage wide this time",
				                                                      12)).toString());
	}


	@Test
	public void all_keys_from_one_seed_should_be_equal() throws Throwable {
		var seed = Seed.RANDOM(Env.SDK_EMPTY);
		var keys1 = Credentials.ofSeed(Env.SDK_EMPTY, seed);
		var keys2 = Credentials.ofSeed(Env.SDK_EMPTY, seed);
		assertEquals(keys1.toString(), keys2.toString());
	}

	@Test
	public void no_credentials_means_signer_type_none() throws Throwable {
		var keys1 = Credentials.NONE;
		assertEquals(keys1.signer(), new Abi.Signer.None());
	}

}
