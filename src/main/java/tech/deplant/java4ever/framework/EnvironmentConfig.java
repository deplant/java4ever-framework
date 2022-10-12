package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.Solc;
import tech.deplant.java4ever.framework.artifact.TvmLinker;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractTemplate;
import tech.deplant.java4ever.framework.template.ContractTvc;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record EnvironmentConfig(String serializationPath,
                                Solc compiler,
                                TvmLinker linker,
                                String sourcePath,
                                String buildPath,
                                Map<String, String> abis,
                                Map<String, String> tvcs,
                                Map<String, String> keys) {

	private static Logger log = LoggerFactory.getLogger(EnvironmentConfig.class);

	public static EnvironmentConfig ofPaths(
			String serializationPath,
			String solcPath,
			String linkerPath,
			String stdLibPath,
			String sourcePath,
			String buildPath) throws IOException {
		var config = new EnvironmentConfig(serializationPath,
		                                   new Solc(solcPath),
		                                   new TvmLinker(linkerPath, stdLibPath),
		                                   sourcePath,
		                                   buildPath,
		                                   new ConcurrentHashMap<>(),
		                                   new ConcurrentHashMap<>(),
		                                   new ConcurrentHashMap<>());
		config.sync();
		return config;
	}

	public static EnvironmentConfig LOAD(String serializationPath) throws JsonProcessingException {
		return ContextBuilder.DEFAULT_MAPPER.readValue(new JsonFile(serializationPath).get(),
		                                               EnvironmentConfig.class);
	}

	public ContractTemplate compileTemplate(String filename,
	                                        String contractName) throws JsonProcessingException, EverSdkException {
		return compileTemplate(sourcePath(),
		                       buildPath(),
		                       filename,
		                       contractName);
	}

	public ContractTemplate compileTemplate(String sourcePath,
	                                        String buildPath,
	                                        String filename,
	                                        String contractName) throws JsonProcessingException, EverSdkException {
		var compilerResult = compiler().compileContract(
				contractName,
				filename,
				sourcePath,
				buildPath);

		if (compilerResult == 0) {
			var linkerResult = linker().assemblyContract(contractName, buildPath);
			if (linkerResult == 0) {
				return new ContractTemplate(
						ContractAbi.ofFile(buildPath + "/" + contractName + ".abi.json"),
						ContractTvc.ofFile(buildPath + "/" + contractName + ".tvc")
				);
			} else {
				log.error("TvmLinker exit code:" + linkerResult);
				return null;
			}
		} else {
			log.error("Solc exit code:" + compilerResult);
			throw new EverSdkException(new EverSdkException.ErrorResult(-600,
			                                                            "Compilation failed. Solc exit code:" +
			                                                            compilerResult), new Exception());
		}
	}

	public ContractAbi abi(String name) throws JsonProcessingException {
		return ContractAbi.ofFile(abis().get(name));
	}

	public Credentials credentials(String name) throws JsonProcessingException {
		return Credentials.ofFile(keys().get(name));
	}

	public ContractTvc tvc(String name) {
		return ContractTvc.ofFile(tvcs().get(name));
	}

	public void addAbiPath(String name, String pathStr) throws IOException {
		abis().put(name, pathStr);
		sync();
	}

	public void addTvcPath(String name, String pathStr) throws IOException {
		tvcs().put(name, pathStr);
		sync();
	}

	public void addKeypairPath(String name, String pathStr) throws IOException {
		keys().put(name, pathStr);
		sync();
	}

	public void sync() throws IOException {
		new JsonFile(serializationPath()).accept(ContextBuilder.DEFAULT_MAPPER.writerWithDefaultPrettyPrinter()
		                                                                      .writeValueAsString(this));
	}
}
