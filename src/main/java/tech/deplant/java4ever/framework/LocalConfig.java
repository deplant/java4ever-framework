package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.*;
import tech.deplant.java4ever.framework.template.AbstractTemplate;
import tech.deplant.java4ever.framework.template.Template;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static tech.deplant.java4ever.framework.LogUtils.error;

/**
 * The type Local config.
 */
public record LocalConfig(Artifact<String, String> artifact,
                          LocalInfo info) {

	private static System.Logger logger = System.getLogger(LocalConfig.class.getName());

	/**
	 * Empty local config.
	 *
	 * @param serializationPath the serialization path
	 * @param solcPath          the solc path
	 * @param linkerPath        the linker path
	 * @param stdLibPath        the std lib path
	 * @param sourcePath        the source path
	 * @param buildPath         the build path
	 * @return the local config
	 * @throws IOException the io exception
	 */
	public static LocalConfig EMPTY(
			String serializationPath,
			String solcPath,
			String linkerPath,
			String stdLibPath,
			String sourcePath,
			String buildPath) throws IOException {
		var path = Paths.get(serializationPath);
		Artifact<String, String> jsonArtifact = null;
		switch (Artifact.pathType(serializationPath)) {
			case ABSOLUTE, RELATIONAL -> jsonArtifact = new JsonFile(serializationPath);
			case RESOURCE -> jsonArtifact = new JsonResource(serializationPath);
		}
		var config = new LocalConfig(jsonArtifact,
		                             new LocalInfo(new Solc(solcPath),
		                                           new TvmLinker(linkerPath, stdLibPath),
		                                           sourcePath,
		                                           buildPath,
		                                           new ConcurrentHashMap<>(),
		                                           new ConcurrentHashMap<>(),
		                                           new ConcurrentHashMap<>()));
		config.sync();
		return config;
	}

	/**
	 * Load local config.
	 *
	 * @param serializationPath the serialization path
	 * @return the local config
	 * @throws JsonProcessingException the json processing exception
	 */
	public static LocalConfig LOAD(String serializationPath) throws JsonProcessingException {
		var mapper = JsonContext.ABI_JSON_MAPPER();
		//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		Artifact<String, String> jsonArtifact = null;
		switch (Artifact.pathType(serializationPath)) {
			case ABSOLUTE, RELATIONAL -> jsonArtifact = new JsonFile(serializationPath);
			case RESOURCE -> jsonArtifact = new JsonResource(serializationPath);
		}
		return new LocalConfig(jsonArtifact, mapper.readValue(new JsonFile(serializationPath).get(),
		                                                      LocalInfo.class));
	}

	/**
	 * Compile template template.
	 *
	 * @param filename     the filename
	 * @param contractName the contract name
	 * @return the template
	 * @throws JsonProcessingException the json processing exception
	 * @throws EverSdkException        the ever sdk exception
	 */
	public Template compileTemplate(String filename,
	                                String contractName) throws JsonProcessingException, EverSdkException {
		return compileTemplate(info().sourcePath(),
		                       info().buildPath(),
		                       filename,
		                       contractName,
		                       info().sourcePath());
	}

	/**
	 * Compile template template.
	 *
	 * @param sourcePath   the source path
	 * @param buildPath    the build path
	 * @param filename     the filename
	 * @param contractName the contract name
	 * @param libsPath     the libs path
	 * @return the template
	 * @throws JsonProcessingException the json processing exception
	 * @throws EverSdkException        the ever sdk exception
	 */
	public Template compileTemplate(String sourcePath,
	                                String buildPath,
	                                String filename,
	                                String contractName,
	                                String libsPath) throws JsonProcessingException, EverSdkException {
		var compilerResult = info().compiler().compileContract(
				contractName,
				filename,
				sourcePath,
				buildPath,
				libsPath);

		if (compilerResult == 0) {
			//var linkerResult = info().linker().assemblyContract(contractName, buildPath);
			//if (linkerResult == 0) {
			return new AbstractTemplate(
					ContractAbi.ofFile(buildPath + "/" + contractName + ".abi.json"),
					Tvc.ofFile(buildPath + "/" + contractName + ".tvc")
			);
			//} else {
			//error(logger, () -> "TvmLinker exit code:" + linkerResult);
			//return null;
			//}
		} else {
			error(logger, () -> "Sold exit code:" + compilerResult);
			throw new EverSdkException(new EverSdkException.ErrorResult(-600,
			                                                            "Compilation failed. Sold exit code:" +
			                                                            compilerResult), new Exception());
		}
	}

	/**
	 * Abi contract abi.
	 *
	 * @param name the name
	 * @return the contract abi
	 * @throws JsonProcessingException the json processing exception
	 */
	public ContractAbi abi(String name) throws JsonProcessingException {
		return ContractAbi.ofFile(info().abis().get(name));
	}

	/**
	 * Credentials credentials.
	 *
	 * @param name the name
	 * @return the credentials
	 * @throws JsonProcessingException the json processing exception
	 */
	public Credentials credentials(String name) throws JsonProcessingException {
		return Credentials.ofFile(info().keys().get(name));
	}

	/**
	 * Tvc tvc.
	 *
	 * @param name the name
	 * @return the tvc
	 */
	public Tvc tvc(String name) {
		return Tvc.ofFile(info().tvcs().get(name));
	}

	/**
	 * Add abi path.
	 *
	 * @param name    the name
	 * @param pathStr the path str
	 * @throws IOException the io exception
	 */
	public void addAbiPath(String name, String pathStr) throws IOException {
		info().abis().put(name, pathStr);
		sync();
	}

	/**
	 * Add tvc path.
	 *
	 * @param name    the name
	 * @param pathStr the path str
	 * @throws IOException the io exception
	 */
	public void addTvcPath(String name, String pathStr) throws IOException {
		info().tvcs().put(name, pathStr);
		sync();
	}

	/**
	 * Add keypair path.
	 *
	 * @param name    the name
	 * @param pathStr the path str
	 * @throws IOException the io exception
	 */
	public void addKeypairPath(String name, String pathStr) throws IOException {
		info().keys().put(name, pathStr);
		sync();
	}

	/**
	 * Sync.
	 *
	 * @throws IOException the io exception
	 */
	public void sync() throws IOException {
		var mapper = JsonContext.ABI_JSON_MAPPER();
		artifact().accept(mapper.writerWithDefaultPrettyPrinter()
		                        .writeValueAsString(info()));
	}

	/**
	 * The type Local info.
	 */
	public record LocalInfo(Solc compiler,
	                        TvmLinker linker,
	                        @JsonProperty("source_path") String sourcePath,
	                        @JsonProperty("build_path") String buildPath,
	                        Map<String, String> abis,
	                        Map<String, String> tvcs,
	                        Map<String, String> keys) {
	}
}
