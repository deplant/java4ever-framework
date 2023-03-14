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


		var mapper = ContextBuilder.DEFAULT_MAPPER;

		var config = mapper.readValue(new JsonResource("codegen/generator-config.json").get(), GeneratorConfig.class);
		Path targetDirectory = Paths.get(config.targetDir());
		String contractPackage = config.contractPkg();
		String templatePackage = config.templatePkg();

		for (var contr : config.contractList()) {
			logger.log(System.Logger.Level.INFO, contr);
			var tvc = Objs.isNull(contr.tvc()) ? null : Tvc.ofResource(contr.tvc());
			ContractWrapper.generate(mapper.readValue(new JsonResource(contr.abi()).get(), Abi.AbiContract.class),
			                         tvc,
			                         targetDirectory,
			                         contr.name(),
			                         contractPackage,
			                         templatePackage,
			                         contr.interfaces());
		}
	}

}
