package tech.deplant.java4ever.framework.contract;

import lombok.NonNull;
import tech.deplant.java4ever.framework.Credentials;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IContract {
    CompletableFuture<Map<String, Object>> runGetter();

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName);

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs);

    CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Credentials credentials);
}
