package tech.deplant.java4ever.framework;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.FileArtifact;

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

    public ArtifactConfig(Artifact fileArtifact) {

        JsonObject jsonRoot = JsonParser.parseString(fileArtifact.getAsJsonString()).getAsJsonObject();
        jsonRoot.get("contracts").getAsJsonArray().iterator().forEachRemaining(elem ->
                {
                    var obj = elem.getAsJsonObject();
                    this.templates.put(obj.get("name").getAsString(),
                            new ContractTemplate(
                                    FileArtifact.ofResourcePath(obj.get("abiPath").getAsString()).getAsABI(),
                                    FileArtifact.ofResourcePath(obj.get("tvcPath").getAsString()).getAsTVC()
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
