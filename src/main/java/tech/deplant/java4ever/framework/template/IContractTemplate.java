package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;

import java.math.BigInteger;
import java.util.Map;

public interface IContractTemplate {

    public IContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs);

    public IContract deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs);

    public IContractTemplate insertPublicKey();

    public IContractTemplate updateInitialData();

}
