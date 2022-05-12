package tech.deplant.java4ever.framework.contract;


import lombok.Value;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.template.MsigTemplate;

import java.math.BigInteger;
import java.util.Map;

@Value
public class Msig extends ControllableContract implements Giver {

    public static Msig ofAddress(Sdk sdk, Address address) {
        var template = new MsigTemplate(sdk);
        return new Msig(Account.ofAddress(sdk, address, template.abi()), template);
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
        callExternalFromOwner("sendTransaction", params);
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        send(to, amount, false, 1, "");
    }
}