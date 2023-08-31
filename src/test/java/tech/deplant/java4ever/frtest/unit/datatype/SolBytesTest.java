package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.SolBytes;
import tech.deplant.java4ever.frtest.unit.Env;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SolBytesTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void bytes_to_string_equals() {
		assertEquals("hello!", new SolBytes("68656C6C6F21").toString());
	}

	@Test
	public void string_to_bytes_equals() {
		assertEquals("68656C6C6F21", new SolBytes(Strings.toHexString("hello!")).toHexString());
	}

}
