package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public class Msig extends OwnedContract implements Giver {

    public Msig(IContract contract) {
        super(contract.sdk(), contract.address(), MsigTemplate.SAFE_MULTISIG_ABI, Credentials.NONE);
    }

    public Msig(Sdk sdk, Address address) {
        super(sdk, address, MsigTemplate.SAFE_MULTISIG_ABI, Credentials.NONE);
    }

    public Msig(Sdk sdk, Address address, Credentials owner) {
        super(sdk, address, MsigTemplate.SAFE_MULTISIG_ABI, owner);
    }

    public void send(Address to, BigInteger amount, boolean sendBounce, int flags, String payload) throws Sdk.SdkException {
        Map<String, Object> params = Map.of(
                "dest", to.makeAddrStd(),
                "value", amount,
                "bounce", sendBounce,
                "flags", flags,
                "payload", payload);
        super.callExternal("sendTransaction", params, null);
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        send(to, amount, false, 1, "");
    }
}