package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.AbiType;
import tech.deplant.java4ever.framework.datatype.AbiTypePrefix;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class TypeParserTests {

	@ParameterizedTest
	@ValueSource(ints = {8, 32, 64, 256})
	public void string_abi_type_parsing_uints(int size) throws EverSdkException {
		assertEquals(new AbiType(AbiTypePrefix.UINT, size, false), AbiType.of("uint%d".formatted(size)));
		assertEquals(new AbiType(AbiTypePrefix.UINT, size, true), AbiType.of("uint%d[]".formatted(size)));
	}

}
