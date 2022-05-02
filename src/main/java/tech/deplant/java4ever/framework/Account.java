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

/**
 * Contract is a storage for all your contract stuff - abi, account info from blockchain.
 * There are no checks, so make sure that you're putting right infos and abis.
 */
@Log4j2
@Value
public class Account {

    tech.deplant.java4ever.framework.Sdk sdk;
    tech.deplant.java4ever.framework.Address address;
    tech.deplant.java4ever.framework.ContractAbi abi;
    int status;
    BigDecimal balance;
    String boc;
    Instant lastPaidUtc;

    public Account(tech.deplant.java4ever.framework.Sdk sdk, tech.deplant.java4ever.framework.Address address, tech.deplant.java4ever.framework.ContractAbi abi, int status, BigDecimal balance, String boc, Instant lastPaidUtc) {
        this.sdk = sdk;
        this.address = address;
        this.abi = abi;
        this.status = status;
        this.balance = balance;
        this.boc = boc;
        this.lastPaidUtc = lastPaidUtc;
    }

    public static Account ofAddress(Sdk sdk, tech.deplant.java4ever.framework.Address address, tech.deplant.java4ever.framework.ContractAbi abi) throws Sdk.SdkException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
        return new Account(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
    }

    public static List<Account> ofAddressList(Sdk sdk, List<tech.deplant.java4ever.framework.Address> addresses, ContractAbi abi) throws Sdk.SdkException {
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

    private Abi.ResultOfEncodeMessage encodeMessage(@NonNull String abiFunction, Map<String, Object> input) throws Sdk.SdkException {
        return this.sdk.syncCall(Abi.encodeMessage(
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
        ));
    }

    public boolean isActive() {
        return status() == 1;
    }

    public Map<String, Object> runGetter(@NonNull String abiFunction, Map<String, Object> input) throws Sdk.SdkException {
        Tvm.ResultOfRunTvm msg = this.sdk.syncCall(Tvm.runTvm(
                        this.sdk().context(),
                        encodeMessage(abiFunction, input).message(),
                        boc(),
                        null,
                        this.abi.abiJson(),
                        null,
                        false
                )
        );
        if (msg.decoded().isPresent()) {
            return tech.deplant.java4ever.framework.Message.decodeOutputMessage(msg.decoded().get());
        } else {
            return new HashMap<>();
        }
    }

    public Map<String, Object> callExternal(Credentials credentials, String functionName, Map<String, Object> functionInputs) throws Sdk.SdkException {
        return Message.decodeOutputMessage(this.sdk.syncCall(Processing.processMessage(this.sdk.context(),
                this.abi.abiJson(),
                this.address().makeAddrStd(),
                null,
                new Abi.CallSet(functionName, null, functionInputs),
                credentials.signer(), null, false, null)).decoded().orElseThrow());
    }

    public Map<String, Object> callInternalFromMsig(Credentials credentials, Address msigAddress, BigInteger transactionValue, String functionName, BigInteger functionValue, Map<String, Object> functionInputs, boolean functionBounce) throws Sdk.SdkException {
        String payload = this.sdk.syncCall(
                Abi.encodeMessageBody(
                        this.sdk.context(),
                        this.abi.abiJson(),
                        new Abi.CallSet(functionName, null, functionInputs),
                        true,
                        credentials.signer(),
                        null
                )
        ).body();
        var msigInputs = new HashMap<String, Object>();
        //				{"name":"dest","type":"address"},
        //				{"name":"value","type":"uint128"},
        //				{"name":"bounce","type":"bool"},
        //				{"name":"flags","type":"uint8"},
        //				{"name":"payload","type":"cell"}'
        msigInputs.put("dest", this.address.makeAddrStd());
        msigInputs.put("value", transactionValue.toString());
        msigInputs.put("bounce", true);
        msigInputs.put("flags", 0);
        msigInputs.put("payload", payload);
        return Message.decodeOutputMessage(this.sdk.syncCall(Processing.processMessage(this.sdk.context(),
                ContractAbi.SAFE_MULTISIG.abiJson(),
                msigAddress.makeAddrStd(),
                null,
                new Abi.CallSet("sendTransaction", null, msigInputs),
                credentials.signer(), null, false, null)).decoded().orElseThrow());
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
