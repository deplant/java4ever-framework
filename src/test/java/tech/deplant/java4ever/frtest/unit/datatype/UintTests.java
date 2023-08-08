package tech.deplant.java4ever.frtest.unit.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class UintTests {

	@Test
	public void bigint_to_uint() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = SafeMultisigWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionInputs("sendTransaction",
				                       Map.of("value", new BigInteger("123")));
		assertEquals("0x7b", converted.get("value"));
	}

}
