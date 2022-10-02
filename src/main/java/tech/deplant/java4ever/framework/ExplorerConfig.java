package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.framework.artifact.JsonFile;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record ExplorerConfig(String endpoint, Map<String, SavedContract> contracts,
                             Map<String, Credentials> credentials) {

	private static String EXPLORER_CONFIG_PATH = System.getProperty("user.dir") + "/.j4e/config/explorer.json";
	private static Logger log = LoggerFactory.getLogger(ExplorerConfig.class);

	public static ExplorerConfig EMPTY(String endpoint) throws IOException {
		var config = new ExplorerConfig(endpoint, new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
		config.sync();
		return config;
	}

	public static ExplorerConfig LOAD() throws JsonProcessingException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		return mapper.readValue(new JsonFile(EXPLORER_CONFIG_PATH).get(), ExplorerConfig.class);
	}

	public void add(String name, OwnedContract contract) throws IOException {
		contracts().put(name, new SavedContract(contract.abi().json(), contract.address().makeAddrStd()));
		sync();
	}

	public void add(String name, Credentials keys) throws IOException {
		credentials().put(name, keys);
		sync();
	}

	public Credentials keys(String keysName) {
		return credentials().get(keysName);
	}

	public Address address(String contractName) {
		return new Address(contracts().get(contractName).address());
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

	public void sync() throws IOException {
		var mapper = ContextBuilder.DEFAULT_MAPPER;
		//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		//.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		new JsonFile(EXPLORER_CONFIG_PATH).accept(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
	}

	public record SavedContract(String abiJson, String address) {
	}
}
