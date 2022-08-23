package tech.deplant.java4ever.framework.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.abi.ArtifactABI;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.template.ContractTemplate;

import java.util.HashMap;
import java.util.Map;

public record LocalConfigCached(String solcPath,
                                String linkerPath,
                                String stdLibPath,
                                String sourcePath,
                                String buildPath,
                                Map<String, ContractTemplate> templates) implements LocalConfig {


    private static Logger log = LoggerFactory.getLogger(LocalConfigCached.class);

    public static LocalConfigCached of(Artifact<String> artifact) throws JsonProcessingException {

        var templates = new HashMap<String, ContractTemplate>();
        JsonNode jsonRoot = Sdk.DEFAULT_MAPPER.readTree(artifact.read());
        jsonRoot.get("contracts").iterator().forEachRemaining(elem ->
                {
                    templates.put(elem.get("name").asText(),
                            new ContractTemplate(
                                    ArtifactABI.ofResource(elem.get("abiPath").asText()),
                                    ArtifactTVC.ofResource(elem.get("tvcPath").asText())
                            )
                    );
                }
        );

        return new LocalConfigCached(
                jsonRoot.get("solc").get("path").asText(),
                jsonRoot.get("linker").get("path").asText(),
                jsonRoot.get("linker").get("stdLibPath").asText(),
                jsonRoot.get("sourcePath").asText(),
                jsonRoot.get("buildPath").asText(),
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
    public IAbi getAbi(String name) {
        return this.templates().get(name).abi();
    }

    @Override
    public ITvc getTvc(String name) {
        return this.templates().get(name).tvc();
    }
}
