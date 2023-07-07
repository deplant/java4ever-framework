package tech.deplant.java4ever.framework.generator;

import java.util.List;

public record GeneratorConfig(String targetDir,
                              String contractPkg,
                              String templatePkg,
                              List<GeneratorContract> contractList) {
	public record GeneratorContract(String contractPkg, String name,
	                                String abi,
	                                String tvc,
	                                Boolean shareOutputs,
	                                String[] interfaces) {
	}

}
