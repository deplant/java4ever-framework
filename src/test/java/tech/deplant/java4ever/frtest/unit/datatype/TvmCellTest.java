package tech.deplant.java4ever.frtest.unit.datatype;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.*;
import tech.deplant.java4ever.frtest.unit.Env;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
class TvmCellTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void empty_cell_equals_empty_builder() throws EverSdkException {
		assertEquals(TvmCell.EMPTY(),TvmCell.builder().toCell(SDK_EMPTY));
	}

	@Test
	public void original_equals_decoded() throws EverSdkException {

		String source = "I want to be equal!!!";

		List<AbiType> types = List.of(Uint.fromJava(32, "200"),
		                              Uint.fromJava(64, "300"),
		                              SolBytes.fromJava(source));

		var builder = new TvmBuilder();
		builder.store(types.toArray(AbiType[]::new));

		var result = builder.toCell(SDK_EMPTY).decodeAndGet(SDK_EMPTY, types.stream()
		                                               .map(AbiType::abiTypeName)
		                                               .toArray(String[]::new), 2);

		assertEquals(source,result.toString());
	}

	@Test
	void fromJava() {
	}

	@Test
	void toJava() {
	}

	@Test
	void toABI() {
	}

	@Test
	void cellBoc() {
	}
}