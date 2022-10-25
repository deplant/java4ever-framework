package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AccountTests {

	static Sdk SDK;
	static Sdk SDK_DEV;
	private static System.Logger logger = System.getLogger(AccountTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = new SdkBuilder()
				.networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
				.create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		SDK_DEV = new SdkBuilder()
				.networkEndpoints("https://mainnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql")
				.create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

	@Test
	public void try_local_execution() throws Throwable {
		var keys = Credentials.RANDOM(SDK);

		Msig msig = MsigTemplate.SAFE()
		                        .deploySingleSig(SDK,
		                                         keys,
		                                         new EverOSGiver(SDK),
		                                         Convert.toNanos("1", CurrencyUnit.Ever.EVER));
		assertTrue(msig.account().isActive());
		Account acc = Account.ofAddress(SDK, msig.address());
		Map<String, Object> params = Map.of(
				"dest", msig.address(),
				"value", Convert.toNanos("1", CurrencyUnit.Ever.EVER),
				"bounce", true,
				"flags", 0,
				"payload", "");
		var result = acc.runLocal(SDK, msig.abi(), "sendTransaction", params, null, msig.credentials(), null);
		String newBoc = result.account();
		Map<String, Object> outputs = result.decoded().output();
	}

}
