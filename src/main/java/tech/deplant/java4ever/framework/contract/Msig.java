package tech.deplant.java4ever.framework.contract;


import lombok.Value;
import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ContractAbi;

import java.math.BigInteger;
import java.util.Map;

@Value
public class Msig extends ControllableContract implements Giver {

    public Msig(Sdk sdk, Address address) {
        super(sdk, address, Credentials.NONE, ContractAbi.SAFE_MULTISIG);
    }

    public Msig(Sdk sdk, Address address, Credentials owner) {
        super(sdk, address, owner, ContractAbi.SAFE_MULTISIG);
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