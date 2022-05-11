package tech.deplant.java4ever.framework.giver;

import lombok.Value;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.artifact.FileArtifact;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Value
public class EverOSGiver implements Giver {

    public static Credentials KEYS = new Credentials("2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16", "172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3");
    public static Address ADDRESS = new Address("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
    public static String ABI_FILE_NAME = "/artifacts/std/GiverV2.abi.json";

    Credentials credentials;
    Account account;

    public EverOSGiver(Sdk sdk) throws Sdk.SdkException {
        this.credentials = KEYS;
        this.account = Account.ofAddress(
                sdk,
                ADDRESS,
                FileArtifact.ofResourcePath(ABI_FILE_NAME).getAsABI()
        );
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        Map<String,Object> params = Map.of(
            "dest", to.makeAddrStd(),
            "value", amount,
            "bounce", false
        );
        this.account.callExternal(this.credentials, "sendTransaction", params);
    }
}
