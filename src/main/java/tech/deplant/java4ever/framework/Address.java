package tech.deplant.java4ever.framework;

import lombok.Value;
import tech.deplant.java4ever.binding.Abi;

import java.math.BigInteger;
import java.util.Map;

@Value
public class Address {

    public static final Address ZERO = new Address(0, BigInteger.ZERO);
    int wid;
    BigInteger value;

    public Address(int wid, BigInteger value) {
        this.wid = wid;
        this.value = value;
    }

    public Address(String address) {
        var splitted = address.split(":");
        this.wid = Integer.valueOf(splitted[0]);
        this.value = new BigInteger(splitted[1], 16);
    }

    public Address(BigInteger value) {
        this(0, value);
    }

    public static Address ofFutureDeploy(Sdk sdk, ContractTemplate template, int workchainId, Map<String, Object> initialData, Credentials credentials) throws Sdk.SdkException {
        return new Address(sdk.syncCall(Abi.encodeMessage(
                sdk.context(),
                template.abi().abiJson(),
                null,
                new Abi.DeploySet(template.tvc().tvcString(), workchainId, initialData, credentials.publicKey()),
                null,
                credentials.signer(),
                null
        )).address());
    }

    public boolean isNone() {
        return this.value.equals(BigInteger.ZERO);
    }

    //TODO Add definition of different address types

    /**
     * @return Returns type of the address:
     * 0 - addr_none 1 - addr_extern 2 - addr_std
     */
    public int getType() {
        return 0;
    }

    public String makeAddrStd() {
        return
                wid() +
                        ":" +
                        Data.padLeftZeros(value().toString(16), 64);
    }
}
