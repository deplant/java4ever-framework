package tech.deplant.java4ever.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;

public class TestEnv {
	public static final String NODESE_URL = "https://nodese.truequery.tech";
	public static final String NODESE_ENDPOINT = "https://nodese.truequery.tech/graphql";

	static void loadEverSdk() {
		EverSdk.load();
	}

	static void loadAckiNackiSdk() {
		EverSdk.load(new AbsolutePathLoader("c:/opt/gosh-sdk/ton_client.dll"));
	}

	static int newContextEmpty() {
		try {
			return EverSdk.createDefault();
		} catch (EverSdkException e) {
			throw new RuntimeException(e);
		}
	}

	static int newContext() {
		try {
			return EverSdk.createWithEndpoint(NODESE_ENDPOINT);
		} catch (EverSdkException e) {
			throw new RuntimeException(e);
		}
	}


	@Test
	public void test_method()
	{
		byte num = (byte)127;
		num++;
		System.out.println(num);
	}

}
