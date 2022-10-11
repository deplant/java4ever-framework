package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.SdkBuilder;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.crypto.Seed;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class CredentialsTests {


	static Sdk SDK;
	private static Logger log = LoggerFactory.getLogger(CredentialsTests.class);

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = new SdkBuilder()
				.create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

	@Test
	public void all_random_keys_should_be_different() throws Throwable {
		var keys1 = Credentials.RANDOM(SDK);
		var keys2 = Credentials.RANDOM(SDK);
		var keys3 = Credentials.RANDOM(SDK);
		var keys4 = Credentials.RANDOM(SDK);
		var keys5 = Credentials.RANDOM(SDK);
		assertNotEquals(keys1.toString(), keys2.toString());
		assertNotEquals(keys2.toString(), keys3.toString());
		assertNotEquals(keys3.toString(), keys4.toString());
		assertNotEquals(keys4.toString(), keys5.toString());
		assertNotEquals(keys5.toString(), keys1.toString());
	}

	@Test
	public void all_random_seeds_should_be_different() throws Throwable {
		var seed1 = Seed.RANDOM(SDK);
		var seed2 = Seed.RANDOM(SDK);
		var seed3 = Seed.RANDOM(SDK);
		var seed4 = Seed.RANDOM(SDK);
		var seed5 = Seed.RANDOM(SDK);
		assertNotEquals(seed1.toString(), seed2.toString());
		assertNotEquals(seed2.toString(), seed3.toString());
		assertNotEquals(seed3.toString(), seed4.toString());
		assertNotEquals(seed4.toString(), seed5.toString());
		assertNotEquals(seed5.toString(), seed1.toString());
	}

	@Test
	public void make_seed_and_keys() throws Throwable {
		log.info(Seed.RANDOM(SDK).toString());
		log.info(Credentials.ofSeed(SDK,
		                            new Seed(
				                            "object burger primary dish harbor luxury morning mystery sausage wide this time",
				                            12)).toString());
	}


	@Test
	public void all_keys_from_one_seed_should_be_equal() throws Throwable {
		var seed = Seed.RANDOM(SDK);
		var keys1 = Credentials.ofSeed(SDK, seed);
		var keys2 = Credentials.ofSeed(SDK, seed);
		assertEquals(keys1.toString(), keys2.toString());
	}

}
