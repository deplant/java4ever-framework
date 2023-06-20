package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.frtest.unit.Env.EVER_ONE;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class MsigTests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void first_msig_deploy_passes_second_throws() throws Throwable {
		var keys = Env.RNG_KEYS();



		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		SafeMultisigWallet msig = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		assertTrue(Account.ofAddress(Env.SDK_LOCAL, msig.address()).isActive());
		try {
			deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
