package tech.deplant.java4ever.framework.contract;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Value;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

@Value
public class Msig extends ControllableContract implements Giver {

    public Msig(IContract contract) {
        super(contract.sdk(), contract.address(), contract.tvmKey(), contract.abi());
    }

    public Msig(Sdk sdk, Address address) {
        super(sdk, address, Credentials.NONE, ContractAbi.SAFE_MULTISIG);
    }

    public Msig(Sdk sdk, Address address, Credentials owner) {
        super(sdk, address, owner, ContractAbi.SAFE_MULTISIG);
    }

    public static Msig ofConfig(Sdk sdk, Artifact artifact, String msigUniqueName) throws JsonProcessingException {
        return new Msig(ControllableContract.ofConfig(sdk, artifact, "msig_" + msigUniqueName));
    }

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