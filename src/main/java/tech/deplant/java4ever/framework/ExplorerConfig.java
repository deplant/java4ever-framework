package tech.deplant.java4ever.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.contract.ControllableContract;
import tech.deplant.java4ever.framework.type.Address;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Value
@Log4j2
public class ExplorerConfig {

    Map<String, ContractConfig> contracts;

    public ExplorerConfig() {
        this.contracts = new HashMap<>();
    }

    public static ExplorerConfig ofConfigFile(Artifact artifact) {
        return new Gson().fromJson(artifact.getAsJsonString(), ExplorerConfig.class);
    }

    public void store(String path) throws IOException {
        FileWriter file = new FileWriter(path);
        try {
            var gson = new GsonBuilder().setPrettyPrinting().create();
            file.write(gson.toJson(this));
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

    public ExplorerConfig addAccountController(String name, ControllableContract controllableContract) {
        this.contracts.put(name, new ExplorerConfig.ContractConfig(
                controllableContract.account().abi().abiJsonString(),
                controllableContract.account().address().makeAddrStd(),
                controllableContract.internalOwner() != null ? controllableContract.internalOwner().account().address().makeAddrStd() : null,
                controllableContract.tvmKey())
        );
        return this;
    }

    public ControllableContract accountController(String name, Sdk sdk) throws Sdk.SdkException {
        var acc = this.contracts.get(name);
        if (acc == null) {
            return null;
        } else {
            return ControllableContract.ofAddress(new ContractAbi(acc.abi()),
                    new Address(acc.address()),
                    sdk,
                    acc.externalOwner(),
                    null);
        }
    }

    @Value
    public static class ContractConfig {
        String abi;
        String address;
        String internalOwner;
        Credentials externalOwner;
    }
}
