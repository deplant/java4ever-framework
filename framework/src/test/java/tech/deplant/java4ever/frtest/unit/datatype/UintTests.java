package tech.deplant.java4ever.frtest.unit.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class UintTests {

	@Test
	public void int_to_correct_hex() throws JsonProcessingException, EverSdkException {
		assertEquals("0x388a1b17", new Uint(948574999).toABI());
	}

	@Test
	public void string_to_correct_hex() throws JsonProcessingException, EverSdkException {
		assertEquals("0x388a1b17", new Uint("948574999").toABI());
	}

	@Test
	public void bigint_to_correct_hex() throws JsonProcessingException, EverSdkException {
		assertEquals("0x00000000388a1b17", Uint.of(128,new BigInteger("948574999")).toABI());
	}

	@ParameterizedTest
	@ValueSource(strings = { "-0x388a1b17", "-388a1b17" })
	public void correct_hexes_should_match_negative_bigint(String value) throws JsonProcessingException, EverSdkException {
		assertEquals(new BigInteger("-948574999"), new Uint(value).toJava());
	}

	@ParameterizedTest
	@ValueSource(strings = { "0x388a1b17", "388a1b17" })
	public void correct_hexes_should_match_positive_bigint(String value) throws JsonProcessingException, EverSdkException {
		assertEquals(new BigInteger("948574999"), new Uint(value).toJava());
	}

}
