package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AccountTests {
	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		INIT();
	}

	@Test
	public void try_local_execution() throws Throwable {
		var keys = RNG_KEYS();

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(SDK_LOCAL,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		MultisigWallet msig = new MultisigBuilder().setType(MultisigWallet.Type.SAFE).build(SDK_LOCAL,keys,GIVER_LOCAL,EVER_ONE);
		assertTrue(msig.account().isActive());
		Account acc = Account.ofAddress(SDK_LOCAL, msig.address());
		Map<String, Object> params = Map.of(
				"dest", msig.address(),
				"value", EVER_ONE,
				"bounce", true,
				"flags", 0,
				"payload", "");
		var result = acc.runLocal(SDK_LOCAL, msig.abi(), "sendTransaction", params, null, msig.credentials(), null);
		String newBoc = result.account();
		Map<String, Object> outputs = result.decoded().output();
	}

}
