package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AbiTests {

	private static System.Logger logger = System.getLogger(AbiTests.class.getName());


	@Test
	public void deserialized_abi_equals_original_json() throws JsonProcessingException {
		var jsonStr = new JsonResource("artifacts/tip31/TokenRoot.abi.json").get();
		var cachedStr = TIP3TokenRootTemplate.DEFAULT_ABI().json();
		assertEquals(JsonContext.ABI_JSON_MAPPER().readTree(jsonStr),
		             JsonContext.ABI_JSON_MAPPER().readTree(cachedStr));
	}

	@Test
	public void true_if_abi_has_function() throws JsonProcessingException {
		assertTrue(SafeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
		assertTrue(SetcodeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
		assertTrue(SurfMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
	}

	@Test
	public void false_if_abi_lacks_function() throws JsonProcessingException {
		assertFalse(SafeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("releaseKraken"));
	}

	@Test
	public void conversion_of_bigint_input_should_be_correct() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = SafeMultisigWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionInputs("sendTransaction",
				                       Map.of("value", new BigInteger("123")));
		assertEquals("0x7b", converted.get("value"));
	}

}
