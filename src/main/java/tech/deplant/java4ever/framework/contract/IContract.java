package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.template.ContractTemplate;

public interface IContract {

    Account account();

    ContractTemplate template();

    Credentials externalOwner();

    IContract internalOwner();

    interface ExternallyOwnable {

    }

    interface InternallyOwnable {

    }

    interface KnownTVC {

    }

    interface KnownABI {

    }

    interface ExternallyDeployable {

    }


//    CompletableFuture<Map<String, Object>> runGetter();
//
//    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName);
//
//    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs);
//
//    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Credentials credentials);
}
