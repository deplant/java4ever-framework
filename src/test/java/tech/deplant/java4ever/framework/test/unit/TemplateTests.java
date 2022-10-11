package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.SdkBuilder;

import java.io.IOException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class TemplateTests {


	static Sdk SDK;
	private static Logger log = LoggerFactory.getLogger(TemplateTests.class);

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = new SdkBuilder()
				.create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

//	@Test
//	public void giver_switch_networks() throws Throwable {
//		MsigTemplate safeTemplate = MsigTemplate.SAFE();
//		Giver giver = null;
//		if (isEverOsNet()) {
//			Giver giver = new EverOSGiver(SDK);
//		} else {
//			Giver giver = Msig.ofSafe(SDK,
//			                          new Address("0:bd7a935b78f85929bc870e466a948f5b9927ac17299f9e45213c598979b83bef"),
//			                          keysOfMsig);
//		}
//		safeTemplate.deploySingleSig(
//				SDK,
//				Credentials.RANDOM(SDK),
//				giver,
//				EVER.amount());
//	}

}
