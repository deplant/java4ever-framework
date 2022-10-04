package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
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
		assertNotEquals(keys1.secretKey(), keys2.secretKey());
		assertNotEquals(keys2.secretKey(), keys3.secretKey());
		assertNotEquals(keys3.secretKey(), keys4.secretKey());
		assertNotEquals(keys4.secretKey(), keys5.secretKey());
		assertNotEquals(keys5.secretKey(), keys1.secretKey());
	}

	@Test
	public void all_random_seeds_should_be_different() throws Throwable {
		var seed1 = Seed.RANDOM(SDK);
		var seed2 = Seed.RANDOM(SDK);
		var seed3 = Seed.RANDOM(SDK);
		var seed4 = Seed.RANDOM(SDK);
		var seed5 = Seed.RANDOM(SDK);
		assertNotEquals(seed1.phrase(), seed2.phrase());
		assertNotEquals(seed2.phrase(), seed3.phrase());
		assertNotEquals(seed3.phrase(), seed4.phrase());
		assertNotEquals(seed4.phrase(), seed5.phrase());
		assertNotEquals(seed5.phrase(), seed1.phrase());
	}

	@Test
	public void all_keys_from_one_seed_should_be_equal() throws Throwable {
		var seed = Seed.RANDOM(SDK);
		var keys1 = Credentials.ofSeed(SDK, seed);
		var keys2 = Credentials.ofSeed(SDK, seed);
		assertEquals(keys1.secretKey(), keys2.secretKey());
		assertEquals(keys1.publicKey(), keys2.publicKey());
	}

}
