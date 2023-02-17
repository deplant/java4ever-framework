package tech.deplant.java4ever.framework.generator;

import java.util.List;

public record GeneratorConfig(String targetDir,
                              String contractPkg,
                              String templatePkg,
                              List<GeneratorContract> contractList) {
	public record GeneratorContract(String name,
	                                String abi,
	                                String tvc,
	                                String[] interfaces) {
	}

}
