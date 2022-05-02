package tech.deplant.java4ever.framework;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Value
@Log4j2
public class ArtifactConfig {

    public String solcPath;
    public String linkerPath;
    public String stdLibPath;
    public String sourcePath;
    public String buildPath;

    Map<String, ContractTemplate> templates = new HashMap<>();

    public ArtifactConfig(String configPath) {
        String str = null;
        try {
            str = FileData.jsonFromFile(configPath);
        } catch (IOException e) {
            log.error("Path: {}, Error: {}", () -> configPath, () -> e.getMessage());
        }
        JsonObject jsonRoot = JsonParser.parseString(str).getAsJsonObject();
        jsonRoot.get("contracts").getAsJsonArray().iterator().forEachRemaining(elem ->
                {
                    var obj = elem.getAsJsonObject();
                    this.templates.put(obj.get("name").getAsString(),
                            new ContractTemplate(
                                    ContractAbi.ofStored(obj.get("abiPath").getAsString()),
                                    ContractTvc.ofStored(obj.get("tvcPath").getAsString())
                            )
                    );
                }
        );
        this.solcPath = jsonRoot.get("solc").getAsJsonObject().get("path").getAsString();
        this.linkerPath = jsonRoot.get("linker").getAsJsonObject().get("path").getAsString();
        this.stdLibPath = jsonRoot.get("linker").getAsJsonObject().get("stdLibPath").getAsString();
        this.sourcePath = jsonRoot.get("sourcePath").getAsString();
        this.buildPath = jsonRoot.get("buildPath").getAsString();
    }

    public Solc getSolidityCompiler() {
        try {
            return new Solc(solcPath);
        } catch(Exception e) {
            log.error("Solidity Compiler init failed! " + e.getMessage());
            return null;
        }

    }

    public TvmLinker getTvmLinker() {
        try {
            return new TvmLinker(linkerPath, stdLibPath);
        } catch(Exception e) {
            log.error("Linker init failed! " + e.getMessage());
            return null;
        }
    }

    public ContractTemplate getTemplate(String name) {
        return this.templates.get(name);
    }
}
