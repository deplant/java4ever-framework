package tech.deplant.java4ever.unit;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoader;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class ContextTests {

	private static final Logger log = LoggerFactory.getLogger(ContextTests.class);

	@BeforeAll
	public static void loadSdk() {
		TestEnv.loadEverSdk();
	}

	@Test
	public void create_and_destroy() {
		EverSdk.destroy(TestEnv.newContextEmpty());
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void context_id_increments_as_we_open_new_contexts() {
		int ctxId = TestEnv.newContextEmpty();
		int startId = ctxId;
		assertTrue(startId >= 1);
		ctxId = TestEnv.newContext();
		assertTrue(ctxId >= startId + 1);
		ctxId = TestEnv.newContext();
		assertTrue(ctxId >= startId + 2);
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void config_from_builder_equals_config_from_json() throws EverSdkException, JsonProcessingException {
		int ctxId = EverSdk.builder().networkEndpoints(TestEnv.NODESE_ENDPOINT).build();
		int ctxId2 = EverSdk.createWithEndpoint(TestEnv.NODESE_ENDPOINT);
		var config1 = EverSdk.contextConfig(ctxId);
		var config2 = EverSdk.contextConfig(ctxId2);
		assertEquals(JsonContext.serialize(config1),JsonContext.serialize(config2));
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void binding_config_from_json_rewrites_to_preset_config() throws EverSdkException {
		var configJson = "{\"binding\":{\"library\":\"ton-client-java\",\"version\":\"1.5.0\"}}";
		int ctxId = EverSdk.builder().build();
		int ctxId2 = EverSdk.createWithJson(configJson);
		assertEquals(DefaultLoader.BINDING_LIBRARY_VERSION, EverSdk.await(Client.config(ctxId)).binding().version());
		assertEquals(DefaultLoader.BINDING_LIBRARY_NAME, EverSdk.await(Client.config(ctxId)).binding().library());
		assertEquals(DefaultLoader.BINDING_LIBRARY_VERSION, EverSdk.await(Client.config(ctxId2)).binding().version());
		assertEquals(DefaultLoader.BINDING_LIBRARY_NAME, EverSdk.await(Client.config(ctxId2)).binding().library());
	}

}
