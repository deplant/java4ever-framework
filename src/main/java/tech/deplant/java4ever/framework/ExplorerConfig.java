package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.LocalJsonArtifact;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.StaticCredentials;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record ExplorerConfig(String endpoint, Map<String, SavedContract> contracts,
                             Map<String, StaticCredentials> credentials) implements Artifact<String> {

    private static Path EXPLORER_CONFIG_PATH = Paths.get(System.getProperty("user.dir") + "/.j4e/config/explorer.json");
    private static Logger log = LoggerFactory.getLogger(ExplorerConfig.class);

    public static ExplorerConfig EMPTY(String endpoint) throws IOException {
        var config = new ExplorerConfig(endpoint, new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
        config.sync();
        return config;
    }

    public static ExplorerConfig LOAD() throws JsonProcessingException {
        var mapper = Sdk.DEFAULT_MAPPER;//.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.readValue(new LocalJsonArtifact(EXPLORER_CONFIG_PATH).read(), ExplorerConfig.class);
    }

    public void add(String name, OwnedContract contract) throws IOException {
        contracts().put(name, new SavedContract(contract.abi().json(), contract.address().makeAddrStd()));
        sync();
    }

    public void add(String name, StaticCredentials keys) throws IOException {
        credentials().put(name, keys);
        sync();
    }

    public void sync() throws IOException {
        var mapper = Sdk.DEFAULT_MAPPER;
        //.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        //.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
    }

    @Override
    public void write(String content) throws IOException {
        log.info("Writing string to path: " + EXPLORER_CONFIG_PATH.toString());
        Files.writeString(EXPLORER_CONFIG_PATH, content, StandardCharsets.UTF_8);
    }

    @Override
    public String read() {
        try {
            log.info("Reading string from path: " + EXPLORER_CONFIG_PATH.toString());
            return Files.readString(EXPLORER_CONFIG_PATH).replaceAll("[\u0000-\u001f]", "");
        } catch (IOException e) {
            log.error("File access error! Path: " + EXPLORER_CONFIG_PATH.toString() + ", Error: " + e.getMessage());
            return "";
        }
    }

    public record SavedContract(String abiJson, String address) {
    }
}
