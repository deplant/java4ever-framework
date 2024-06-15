package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ThreadedExecutionTests {


	private static System.Logger logger = System.getLogger(SubscribeHandleTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	private static void randomWalletDeployment(int contextId, GiverContract giver) {
		try {
			new MultisigBuilder().setType(MultisigContract.Type.SAFE)
			                     .prepareAndDeploy(contextId, Credentials.ofRandom(contextId), giver, EVER_ONE);
		} catch (JsonProcessingException | EverSdkException e) {
			logger.log(System.Logger.Level.ERROR, e);
		}
	}

//	@Test
//	public void concurrent_deployment_test() throws Throwable {
//		try(var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
//			executorService.submit(() -> {
//				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
//			});
//			executorService.submit(() -> {
//				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
//			});
//			executorService.submit(() -> {
//				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
//			});
//			executorService.submit(() -> {
//				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
//			});
//			executorService.submit(() -> {
//				randomWalletDeployment(SDK_LOCAL, GIVER_LOCAL);
//			});
//			executorService.awaitTermination(600, TimeUnit.SECONDS);
//		}
//		assertTrue(true);
//	}

}
