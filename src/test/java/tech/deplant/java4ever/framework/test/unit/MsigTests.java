package tech.deplant.java4ever.framework.test.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.SdkBuilder;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.framework.type.EVERAmount.EVER;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class MsigTests {

	static Sdk SDK;

	@BeforeAll
	public static void init_sdk_and_other_vars() throws JsonProcessingException {
		SDK = new SdkBuilder()
				.networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
				.create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

	@Test
	public void first_msig_deploy_passes_second_throws() throws Throwable {
		var keys = Credentials.RANDOM(SDK);
		Msig msig = MsigTemplate.SAFE()
		                        .deploySingleSig(SDK,
		                                         keys,
		                                         new EverOSGiver(SDK),
		                                         EVER.amount().multiply(new BigInteger("1")));
		assertTrue(msig.account().isActive());
		try {
			msig = MsigTemplate.SAFE()
			                   .deploySingleSig(SDK,
			                                    keys,
			                                    new EverOSGiver(SDK),
			                                    EVER.amount().multiply(new BigInteger("1")));
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
