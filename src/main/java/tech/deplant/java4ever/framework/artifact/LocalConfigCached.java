package tech.deplant.java4ever.framework.artifact;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.template.ContractTemplate;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public record LocalConfigCached(String solcPath,
                                String linkerPath,
                                String stdLibPath,
                                String sourcePath,
                                String buildPath,
                                Map<String, ContractTemplate> templates) implements LocalConfig {


    public static LocalConfigCached of(Artifact artifact) {

        var templates = new HashMap<String, ContractTemplate>();
        JsonObject jsonRoot = JsonParser.parseString(artifact.getAsJsonString()).getAsJsonObject();
        jsonRoot.get("contracts").getAsJsonArray().iterator().forEachRemaining(elem ->
                {
                    var obj = elem.getAsJsonObject();
                    templates.put(obj.get("name").getAsString(),
                            new ContractTemplate(
                                    ContractAbi.ofArtifact(sdk, FileArtifact.ofResourcePath(obj.get("abiPath").getAsString())),
                                    ContractTvc.of(FileArtifact.ofResourcePath(obj.get("tvcPath").getAsString()))
                            )
                    );
                }
        );

        return new LocalConfigCached(
                jsonRoot.get("solc").getAsJsonObject().get("path").getAsString(),
                jsonRoot.get("linker").getAsJsonObject().get("path").getAsString(),
                jsonRoot.get("linker").getAsJsonObject().get("stdLibPath").getAsString(),
                jsonRoot.get("sourcePath").getAsString(),
                jsonRoot.get("buildPath").getAsString(),
                templates);
    }


    @Override
    public Solc getSolidityCompiler() {
        try {
            return new Solc(this.solcPath);
        } catch (Exception e) {
            log.warn("Solidity Compiler init failed! " + e.getMessage());
            return null;
        }

    }

    @Override
    public TvmLinker getTvmLinker() {
        try {
            return new TvmLinker(this.linkerPath, this.stdLibPath);
        } catch (Exception e) {
            log.warn("Linker init failed! " + e.getMessage());
            return null;
        }
    }

    @Override
    public ContractAbi getAbi(String name) {
        return this.templates().get(name).abi();
    }

    @Override
    public ContractTvc getTvc(String name) {
        return this.templates().get(name).tvc();
    }
}
