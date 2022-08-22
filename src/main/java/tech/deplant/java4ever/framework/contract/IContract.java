package tech.deplant.java4ever.framework.contract;

import lombok.NonNull;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IContract {

    Sdk sdk();

    Address address();

    IAbi abi();

    Credentials tvmKey();

    Account account();

    Map<String, Object> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);

    Map<String, Object> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials);

    Map<String, Object> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);

    Map<String, Object> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials);

    String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);

    interface Cacheable {
        void save(Artifact artifact) throws IOException;
    }


    interface ExternallyOwnable {

    }

    interface KnownTVC {

    }

    interface KnownABI {

    }

    interface ExternallyDeployable {

    }

    record Account(String id, int acc_type, String balance, String boc,
                   long last_paid) {
    }
}
