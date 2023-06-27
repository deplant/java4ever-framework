package tech.deplant.java4ever.frtest.codegen;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.generator.ContractWrapper;
import tech.deplant.java4ever.framework.generator.GeneratorConfig;
import tech.deplant.java4ever.utils.Objs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class WrapperGenerationTests {

	private static System.Logger logger = System.getLogger(WrapperGenerationTests.class.getName());

	@Test
	public void generate() throws IOException, EverSdkException {
		ContractWrapper.generateFromConfig("codegen/generator-config.json");
	}

}
