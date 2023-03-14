package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
class TvmCellTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException {
		Env.INIT();
	}

	@Test
	public void empty_cell_equals_empty_builder() throws EverSdkException {
		assertEquals(TvmCell.EMPTY(),TvmCell.builder().toCell(SDK_EMPTY));
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