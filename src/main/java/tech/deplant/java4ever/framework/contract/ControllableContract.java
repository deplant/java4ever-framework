package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.type.AbiAddressConverter;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@AllArgsConstructor
public class ControllableContract implements IContract {

    @Getter
    Sdk sdk;
    @Getter
    Address address;
    @Getter
    Credentials tvmKey;
    @Getter
    ContractAbi abi;

//    public static ControllableContract ofAddress(Sdk sdk, Address address, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
//        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
//        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
//        return new ControllableContract(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//    }


//    public Map<String, Object> callInternalFromCustom(ControllableContract customSender, @NonNull String functionName, Map<String, Object> functionInputs, BigInteger functionValue, boolean functionBounce) throws Sdk.SdkException {
//        if (this.account.abi().hasFunction(functionName)) {
//            convertInputs(functionName, functionInputs);
//            Map<String, Object> result = this.account.callInternalFromMsig(
//                    customSender.externalOwner(),
//                    customSender.account().address(),
//                    functionValue,
//                    functionName,
//                    functionValue,
//                    functionInputs,
//                    functionBounce
//            );
//            this.account(refreshAcc());
//            return result;
//        } else {
//            log.error(() -> "Function (" + functionName + ") not found in ABI of " + this.account.address().makeAddrStd());
//            return new HashMap<>();
//        }
//    }

//    public Map<String, Object> callInternalFromOwner(@NonNull String functionName, Map<String, Object> functionInputs, BigInteger functionValue, boolean functionBounce) throws IOException, Sdk.SdkException {
//        return callInternalFromCustom(this.internalOwner, functionName, functionInputs, functionValue, functionBounce);
//    }

//    public static List<Account> ofAddressList(Sdk sdk, List<Address> addresses, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(addresses.stream().map(tech.deplant.java4ever.framework.Address::makeAddrStd).toArray(String[]::new)));
//        return Arrays
//                .stream(sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result())
//                .map(
//                        obj -> {
//                            var collection = new Gson().fromJson(new Gson().toJson(obj), Account.AccountQueryCollection.class);
//                            return new Account(sdk, new Address(collection.id()), abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//                        }
//                ).toList();
//    }

    private CompletableFuture<Account> account() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(new String[]{this.address.makeAddrStd()}));
        return Net.queryCollection(this.sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)
                .thenApply(response -> {
                    try {
                        return JSONContext.MAPPER.readValue(response.result()[0].toString(), Account.class);
                    } catch (JsonProcessingException e) {
                        log.error("JSON Parsing error! " + e.getMessage());
                        return null;
                    }
                });
    }

    public CompletableFuture<Map<String, Object>> callExternalFromOwner(@NonNull String functionName, Map<String, Object> functionInputs) throws Sdk.SdkException {
        convertInputs(functionName, functionInputs);
        CompletableFuture<Map<String, Object>> result = callExternal(this.tvmKey, functionName, functionInputs);
        return result;
    }

    public CompletableFuture<Map<String, Object>> runGetter(@NonNull String abiFunction, Map<String, Object> input) {
        convertInputs(abiFunction, input);
        return runGetter(abiFunction, input);
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

    public CompletableFuture<Map<String, Object>> runGetter(@NonNull String abiFunction, Map<String, Object> input) throws Sdk.SdkException {
        return Abi.encodeMessage(
                this.sdk().context(),
                this.abi.ABI(),
                this.address.makeAddrStd(),
                null,
                new Abi.CallSet(
                        abiFunction,
                        null,
                        input
                ),
                Abi.Signer.None,
                null
        ).thenCompose(body ->
                Tvm.runTvm(
                        this.sdk().context(),
                        body.message(),
                        account().boc(),
                        null,
                        this.abi.ABI(),
                        null,
                        false
                )
        ).thenApply(msg -> {
            if (msg.decoded().isPresent()) {
                return Message.decodeOutputMessage(msg.decoded().get());
            } else {
                return Map.of();
            }
        });
    }

    public CompletableFuture<Map<String, Object>> callExternal(Credentials credentials, String functionName, Map<String, Object> functionInputs) throws Sdk.SdkException {
        return processMessage(this.abi, this.address, null, credentials, functionName, null, functionInputs);
    }

//    public CompletableFuture<Map<String, Object>> callInternalFromMsig(Credentials credentials, Address msigAddress, BigInteger transactionValue, String functionName, BigInteger functionValue, Map<String, Object> functionInputs, boolean functionBounce) throws Sdk.SdkException {
//        return Abi.encodeMessageBody(
//                this.sdk.context(),
//                this.abi.ABI(),
//                new Abi.CallSet(functionName, null, functionInputs),
//                true,
//                credentials.signer(),
//                null
//        ).thenCompose(payload ->
//                {
//                    Map<String, Object> inputs = Map.of(
//                            "dest", this.address.makeAddrStd(),
//                            "value", transactionValue.toString(),
//                            "bounce", true,
//                            "flags", 0,
//                            "payload", payload.body()
//                    );
//                    return processMessage(ContractAbi.SAFE_MULTISIG, msigAddress, null, credentials, "sendTransaction", null, inputs);
//                }
//        );
//    }

    private CompletableFuture<Map<String, Object>> processMessage(ContractAbi abi, Address address, Abi.DeploySet deploySet, Credentials credentials, String functionName, Abi.FunctionHeader header, Map<String, Object> functionInputs) {
        return Processing.processMessage(this.sdk.context(),
                        abi.ABI(),
                        address.makeAddrStd(),
                        deploySet,
                        new Abi.CallSet(functionName, header, functionInputs),
                        credentials.signer(), null, false, null)
                .thenApply(msg -> {
                    if (msg.decoded().isPresent()) {
                        return Message.decodeOutputMessage(msg.decoded().get());
                    } else {
                        return Map.of();
                    }
                });

    }

    private Map<String, Object> convertInputsToCompatible(String functionName, Map<String, Object> functionInputs) {
        Map<String, Object> convertedInputs = new HashMap<>();
        functionInputs.forEach((inputName, passedObject) -> {
            switch (this.abi.inputType(functionName, inputName)) {
                case "address" -> convertedInputs.put(inputName, AbiAddressConverter.convert(passedObject));
                default -> convertedInputs.put(inputName, passedObject);
            }
        });
        return convertedInputs;
    }

    @Override
    public CompletableFuture<Map<String, Object>> runGetter() {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Credentials credentials) {
        return null;
    }

    private record Account(String id, int acc_type, String balance, String boc,
                           long last_paid) {
    }

}
