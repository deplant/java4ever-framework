package tech.deplant.java4ever.unit;

import com.yegor256.WeAreOnline;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.DefaultLoader;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class ClientTests {

	private static final Logger log = LoggerFactory.getLogger(ClientTests.class);

	@BeforeAll
	public static void loadSdk() {
		TestEnv.loadEverSdk();
	}

	@Test
	public void sdk_version_equals_constant() throws EverSdkException, ExecutionException, InterruptedException {
		int ctxId = TestEnv.newContextEmpty();
		assertEquals(DefaultLoader.EVER_SDK_VERSION, Client.version(ctxId).get().version());
	}

	@Test
	public void get_version_snippet() throws EverSdkException {
		int ctxId = TestEnv.newContextEmpty();
		var asyncResult = Client.version(ctxId);
		var syncResult = EverSdk.await(asyncResult);
		System.out.println("EVER-SDK Version: " + syncResult.version());
	}

	@Test
	public void api_reference_version_equals_constant() throws EverSdkException, ExecutionException, InterruptedException {
		int ctxId = TestEnv.newContextEmpty();
		assertEquals(DefaultLoader.EVER_SDK_VERSION, Client.getApiReference(ctxId).get().api().get("version").asText());
	}

//	@Test
//	public void api_reference_memory_consumption_should_be_stable() throws EverSdkException, InterruptedException {
//		int ctxId = TestEnv.newContextEmpty();
//		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//			for (int i = 0; i < 10000; i++) {
//				//CompletableFuture<Client.ResultOfGetApiReference> result = //Client.getApiReference(ctxId);
//				CompletableFuture<Client.ResultOfGetApiReference> result = EverSdk.async(ctxId,
//				                                                                         "client.get_api_reference",
//				                                                                         null,
//				                                                                         Client.ResultOfGetApiReference.class,
//				                                                                         null);
//				EverSdk.await(EverSdk.async(ctxId,"client.get_api_reference",
//				                                              null,
//				                                              Client.ResultOfGetApiReference.class,
//				                                              null));
//				executor.submit(() -> result.get());
//			}
//			executor.awaitTermination(100, TimeUnit.SECONDS);
//		}
//	}

}