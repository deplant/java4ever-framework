package tech.deplant.java4ever.framework.contract;


import lombok.NonNull;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.giver.Giver;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public record Msig(IContract contract) implements IContract, Giver {

    public static Msig ofDeploy(Credentials keys, Giver giver, Sdk sdk) throws Sdk.SdkException {
        var template = ContractTemplate.SAFE_MULTISIG;
        var address = Address.ofFutureDeploy(sdk, ContractTemplate.SAFE_MULTISIG, 0, null, keys);
        giver.give(address, Data.EVER);
        var params = new HashMap<String, Object>();
        params.put("owners", new String[]{"0x" + keys.publicKey()});
        params.put("reqConfirms", 1);
        Map<String, Object> msg = template.deploy(sdk, 0, null, keys, params);
        return new Msig(Contract.ofAddress(template.abi(), address, sdk, keys, null));
    }

//    public static Msig ofLocalConfig(ExplorerConfig config, Sdk sdk, int msigNumber) throws Sdk.SdkException {
//        return new Msig(config.accountController("msig" + msigNumber, sdk));
//    }

//    public void storeTo(ExplorerConfig config, int msigNumber) {
//        config.addAccountController("msig" + msigNumber, contract());
//    }

    public void send(Address to, BigInteger amount, boolean sendBounce, int flags, String payload) throws Sdk.SdkException {
        Map<String, Object> params = Map.of(
                "dest", to.makeAddrStd(),
                "value", amount,
                "bounce", sendBounce,
                "flags", flags,
                "payload", payload);
        this.contract.callExternal("sendTransaction", params);
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        send(to, amount, false, 1, "");
    }

    @Override
    public CompletableFuture<Map<String, Object>> runGetter() {
        return this.contract.runGetter();
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName) {
        return this.contract.callExternal(functionName);
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs) {
        return this.contract.callExternal(functionName, functionInputs);
    }

    @Override
    public CompletableFuture<Map<String, Object>> callExternal(@NonNull String functionName, Map<String, Object> functionInputs, Credentials credentials) {
        return this.contract.callExternal(functionName, functionInputs, credentials);
    }
}