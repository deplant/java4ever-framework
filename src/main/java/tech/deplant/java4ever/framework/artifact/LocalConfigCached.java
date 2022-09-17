package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.abi.IAbi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record LocalConfigCached(Solc compiler,
                                TvmLinker linker,
                                String sourcePath,
                                String buildPath,
                                Map<String, IAbi> abis,
                                Map<String, IAbi> tvcs,
                                Map<String, IAbi> keys) implements Artifact<String> {

	private static Path LOCAL_CONFIG_PATH = Paths.get(System.getProperty("user.dir") + "/.j4e/config/local.json");

	private static Logger log = LoggerFactory.getLogger(LocalConfigCached.class);

	public static LocalConfigCached EMPTY(String solcPath,
	                                      String linkerPath,
	                                      String stdLibPath,
	                                      String sourcePath,
	                                      String buildPath) throws IOException {
		var config = new LocalConfigCached(new Solc(solcPath),
		                                   new TvmLinker(linkerPath, stdLibPath),
		                                   sourcePath,
		                                   buildPath,
		                                   new ConcurrentHashMap<>(),
		                                   new ConcurrentHashMap<>(),
		                                   new ConcurrentHashMap<>());
		config.sync();
		return config;
	}

	public static LocalConfigCached LOAD() {
		try {
			return Sdk.DEFAULT_MAPPER.readValue(new LocalJsonArtifact(LOCAL_CONFIG_PATH).read(),
			                                    LocalConfigCached.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void addAbi(String name, String pathStr) throws IOException {
		abis().put(name, new LocalJsonArtifact(Artifact.resourceToPath(pathStr)));
		sync();
	}

	public void addTvc(String name, String pathStr) throws IOException {
		tvcs().put(name, new LocalJsonArtifact(Artifact.resourceToPath(pathStr)));
		sync();
	}

	public void addKey(String name, String pathStr) throws IOException {
		keys().put(name, new LocalJsonArtifact(Artifact.resourceToPath(pathStr)));
		sync();
	}

	public void sync() throws IOException {
		write(Sdk.DEFAULT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this));
	}

	@Override
	public void write(String content) throws IOException {
		log.info("Writing string to path: " + LOCAL_CONFIG_PATH.toString());
		Files.writeString(LOCAL_CONFIG_PATH, content, StandardCharsets.UTF_8);
	}

	@Override
	public String read() {
		try {
			log.info("Reading string from path: " + LOCAL_CONFIG_PATH.toString());
			return Files.readString(LOCAL_CONFIG_PATH).replaceAll("[\u0000-\u001f]", "");
		} catch (IOException e) {
			log.error("File access error! Path: " + LOCAL_CONFIG_PATH.toString() + ", Error: " + e.getMessage());
			return "";
		}
	}
}
