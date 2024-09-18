package tech.deplant.java4ever.frtest.unit.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.template.*;
import tech.deplant.java4ever.frtest.unit.AbiTests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class TypeConversionTests {

	private static System.Logger logger = System.getLogger(AbiTests.class.getName());

	@Test
	public void bigint_passed_to_uint128_input_equals_to_hex() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = SafeMultisigWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionInputs("sendTransaction",
				                       Map.of("value", new BigInteger("123")));
		assertEquals("0x000000000000007b", converted.get("value"));
	}

	@Test
	public void bigdec_passed_to_uint128_input_equals_to_hex() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = SafeMultisigWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionInputs("sendTransaction",
				                       Map.of("value", new BigDecimal("123")));
		assertEquals("0x000000000000007b", converted.get("value"));
	}

	@Test
	public void hex_passed_to_uint128_output_equals_to_bigint() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = TIP3TokenWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionOutputs("balance",
				                       Map.of("value0", "0x7b"));
		assertEquals(new BigInteger("123"), converted.get("value0"));
	}

	@Test
	public void str_passed_to_addr_output_equals_to_str() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = TIP3TokenWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionOutputs("owner",
				                        Map.of("value0", "0:57df2b130505d23f69c26b5cc60861c2ad2b9417b5255c3fc443dbd683c7562d"));
		assertEquals("0:57df2b130505d23f69c26b5cc60861c2ad2b9417b5255c3fc443dbd683c7562d", converted.get("value0"));
	}

}
