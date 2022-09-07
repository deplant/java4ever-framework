package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ArtifactABI;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.crypto.StaticCredentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public record EverOSGiver(OwnedContract contract) implements Giver {

    public static Credentials KEYS = new StaticCredentials("2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16", "172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3");
    public static Address ADDRESS = new Address("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
    public static String ABI_FILE_NAME = "/artifacts/giver/GiverV2.abi.json";

    public EverOSGiver(Sdk sdk) throws JsonProcessingException {
        this(new OwnedContract(sdk, ADDRESS, ArtifactABI.ofResource(sdk, ABI_FILE_NAME), KEYS));
    }

    @Override
    public void give(Address to, BigInteger amount) throws Sdk.SdkException {
        Map<String, Object> params = Map.of(
                "dest", to,
                "value", amount,
                "bounce", false
        );
        this.contract.callExternal("sendTransaction", params, null);
    }
}
