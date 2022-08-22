package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    IAbi abi;

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

//    public static IContract ofConfig(Sdk sdk, Artifact artifact, String name) throws JsonProcessingException {
//        ExplorerCache.ContractRecord contract = ExplorerCache.read(artifact).get(name);
//        return new ControllableContract(sdk, new Address(contract.address()), contract.externalOwner(), new CachedABI(contract.abi()));
//    }

    protected static Account graphQLRequest(Sdk sdk, Address address) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQLFilter.In(new String[]{address.makeAddrStd()}));
        Net.ResultOfQueryCollection result = Net.queryCollection(sdk.context(),
                                                                 "accounts",
                                                                 filter,
                                                                 "id acc_type balance boc last_paid",
                                                                 null,
                                                                 null);
        try {
            return JSONContext.MAPPER.readValue(result.result()[0].toString(), Account.class);
        } catch (JsonProcessingException e) {
            log.error("JSON Parsing error! " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account account() {
        return graphQLRequest(this.sdk, this.address);
    }

    private Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs) {
        return functionInputs.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(), entry -> {
                    if (this.abi.hasInput(functionName, entry.getKey())) {
                        var type = this.abi.inputType(functionName, entry.getKey());
                        return switch (type) {
                            case "uint128", "uint256", "uint64", "uint32" -> switch (entry.getValue()) {
                                case BigInteger b -> "0x" + b.toString(16);
                                case Instant i -> "0x" + BigInteger.valueOf(i.getEpochSecond()).toString(16);
                                case String s && "0x".equals(s.substring(0, 2)) -> s;
                                case String s -> "0x" + s;
                                default -> entry.getValue();
                            };
                            case "address" -> switch (entry.getValue()) {
                                case Address a -> a.makeAddrStd();
                                default -> entry.getValue();
                            };
                            default -> entry.getValue();
                        };
                    } else {
                        log.error(() -> "Function " + functionName + " doesn't contain input (" + entry.getKey() + ") in ABI of " + this.address.makeAddrStd());
                        return null;
                    }
                }
        ));
    }

    public Boolean isActive() {
        return 1 == account().acc_type();
    }

    @Override
    public String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return Abi.encodeMessageBody(
                this.sdk.context(),
                this.abi.ABI(),
                new Abi.CallSet(functionName, functionHeader, convertInputs(functionName, functionInputs)),
                true,
                Credentials.NONE.signer(),
                null,
                dest.makeAddrStd()
        ).body();

    }

    protected Map<String, Object> processMessage(IAbi abi, Address address, Abi.DeploySet deploySet, Credentials credentials, String functionName, Abi.FunctionHeader functionHeader, Map<String, Object> functionInputs) {
        Processing.ResultOfProcessMessage result = Processing.processMessage(this.sdk.context(),
                                                                    abi.ABI(),
                                                                    address.makeAddrStd(),
                                                                    deploySet,
                                                                    new Abi.CallSet(functionName, functionHeader, functionInputs),
                                                                    credentials.signer(), null, false, null);
        return decodeOutputMessage(result.decoded());
    }

    public Map<String, Object> decodeOutputMessage(Processing.DecodedOutput decoded) {
            return decoded.output();
    }

    @Override
    public Map<String, Object> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return runGetter(functionName, functionInputs, functionHeader, this.tvmKey);
    }

    @Override
    public Map<String, Object> runGetter(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        convertInputs(functionName, functionInputs);
        Abi.ResultOfEncodeMessage msg =
                Abi.encodeMessage(
                        sdk().context(),
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

        Tvm.ResultOfRunTvm tvmExecuteResult = Tvm.runTvm(
                sdk().context(),
                msg.message(),
                account().boc(),
                null,
                this.abi.ABI(),
                null,
                false
        );
        return decodeOutputMessage(tvmExecuteResult.decoded());
    }

    @Override
    public Map<String, Object> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        convertInputs(functionName, functionInputs);
        return callExternal(functionName, functionInputs, functionHeader, this.tvmKey);
    }

    @Override
    public Map<String, Object> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        convertInputs(functionName, functionInputs);
        return processMessage(this.abi, this.address, null, credentials, functionName, null, functionInputs);
    }


    @Override
    public void save(Artifact artifact) throws IOException {
        ExplorerCache.flush(this, artifact);
    }

}
