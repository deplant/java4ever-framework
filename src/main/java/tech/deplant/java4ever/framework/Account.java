package tech.deplant.java4ever.framework;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.type.AbiAddressConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract is a storage for all your contract stuff - abi, account info from blockchain.
 * There are no checks, so make sure that you're putting right infos and abis.
 */
@Log4j2
@Value
public class Account {

    Sdk sdk;
    Address address;
    ContractAbi abi;
    int status;
    BigDecimal balance;
    String boc;
    Instant lastPaidUtc;

    public Account(Sdk sdk, Address address, ContractAbi abi, int status, BigDecimal balance, String boc, Instant lastPaidUtc) {
        this.sdk = sdk;
        this.address = address;
        this.abi = abi;
        this.status = status;
        this.balance = balance;
        this.boc = boc;
        this.lastPaidUtc = lastPaidUtc;
    }

    public static Account ofAddress(Sdk sdk, Address address, ContractAbi abi) throws Sdk.SdkException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
        return new Account(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
    }

    public static List<Account> ofAddressList(Sdk sdk, List<Address> addresses, ContractAbi abi) throws Sdk.SdkException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(addresses.stream().map(tech.deplant.java4ever.framework.Address::makeAddrStd).toArray(String[]::new)));
        return Arrays
                .stream(sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result())
                .map(
                        obj -> {
                            var collection = new Gson().fromJson(new Gson().toJson(obj), Account.AccountQueryCollection.class);
                            return new Account(sdk, new Address(collection.id()), abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
                        }
                ).toList();
    }

    public boolean isActive() {
        return status() == 1;
    }

    public CompletableFuture<Map<String, Object>> runGetter(@NonNull String abiFunction, Map<String, Object> input) throws Sdk.SdkException {
        return Abi.encodeMessage(
                this.sdk().context(),
                this.abi.abiJson(),
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
                        boc(),
                        null,
                        this.abi.abiJson(),
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

    public CompletableFuture<Map<String, Object>> callInternalFromMsig(Credentials credentials, Address msigAddress, BigInteger transactionValue, String functionName, BigInteger functionValue, Map<String, Object> functionInputs, boolean functionBounce) throws Sdk.SdkException {
        return Abi.encodeMessageBody(
                this.sdk.context(),
                this.abi.abiJson(),
                new Abi.CallSet(functionName, null, functionInputs),
                true,
                credentials.signer(),
                null
        ).thenCompose(payload ->
                {
                    Map<String, Object> inputs = Map.of(
                            "dest", this.address.makeAddrStd(),
                            "value", transactionValue.toString(),
                            "bounce", true,
                            "flags", 0,
                            "payload", payload.body()
                    );
                    return processMessage(ContractAbi.SAFE_MULTISIG, msigAddress, null, credentials, "sendTransaction", null, inputs);
                }
        );
    }

    private CompletableFuture<Map<String, Object>> processMessage(ContractAbi abi, Address address, Abi.DeploySet deploySet, Credentials credentials, String functionName, Abi.FunctionHeader header, Map<String, Object> functionInputs) {
        return Processing.processMessage(this.sdk.context(),
                        abi.abiJson(),
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

    @Value
    private static class AccountQueryCollection {
        String id;
        int acc_type;
        String balance;
        String boc;
        long last_paid;
    }

}
