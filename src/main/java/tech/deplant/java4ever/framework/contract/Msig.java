package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public class Msig extends OwnedContract implements Giver {

    public Msig(Sdk sdk, Address address, Credentials owner, IAbi abi) {
        super(sdk, address, abi, owner);
    }

    public static Msig ofSafe(Sdk sdk, Address address) throws JsonProcessingException {
        return new Msig(sdk, address, Credentials.NONE, MsigTemplate.MsigAbiSafe(sdk));
    }

    public static Msig ofSafeOwned(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
        return new Msig(sdk, address, owner, MsigTemplate.MsigAbiSafe(sdk));
    }

    public static Msig ofSetcode(Sdk sdk, Address address) throws JsonProcessingException {
        return new Msig(sdk, address, Credentials.NONE, MsigTemplate.MsigAbiSetcode(sdk));
    }

    public static Msig ofSetcodeOwned(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
        return new Msig(sdk, address, owner, MsigTemplate.MsigAbiSetcode(sdk));
    }

    public static Msig ofSurf(Sdk sdk, Address address) throws JsonProcessingException {
        return new Msig(sdk, address, Credentials.NONE, MsigTemplate.MsigAbiSurf(sdk));
    }

    public static Msig ofSurfOwned(Sdk sdk, Address address, Credentials owner) throws JsonProcessingException {
        return new Msig(sdk, address, owner, MsigTemplate.MsigAbiSurf(sdk));
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