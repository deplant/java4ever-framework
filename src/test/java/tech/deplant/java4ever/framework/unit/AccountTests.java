package tech.deplant.java4ever.framework.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Convert;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AccountTests {
	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		TestEnvironment.INIT();
	}

	@Test
	public void try_local_execution() throws Throwable {
		var keys = TestEnvironment.RNG_KEYS();

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(TestEnvironment.SDK_LOCAL,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		SafeMultisigWallet msig = deployStatement.deployWithGiver(TestEnvironment.GIVER_LOCAL,
		                                                          Convert.toValue("1",
		                                                                          CurrencyUnit.Ever.EVER));
		assertTrue(msig.account().isActive());
		Account acc = Account.ofAddress(TestEnvironment.SDK_LOCAL, msig.address());
		Map<String, Object> params = Map.of(
				"dest", msig.address(),
				"value", Convert.toValue("1", CurrencyUnit.Ever.EVER),
				"bounce", true,
				"flags", 0,
				"payload", "");
		var result = acc.runLocal(TestEnvironment.SDK_LOCAL, msig.abi(), "sendTransaction", params, null, msig.credentials(), null);
		String newBoc = result.account();
		Map<String, Object> outputs = result.decoded().output();
	}

}
