package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigWallet;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigWallet;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.frtest.unit.Env.*;
import static tech.deplant.java4ever.frtest.unit.Env.EVER_ONE;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ThreadedExecutionTests {


	private static System.Logger logger = System.getLogger(SubscriptionTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void concurrent_deployment_test() throws Throwable {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			IntStream.range(1,5).forEach(i -> scope.fork(() -> {
				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
				return i;
			}));

			scope.join();
		}
	}

	private static void randomWalletDeployment(Sdk sdk, Giver giver) {
		try {
			new MultisigBuilder().setType(MultisigWallet.Type.SAFE)
			                     .build(sdk, Credentials.RANDOM(sdk), giver, EVER_ONE);
		} catch (JsonProcessingException | EverSdkException e) {
			logger.log(System.Logger.Level.ERROR, e);
		}
	}

}
