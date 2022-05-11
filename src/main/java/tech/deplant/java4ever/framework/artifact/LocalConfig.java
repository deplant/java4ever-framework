package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.ContractTvc;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;

public interface LocalConfig {

    Solc getSolidityCompiler();

    TvmLinker getTvmLinker();

    ContractAbi getAbi(String name);

    ContractTvc getTvc(String name);

}
