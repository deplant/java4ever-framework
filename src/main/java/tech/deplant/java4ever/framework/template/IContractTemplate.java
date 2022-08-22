package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public interface IContractTemplate {

    IContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs);

    IContract deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs);

    IContractTemplate insertPublicKey();

    IContractTemplate updateInitialData();

}
