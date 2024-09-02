package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ThreadedExecutionTests {


	private static System.Logger logger = System.getLogger(SubscribeHandleTests.class.getName());

	Runnable RUNNABLE = () -> {
		try {
			var walletTemplate = new SurfMultisigWalletTemplate();
			for (int i = 0; i < 10_002; i++) {

				var keys = Credentials.ofRandom(SDK_EMPTY);
				var owners = new BigInteger[]{keys.publicKeyBigInt()};
				String result = null;

				result = walletTemplate.prepareDeploy(SDK_EMPTY, 0, keys, owners, 1)
				                       .toAddress()
				                       .toString()
				                       .split(":")[1];
				if (i % 1_000 == 0) {
					System.out.printf("Thread: %s Cycle num: %d Addr: %s%n",
					                  Thread.currentThread().getName(), i, result);
				}

			}
		} catch (EverSdkException | JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	};

	Runnable RUNNABLE_DEPLOYS = () -> {
		try {
			var walletTemplate = new SurfMultisigWalletTemplate();
			for (int i = 0; i < 100; i++) {

				var keys = Credentials.ofRandom(SDK_LOCAL);
				var owners = new BigInteger[]{keys.publicKeyBigInt()};
				String result = null;
				var prepared = walletTemplate.prepareDeploy(SDK_LOCAL, 0, keys, owners, 1);
				result = walletTemplate.prepareDeploy(SDK_LOCAL, 0, keys, owners, 1)
				                       .toAddress()
				                       .toString()
				                       .split(":")[1];
				prepared.deployWithGiver(GIVER_LOCAL,EVER_ONE);
				if (i % 50_000 == 0) {
					System.out.printf("Thread: %s Cycle num: %d Addr: %s%n",
					                  Thread.currentThread().getName(), i, result);
				}

			}
		} catch (EverSdkException | JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	};

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

	@Test
	@Disabled
	public void repeatable_platform_threads() {

		System.out.println("Start: " + Instant.now().toString());
		try (var executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
			for (int i = 0; i < 8; i++) {
				executor.submit(RUNNABLE_DEPLOYS);
			}
			executor.shutdown();
			executor.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("End: " + Instant.now().toString());
	}

	@Test
	@Disabled
	public void repeatable_virtual_threads() {

		System.out.println("Start: " + Instant.now().toString());
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < 10; i++) {
				executor.submit(RUNNABLE);
			}
			executor.shutdown();
			executor.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("End: " + Instant.now().toString());
	}

}
