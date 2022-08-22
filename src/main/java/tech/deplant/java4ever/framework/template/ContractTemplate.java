package tech.deplant.java4ever.framework.template;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.artifact.ArtifactABI;
import tech.deplant.java4ever.framework.artifact.ArtifactTVC;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.artifact.ITvc;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class ContractTemplate implements IContractTemplate {

//    public static final ContractTemplate SAFE_MULTISIG = new ContractTemplate(
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json").getAsABI(),
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc").getAsTVC()
//    );

    @Getter
    private final IAbi abi;
    @Getter
    private final ITvc tvc;

    public ContractTemplate(IAbi abi, ITvc tvc) {
        this.abi = abi;
        this.tvc = tvc;
    }

    //TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

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

    protected IContract doDeploy(Sdk sdk, int workchainId, Address address, Map<String, Object> initialData, Credentials
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
        return new OwnedContract(sdk, address, credentials, this.abi);
    }

    @Override
    public IContract deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

    @Override
    public IContract deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws
            Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

}
