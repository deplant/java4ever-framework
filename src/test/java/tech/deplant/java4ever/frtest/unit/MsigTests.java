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
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.frtest.unit.Env.*;

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

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		MultisigContract msig = new MultisigBuilder().setType(MultisigContract.Type.SAFE).build(SDK_LOCAL, keys, GIVER_LOCAL, EVER_ONE);
		String s = switch (msig) {
			case null -> "";
			case MultisigContract c -> c.tvmPubkey();
		};
		assertTrue(Account.ofAddress(Env.SDK_LOCAL, msig.address()).isActive());
		try {
			deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
