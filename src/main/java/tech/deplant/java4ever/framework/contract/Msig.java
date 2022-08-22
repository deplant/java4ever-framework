package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public record Msig(IContract contract) implements IContract, Giver {

    public Msig(Sdk sdk, Address address) {
        super(sdk, address, Credentials.NONE, MsigTemplate.SAFE_MULTISIG_ABI);
    }

    public Msig(Sdk sdk, Address address, Credentials owner) {
        super(sdk, address, owner, MsigTemplate.SAFE_MULTISIG_ABI);
    }

//    public static Msig ofConfig(Sdk sdk, Artifact artifact, String msigUniqueName) throws JsonProcessingException {
//        return new Msig(ControllableContract.ofConfig(sdk, artifact, "msig_" + msigUniqueName));
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
        callExternal("sendTransaction", params, null);
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        send(to, amount, false, 1, "");
    }
}