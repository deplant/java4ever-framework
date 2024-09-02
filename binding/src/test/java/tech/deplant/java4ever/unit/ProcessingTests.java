package tech.deplant.java4ever.unit;

import com.yegor256.OnlineMeans;
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
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class ProcessingTests {

	private static final Logger log = LoggerFactory.getLogger(ProcessingTests.class);

	@BeforeAll
	public static void loadSdk() {
		TestEnv.loadEverSdk();
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void cancel_monitor_not_throws_for_random_queue() {
		int ctxId = TestEnv.newContext();
		assertDoesNotThrow(() -> Processing.cancelMonitor(ctxId,"aaa"));
	}

}