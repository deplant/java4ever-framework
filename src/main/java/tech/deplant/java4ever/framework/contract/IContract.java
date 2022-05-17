package tech.deplant.java4ever.framework.contract;

import lombok.NonNull;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IContract {

    Sdk sdk();

    Address address();

    ContractAbi abi();

    Credentials tvmKey();

    CompletableFuture<Map<String, Object>> runGetter();

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName);

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs);

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Credentials credentials);


    interface ExternallyOwnable {

    }

    interface KnownTVC {

    }

    interface KnownABI {

    }

    interface ExternallyDeployable {

    }
}
