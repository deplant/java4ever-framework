package tech.deplant.java4ever.unit;

import com.yegor256.OnlineMeans;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.ffi.EverSdkSubscription;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SubscribeTests {

	private final static System.Logger logger = System.getLogger(SubscribeTests.class.getName());

	@BeforeAll
	public static void loadSdk() {
		TestEnv.loadEverSdk();
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void subscribe_to_account() throws EverSdkException, ExecutionException, InterruptedException {
		//var configJson = STR."{\"network\":{\"endpoints\":[\"\{TestEnv.NODESE_ENDPOINT}\"]}}";
		int ctxId = TestEnv.newContext();
		String queryText = """
				subscription {
							transactions(
									filter: {
										account_addr: { in: ["%s"] }
									}
				                ) {
								id
								account_addr
								balance_delta
							}
						}
				""".formatted("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
		var handle = Net.subscribe(ctxId,
		                           queryText,
		                           JsonContext.SDK_JSON_MAPPER().valueToTree(Map.of()),
		                           eventString -> logger.log(System.Logger.Level.WARNING,
		                                                                             "code: %s".formatted(eventString))).get()
		                .handle();

		assertTrue(handle > 0);

		Net.unsubscribe(ctxId, new Net.ResultOfSubscribeCollection(handle));

	}
}
