package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.SolBytes;
import tech.deplant.java4ever.framework.datatype.SolString;
import tech.deplant.java4ever.frtest.unit.Env;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SolStringTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void abi_string_to_string_equals() {
		assertEquals("hello!", SolString.fromABI("hello!").toJava());
	}

	@Test
	public void string_to_abi_string_equals() {
		assertEquals("hello!", SolString.fromJava("hello!").toABI());
	}

}
