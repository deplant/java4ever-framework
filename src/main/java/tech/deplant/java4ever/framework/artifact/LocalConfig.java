package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.abi.IAbi;

public interface LocalConfig {

    Solc getSolidityCompiler();

    TvmLinker getTvmLinker();

    IAbi getAbi(String name);

    ITvc getTvc(String name);

}
