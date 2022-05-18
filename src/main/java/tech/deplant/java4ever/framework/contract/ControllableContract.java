package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@AllArgsConstructor
public class ControllableContract implements IContract, IContract.Cacheable {

    @Getter
    Sdk sdk;
    @Getter
    Address address;
    @Getter
    Credentials tvmKey;
    @Getter
    ContractAbi abi;

//    public static CompletableFuture<ControllableContract> ofAddress(Sdk sdk, Address address, ContractAbi abi) throws Sdk.SdkException {
//        return graphQLRequest(sdk, address).thenApply(account ->
//                new ControllableContract(sdk, address, abi, account.acc_type(), Data.hexToDec(account.balance(), 9), account.boc(), Instant.ofEpochSecond(account.last_paid())));
//
//    }

    //    public static Collection<Account> ofAddressList(Sdk sdk, Iterable<Address> addresses, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(addresses.stream().map(tech.deplant.java4ever.framework.type.Address::makeAddrStd).toArray(String[]::new)));
//        return Arrays
//                .stream(sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result())
//                .map(
//                        obj -> {
//                            var collection = new Gson().fromJson(new Gson().toJson(obj), Account.AccountQueryCollection.class);
//                            return new Account(sdk, new Address(collection.id()), abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//                        }
//                ).toList();
//    }
    public static IContract ofConfig(Sdk sdk, Artifact artifact, String name) throws JsonProcessingException {
        ExplorerCache.ContractRecord contract = ExplorerCache.read(artifact).get(name);
        return new ControllableContract(sdk, new Address(contract.address()), contract.externalOwner(), ContractAbi.ofJson(contract.abi()));
    }

    protected static CompletableFuture<Account> graphQLRequest(Sdk sdk, Address address) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
        return Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)
                .thenApply(response -> {
                    try {
                        return JSONContext.MAPPER.readValue(response.result()[0].toString(), Account.class);
                    } catch (JsonProcessingException e) {
                        log.error("JSON Parsing error! " + e.getMessage());
                        return null;
                    }
                });
    }

    @Override
    public CompletableFuture<Account> account() {
        return graphQLRequest(this.sdk, this.address);
    }

    private void convertInputs(String functionName, Map<String, Object> functionInputs) {
        if (functionInputs != null) {
            functionInputs.forEach((key, value) ->
                    {
                        if (this.abi.hasInput(functionName, key)) {
                            var type = this.abi.inputType(functionName, key);
                            functionInputs.replace(key,
                                    switch (type) {
                                        case "uint128", "uint256", "uint64", "uint32" -> switch (value) {
                                            case BigInteger b -> "0x" + b.toString(16);
                                            case Instant i -> "0x" + BigInteger.valueOf(i.getEpochSecond()).toString(16);
                                            case String s && "0x".equals(s.substring(0, 2)) -> s;
                                            case String s -> "0x" + s;
                                            default -> value;
                                        };
                                        case "address" -> switch (value) {
                                            case Address a -> a.makeAddrStd();
                                            default -> value;
                                        };
                                        default -> value;
                                    }
                            );
                        } else {
                            log.error(() -> "Function " + functionName + " doesn't contain input (" + key + ") in ABI of " + this.address.makeAddrStd());
                        }
                    }
            );
        }
    }

    public CompletableFuture<Boolean> isActive() {
        return account().thenApply(Account::acc_type).thenApply(type -> type == 1);
    }


    @Override
    public CompletableFuture<String> encodeInternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        convertInputs(functionName, functionInputs);
        return Abi.encodeMessageBody(
                this.sdk.context(),
                this.abi.ABI(),
                new Abi.CallSet(functionName, functionHeader, functionInputs),
                true,
                Credentials.NONE.signer(),
                null
        ).thenApply(Abi.ResultOfEncodeMessageBody::body);

    }

    protected CompletableFuture<Map<String, Object>> processMessage(ContractAbi abi, Address address, Abi.DeploySet deploySet, Credentials credentials, String functionName, Abi.FunctionHeader functionHeader, Map<String, Object> functionInputs) {
        return Processing.processMessage(this.sdk.context(),
                        abi.ABI(),
                        address.makeAddrStd(),
                        deploySet,
                        new Abi.CallSet(functionName, functionHeader, functionInputs),
                        credentials.signer(), null, false, null)
                .thenApply(msg -> {
                    if (msg.decoded().isPresent()) {
                        return Message.decodeOutputMessage(msg.decoded().get());
                    } else {
                        return Map.of();
                    }
                });

    }

    @Override
    public CompletableFuture<Map<String, Object>> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return runGetter(functionName, functionInputs, functionHeader, this.tvmKey);
    }

    @Override
    public CompletableFuture<Map<String, Object>> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        convertInputs(functionName, functionInputs);
        CompletableFuture<Abi.ResultOfEncodeMessage> futureEncoded =
                Abi.encodeMessage(
                        this.sdk().context(),
                        this.abi.ABI(),
                        this.address.makeAddrStd(),
                        null,
                        new Abi.CallSet(
                                functionName,
                                null,
                                functionInputs
                        ),
                        credentials.signer(),
                        null
                );

        return account().thenCombineAsync(futureEncoded, (acc, body) -> Tvm.runTvm(
                this.sdk().context(),
                body.message(),
                acc.boc(),
                null,
                this.abi.ABI(),
                null,
                false
        ).join()).thenApply(tvm -> {
            if (tvm.decoded().isPresent()) {
                return Message.decodeOutputMessage(tvm.decoded().get());
            } else {
                return Map.of();
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        convertInputs(functionName, functionInputs);
        return callExternal(functionName, functionInputs, functionHeader, this.tvmKey);
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        convertInputs(functionName, functionInputs);
        return processMessage(this.abi, this.address, null, credentials, functionName, null, functionInputs);
    }


    @Override
    public void save(Artifact artifact) throws IOException {
        ExplorerCache.flush(this, artifact);
    }

}
