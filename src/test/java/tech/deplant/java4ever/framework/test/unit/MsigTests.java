package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class MsigTests {

	static Sdk SDK;
	static Sdk SDK_DEV;

	static Giver GIVER;
	private static System.Logger logger = System.getLogger(MsigTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = Sdk.builder()
		         .networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
		         .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		SDK_DEV = Sdk.builder()
		             .networkEndpoints(System.getenv("DEV_NET_OSIRIS_ENDPOINT"))
		             .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		GIVER = EverOSGiver.V2(SDK);
	}

	@Test
	public void first_msig_deploy_passes_second_throws() throws Throwable {
		var keys = Credentials.RANDOM(SDK);

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(SDK,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicBigInt()},
		                                                                     1);
		SafeMultisigWallet msig = deployStatement.deployWithGiver(GIVER,
		                                                          Convert.toValue("1",
		                                                                          CurrencyUnit.Ever.EVER));
		assertTrue(Account.ofAddress(SDK, msig.address()).isActive());
		try {
			deployStatement.deployWithGiver(GIVER,
			                                Convert.toValue("1",
			                                                CurrencyUnit.Ever.EVER));
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
