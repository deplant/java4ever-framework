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
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AccountTests {

	static Sdk SDK;
	static Sdk SDK_DEV;

	static Giver GIVER;
	private static System.Logger logger = System.getLogger(AccountTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = Sdk.builder()
		         .networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
		         .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		SDK_DEV = Sdk.builder()
		             .networkEndpoints("https://mainnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql")
		             .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		GIVER = EverOSGiver.V2(SDK);
	}

	@Test
	public void try_local_execution() throws Throwable {
		var keys = Credentials.RANDOM(SDK);

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(SDK,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		SafeMultisigWallet msig = deployStatement.deployWithGiver(GIVER,
		                                                          Convert.toValue("1",
		                                                                          CurrencyUnit.Ever.EVER));
		assertTrue(msig.account().isActive());
		Account acc = Account.ofAddress(SDK, msig.address());
		Map<String, Object> params = Map.of(
				"dest", msig.address(),
				"value", Convert.toValue("1", CurrencyUnit.Ever.EVER),
				"bounce", true,
				"flags", 0,
				"payload", "");
		var result = acc.runLocal(SDK, msig.abi(), "sendTransaction", params, null, msig.credentials(), null);
		String newBoc = result.account();
		Map<String, Object> outputs = result.decoded().output();
	}

}
