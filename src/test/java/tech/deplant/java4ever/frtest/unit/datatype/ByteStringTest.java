package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.ByteString;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.frtest.unit.Env;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ByteStringTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void hex_to_string_equals() throws EverSdkException {
		assertEquals("hello!", ByteString.fromABI("68656C6C6F21").toJava());
	}

}
