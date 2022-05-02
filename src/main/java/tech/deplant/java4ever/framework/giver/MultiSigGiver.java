package tech.deplant.java4ever.framework.giver;

import lombok.Value;
import tech.deplant.java4ever.framework.*;

import java.math.BigInteger;
import java.util.HashMap;

@Value
public class MultiSigGiver implements Giver {

    Credentials credentials;
    Account account;

    public MultiSigGiver(Sdk sdk, Address address, Credentials credentials) throws Sdk.SdkException {
        this.credentials = credentials;
        this.account = Account.ofAddress(
                sdk,
                address,
                ContractAbi.SAFE_MULTISIG
        );
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        var params = new HashMap<String, Object>();
        //				{"name":"dest","type":"address"},
        //				{"name":"value","type":"uint128"},
        //				{"name":"bounce","type":"bool"},
        //				{"name":"flags","type":"uint8"},
        //				{"name":"payload","type":"cell"}'
        params.put("dest", to.makeAddrStd());
        params.put("value", amount);
        params.put("bounce", false);
        params.put("flags", 0);
        params.put("payload", "");
        this.account.callExternal(this.credentials, "sendTransaction", params);
    }
}
