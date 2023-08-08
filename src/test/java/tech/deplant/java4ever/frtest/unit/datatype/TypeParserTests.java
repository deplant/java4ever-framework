package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.AbiType;
import tech.deplant.java4ever.framework.datatype.SolStruct;
import tech.deplant.java4ever.framework.datatype.AbiTypePrefix;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class TypeParserTests {


	@Test
	public void string_abi_type_parsing_uints() throws EverSdkException {
		assertEquals(new AbiType(AbiTypePrefix.UINT, 8, false), AbiType.of("uint8"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 8, true), AbiType.of("uint8[]"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 32, false), AbiType.of("uint32"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 32, true), AbiType.of("uint32[]"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 64, false), AbiType.of("uint64"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 64, true), AbiType.of("uint64[]"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 256, false), AbiType.of("uint256"));
		assertEquals(new AbiType(AbiTypePrefix.UINT, 256, true), AbiType.of("uint256[]"));
	}



}
