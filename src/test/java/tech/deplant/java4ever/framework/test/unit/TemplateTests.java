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
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class TemplateTests {

	static Sdk SDK;
	private static System.Logger logger = System.getLogger(TemplateTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		SDK = Sdk.builder()
		         .networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
		         .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

	@Test
	public void multisig_code_hashes_should_be_equal_to_known() throws EverSdkException {
		assertEquals("80d6c47c4a25543c9b397b71716f3fae1e2c5d247174c52e2c19bd896442b105",
		             SafeMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK));
		assertEquals("e2b60b6b602c10ced7ea8ede4bdf96342c97570a3798066f3fb50a4b2b27a208",
		             SetcodeMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK));
		assertEquals("207dc560c5956de1a2c1479356f8f3ee70a59767db2bf4788b1d61ad42cdad82",
		             SurfMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK));
	}

	@Test
	public void template_updated_with_pubkey_should_calculate_to_correct_address() throws EverSdkException, JsonProcessingException {
//TODO Return tests

		//		assertEquals("0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7",
//		             new SurfMultisigWalletTemplate()
//				             .deploy(sdk,)
//				             .withUpdatedInitialData(SDK,
//				                                     "a828a9533949a4eba661d54674fb5d5aaa1e968ac0cdab88d1d71f91996bed48")
//				             .addressFromEncodedTvc(SDK)
//		);
//		assertFalse(
//				MsigTemplate
//						.SURF()
//						.withUpdatedInitialData(SDK,
//						                        "a828a9533949a4eba661d54674fb5d5aaa1e968ac0cdab88d1d71f91996bed48")
//						.isDeployed(SDK)
//		);
	}

	@Test
	public void template_updated_and_hashed_address_equal_to_deployset_message_address() {
//		assertEquals("0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7",
//		             MsigTemplate
//				             .SURF()
//				             .withUpdatedInitialData(SDK,
//				                                     "a828a9533949a4eba661d54674fb5d5aaa1e968ac0cdab88d1d71f91996bed48")
//				             .calculateAddress(SDK)
//		);
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
