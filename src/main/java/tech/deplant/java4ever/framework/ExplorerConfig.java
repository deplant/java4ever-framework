package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExplorerConfig {

    Map<String, ContractConfig> contracts;

    public ExplorerConfig() {
        this.contracts = new HashMap<>();
    }

    public static ExplorerConfig ofConfigFile(Artifact<String> artifact) {
        try {
            return Sdk.DEFAULT_MAPPER.readValue(artifact.read(), ExplorerConfig.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void store(String path) throws IOException {
        FileWriter file = new FileWriter(path);
        try {
            var writer = Sdk.DEFAULT_MAPPER.writerWithDefaultPrettyPrinter();
            file.write(writer.writeValueAsString(this));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }


    public ExplorerConfig removeAccountController(String name) {
        this.contracts.remove(name);
        return this;
    }

//    public ExplorerConfig addAccountController(String name, ControllableContract controllableContract) {
//        this.contracts.put(name, new ExplorerConfig.ContractConfig(
//                controllableContract.account().abi().json(),
//                controllableContract.account().address().makeAddrStd(),
//                controllableContract.internalOwner() != null ? controllableContract.internalOwner().account().address().makeAddrStd() : null,
//                controllableContract.tvmKey())
//        );
//        return this;
//    }

//    public ControllableContract accountController(String name, Sdk sdk) throws Sdk.SdkException {
//        var acc = this.contracts.get(name);
//        if (acc == null) {
//            return null;
//        } else {
//            return ControllableContract.ofAddress(new ContractAbi(acc.abi()),
//                    new Address(acc.address()),
//                    sdk,
//                    acc.externalOwner(),
//                    null);
//        }
//    }

    public record ContractConfig(String abi, String address, String internalOwner, Credentials externalOwner){}
}
