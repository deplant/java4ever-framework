package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.contract.IContract;

import java.io.IOException;
import java.util.Map;

public interface ExplorerCache {

    static Map<String, ContractRecord> read(Artifact<String> artifact) throws JsonProcessingException {
        TypeReference<Map<String, ContractRecord>> typeRef
                = new TypeReference<>() {
        };
        return JSONContext.MAPPER.readValue(artifact.read(), typeRef);
    }

    static void flush(IContract contract, Artifact artifact) throws IOException {
        var content = JSONContext.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(
                new ContractRecord(contract.abi().json(), contract.address().makeAddrStd(), contract.tvmKey())
        );
        //TODO artifact.saveString(content);
    }

    record ContractRecord(String abi, String address, Credentials externalOwner) {
    }

}
