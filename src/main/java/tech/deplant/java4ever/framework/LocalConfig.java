package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.artifact.*;
import tech.deplant.java4ever.framework.template.Template;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static tech.deplant.java4ever.framework.LogUtils.error;

public record LocalConfig(Artifact<String, String> artifact,
                          LocalInfo info) {

	public record LocalInfo(Solc compiler,
	                 TvmLinker linker,
	                 String sourcePath,
	                 String buildPath,
	                 Map<String, String> abis,
	                 Map<String, String> tvcs,
	                 Map<String, String> keys) {}

	private static System.Logger logger = System.getLogger(LocalConfig.class.getName());

	public static LocalConfig EMPTY(
			String serializationPath,
			String solcPath,
			String linkerPath,
			String stdLibPath,
			String sourcePath,
			String buildPath) throws IOException {
		var path = Paths.get(serializationPath);
		Artifact<String,String> jsonArtifact = null;
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

	public static LocalConfig LOAD(String serializationPath) throws JsonProcessingException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;
		//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		Artifact<String,String> jsonArtifact = null;
		switch (Artifact.pathType(serializationPath)) {
			case ABSOLUTE, RELATIONAL -> jsonArtifact = new JsonFile(serializationPath);
			case RESOURCE -> jsonArtifact = new JsonResource(serializationPath);
		}
		return new LocalConfig(jsonArtifact,mapper.readValue(new JsonFile(serializationPath).get(),
		                                               LocalInfo.class));
	}

	public Template compileTemplate(String filename,
	                                String contractName) throws JsonProcessingException, EverSdkException {
		return compileTemplate(info().sourcePath(),
		                       info().buildPath(),
		                       filename,
		                       contractName);
	}

	public Template compileTemplate(String sourcePath,
	                                String buildPath,
	                                String filename,
	                                String contractName) throws JsonProcessingException, EverSdkException {
		var compilerResult = info().compiler().compileContract(
				contractName,
				filename,
				sourcePath,
				buildPath);

		if (compilerResult == 0) {
			var linkerResult = info().linker().assemblyContract(contractName, buildPath);
			if (linkerResult == 0) {
				return new Template.CustomTemplate(
						ContractAbi.ofFile(buildPath + "/" + contractName + ".abi.json"),
						Tvc.ofFile(buildPath + "/" + contractName + ".tvc")
				);
			} else {
				error(logger, () -> "TvmLinker exit code:" + linkerResult);
				return null;
			}
		} else {
			error(logger, () -> "Solc exit code:" + compilerResult);
			throw new EverSdkException(new EverSdkException.ErrorResult(-600,
			                                                            "Compilation failed. Solc exit code:" +
			                                                            compilerResult), new Exception());
		}
	}

	public ContractAbi abi(String name) throws JsonProcessingException {
		return ContractAbi.ofFile(info().abis().get(name));
	}

	public Credentials credentials(String name) throws JsonProcessingException {
		return Credentials.ofFile(info().keys().get(name));
	}

	public Tvc tvc(String name) {
		return Tvc.ofFile(info().tvcs().get(name));
	}

	public void addAbiPath(String name, String pathStr) throws IOException {
		info().abis().put(name, pathStr);
		sync();
	}

	public void addTvcPath(String name, String pathStr) throws IOException {
		info().tvcs().put(name, pathStr);
		sync();
	}

	public void addKeypairPath(String name, String pathStr) throws IOException {
		info().keys().put(name, pathStr);
		sync();
	}

	public void sync() throws IOException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;
		artifact().accept(mapper.writerWithDefaultPrettyPrinter()
		                                                                      .writeValueAsString(info()));
	}
}
