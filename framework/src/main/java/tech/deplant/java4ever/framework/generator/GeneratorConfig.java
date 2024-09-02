package tech.deplant.java4ever.framework.generator;

import java.util.List;

/**
 * The type Generator config.
 */
public record GeneratorConfig(String targetDir,
                              String contractPkg,
                              String templatePkg,
                              List<GeneratorContract> contractList) {
	/**
	 * The type Generator contract.
	 */
	public record GeneratorContract(String contractPkg,
	                                String name,

									String contractNameMask,
	                                String templateNameMask,
	                                String abi,
	                                String tvc,
	                                Boolean shareOutputs,
	                                String[] interfaces) {
	}

}
