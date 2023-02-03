package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record ExplorerConfig(String serializationPath, Map<String, SavedContract> contracts,
                             Map<String, Credentials> credentials) {

	private static System.Logger logger = System.getLogger(ExplorerConfig.class.getName());

	public static ExplorerConfig EMPTY(String serializationPath) throws IOException {
		var config = new ExplorerConfig(serializationPath, new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
		config.sync();
		return config;
	}

	public static ExplorerConfig LOAD(String configFilePath) throws JsonProcessingException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		return mapper.readValue(new JsonFile(configFilePath).get(), ExplorerConfig.class);
	}

	public Credentials keys(String keysName) {
		return credentials().get(keysName);
	}

	public String address(String contractName) {
		return contracts().get(contractName).address();
	}

	public ContractAbi abi(String contractName) throws JsonProcessingException {
		return ContractAbi.ofString(contracts().get(contractName).abiJson());
	}

	public OwnedContract contract(Sdk sdk, String contractName, String keysName) throws JsonProcessingException {
		return new OwnedContract(
				sdk,
				address(contractName),
				abi(contractName),
				keys(keysName));
	}

	/**
	 * Adds OwnedContract object to config with a given name as a key.
	 *
	 * @param name     key for finding contract in config later
	 * @param contract contract object
	 * @return OwnedContract that we successfully putted to config
	 * @throws IOException can be thrown by the call of sync() method
	 */
	public OwnedContract addContract(String name, OwnedContract contract) throws IOException {
		contracts().put(name,
		                new ExplorerConfig.SavedContract(contract.abi().json(), contract.address()));
		sync();
		return contract;
	}

	public Credentials addKeys(String name, Credentials keys) throws IOException {
		credentials().put(name, keys);
		sync();
		return keys;
	}

	/**
	 * Flushes this config to file on serializationPath()
	 *
	 * @throws IOException can be thrown by work with File
	 */
	public void sync() throws IOException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;
		//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		//.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		new JsonFile(serializationPath()).accept(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
	}

	public record SavedContract(String abiJson, String address) {
	}
}
