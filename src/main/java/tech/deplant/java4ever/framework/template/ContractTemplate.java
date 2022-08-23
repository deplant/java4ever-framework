package tech.deplant.java4ever.framework.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.abi.ArtifactABI;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.artifact.ArtifactTVC;
import tech.deplant.java4ever.framework.artifact.ITvc;
import tech.deplant.java4ever.framework.contract.ActiveContract;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ContractTemplate implements IContractTemplate {

    private static Logger log = LoggerFactory.getLogger(ContractTemplate.class);
    private final IAbi abi;
    private final ITvc tvc;

    public ContractTemplate(IAbi abi, ITvc tvc) {
        this.abi = abi;
        this.tvc = tvc;
    }

    public static CompletableFuture<ContractTemplate> ofSoliditySource(Solc solc, TvmLinker tvmLinker, String solidityPath, String buildPath, String filename, String contractName) {
        return solc.compileContract(
                        contractName,
                        filename,
                        solidityPath,
                        buildPath)
                .thenCompose(solResult -> {
                    if (solResult.exitValue() == 0) {
                        return tvmLinker.assemblyContract(contractName, buildPath);
                    } else {
                        log.error("Solc exit code:" + solResult.exitValue());
                        return CompletableFuture.failedFuture(new RuntimeException("Solc exit code:" + solResult.exitValue()));
                    }
                }).thenApply(linkerResult -> {
                    if (linkerResult.exitValue() == 0) {
                        return new ContractTemplate(
                                ArtifactABI.ofAbsolute(buildPath + "/" + contractName + ".abi.json"),
                                ArtifactTVC.ofResource(buildPath + "/" + contractName + ".tvc")
                        );
                    } else {
                        log.error("TvmLinker exit code:" + linkerResult.exitValue());
                        return null;
                    }
                });
    }

    public IAbi abi() {
        return this.abi;
    }

    //TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

    public ITvc tvc() {
        return this.tvc;
    }

    @Override
    public IContractTemplate insertPublicKey() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    @Override
    public IContractTemplate updateInitialData() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    protected OwnedContract doDeploy(Sdk sdk, int workchainId, Address address, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        Processing.processMessage(
                sdk.context(),
                this.abi.ABI(),
                null,
                new Abi.DeploySet(this.tvc.base64String(), workchainId, initialData, credentials.publicKey()),
                new Abi.CallSet("constructor", null, constructorInputs),
                credentials.signer(),
                null,
                false,
                null
        );
        return new OwnedContract(new ActiveContract(sdk, address, this.abi), credentials);
    }

    @Override
    public OwnedContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

    @Override
    public OwnedContract deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws
            Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

}
