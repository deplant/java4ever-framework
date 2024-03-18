package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static tech.deplant.java4ever.framework.LogUtils.warn;

public record OnchainConfig(Artifact<String, String> artifact, OnchainInfo info) {

	private static System.Logger logger = System.getLogger(OnchainConfig.class.getName());

	public static OnchainConfig LOAD_IF_EXISTS(String serializationPath) throws IOException {
		OnchainConfig conf = null;
		try {
			conf = OnchainConfig.LOAD(serializationPath);
		} catch (Exception e) {
			conf = OnchainConfig.EMPTY(serializationPath);
			warn(logger, e.getMessage());
		}
		return conf;
	}

	public static OnchainConfig EMPTY(String serializationPath) throws IOException {
		var path = Paths.get(serializationPath);
		Artifact<String, String> jsonArtifact = null;
		switch (Artifact.pathType(serializationPath)) {
			case ABSOLUTE, RELATIONAL -> jsonArtifact = new JsonFile(serializationPath);
			case RESOURCE -> jsonArtifact = new JsonResource(serializationPath);
		}
		var config = new OnchainConfig(jsonArtifact,
		                               new OnchainInfo(new ConcurrentHashMap<>(), new ConcurrentHashMap<>()));
		config.sync();
		return config;
	}

	public static OnchainConfig LOAD(String serializationPath) throws JsonProcessingException {
		var mapper = JsonContext.ABI_JSON_MAPPER();
		//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		Artifact<String, String> jsonArtifact = null;
		switch (Artifact.pathType(serializationPath)) {
			case ABSOLUTE, RELATIONAL -> jsonArtifact = new JsonFile(serializationPath);
			case RESOURCE -> jsonArtifact = new JsonResource(serializationPath);
		}
		return new OnchainConfig(jsonArtifact, mapper.readValue(jsonArtifact.get(), OnchainInfo.class));
	}

	public Credentials keys(String keysName) {
		return info().credentials().get(keysName);
	}

	private <T extends AbstractContract> T instatiateContract(Class<T> clazz,
	                                                          int contextId,
	                                                          String contractName,
	                                                          Credentials credentials) throws JsonProcessingException {
		var contr = info().contracts().get(contractName);
		if (Optional.ofNullable(contr).isEmpty()) {
			return null;
		}
		return Contract.instantiate(clazz, contextId, contr.address(), ContractAbi.ofString(contr.abiJson()), credentials);
	}

	public <T extends AbstractContract> T contract(Class<T> clazz,
	                      int contextId,
	                      String contractName,
	                      String keysName) throws JsonProcessingException {
		return instatiateContract(clazz, contextId, contractName, keys(keysName));
	}

	public <T extends AbstractContract> T contract(Class<T> clazz,
	                      int contextId,
	                      String contractName) throws JsonProcessingException {
		return instatiateContract(clazz, contextId, contractName, Credentials.NONE);
	}

	/**
	 * Adds OwnedContract object to config with a given name as a key.
	 *
	 * @param name     key for finding contract in config later
	 * @param contract contract object
	 * @return OwnedContract that we successfully putted to config
	 * @throws IOException can be thrown by the call of sync() method
	 */
	public Contract addContract(String name, Contract contract) throws IOException {
		info().contracts().put(name,
		                       new OnchainConfig.SavedContract(contract.abi().json(), contract.address().makeAddrStd()));
		sync();
		logger.log(System.Logger.Level.INFO, "Saved contract: %s, name: %s".formatted(contract.address(), name));
		return contract;
	}

	public Credentials addKeys(String name, Credentials keys) throws IOException {
		info().credentials().put(name, keys);
		sync();
		logger.log(System.Logger.Level.INFO, "Saved keys: %s, name: %s".formatted(keys, name));
		return keys;
	}

	/**
	 * Flushes this config to file on serializationPath()
	 *
	 * @throws IOException can be thrown by work with File
	 */
	public void sync() throws IOException {
		var mapper = JsonContext.ABI_JSON_MAPPER();
		//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		//.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		artifact().accept(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(info()));
	}

	public record OnchainInfo(Map<String, SavedContract> contracts, Map<String, Credentials> credentials) {
	}

	public record SavedContract(@JsonProperty("abi_json") String abiJson, String address) {
	}
}
