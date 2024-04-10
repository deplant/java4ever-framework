package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class DeployHandleTests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws Throwable {
		Env.INIT();
	}

	@Test
	public void contract_is_active_after_deploy() throws Throwable {
		var keys = Env.RNG_KEYS();
		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		var contract1 = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		assertTrue(contract1.account().isActive());
	}

//	@Test
//	public void deployed_contract_pubkey_in_gql_equals_to_expected() throws Throwable {
//		var keys = Env.RNG_KEYS();
//		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
//		                                                                     keys,
//		                                                                     new BigInteger[]{keys.publicBigInt()},
//		                                                                     1);
//		var contract1 = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
//		assertEquals(keys.publicKey(),contract1.tvmPubkey());
//	}

	@Test
	public void first_deployment_passes_second_throws_with_414() throws Throwable {
		var keys = Env.RNG_KEYS();

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE); // first try
		try {
			deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE); // second try
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
