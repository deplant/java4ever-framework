package tech.deplant.java4ever.framework.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public class ContractTemplate {

    private static Logger log = LoggerFactory.getLogger(ContractTemplate.class);
    private final ContractAbi abi;
    private final ContractTvc tvc;

    public ContractTemplate(ContractAbi abi, ContractTvc tvc) {
        this.abi = abi;
        this.tvc = tvc;
    }


    public ContractAbi abi() {
        return this.abi;
    }

    //TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

    public ContractTvc tvc() {
        return this.tvc;
    }

    protected OwnedContract doDeploy(Sdk sdk,
                                     int workchainId,
                                     Address address,
                                     Map<String, Object> initialData,
                                     Credentials
                                             credentials,
                                     Map<String, Object> constructorInputs) throws Sdk.SdkException {
        Processing.processMessage(
                sdk.context(),
                abi().ABI(),
                null,
                new Abi.DeploySet(
                        this.tvc.base64String(),
                        workchainId,
                        abi().convertInitDataInputs(initialData),
                        credentials.publicKey()),
                new Abi.CallSet(
                        "constructor",
                        null,
                        abi().convertFunctionInputs("constructor", constructorInputs)),
                credentials.signer(),
                null,
                false,
                null
        );
        return new OwnedContract(sdk, address, this.abi, credentials);
    }

    public OwnedContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

    public OwnedContract deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws
            Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

    public Map<String, Object> decodeInitialData(Sdk sdk) {
        return tvc().decodeInitialData(sdk, abi());
    }

    public String decodeInitialPubkey(Sdk sdk) {
        return tvc().decodeInitialPubkey(sdk, abi());
    }

    public ContractTemplate withUpdatedInitialData(Sdk sdk, Map<String, Object> initialData, String publicKey) {
        return new ContractTemplate(abi(),
                tvc().withUpdatedInitialData(sdk, abi(), initialData, publicKey));
    }

}
