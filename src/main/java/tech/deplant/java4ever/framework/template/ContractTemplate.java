package tech.deplant.java4ever.framework.template;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Solc;
import tech.deplant.java4ever.framework.TvmLinker;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.artifact.ContractTvc;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.contract.ControllableContract;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;
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
    private final ContractAbi abi;
    @Getter
    private final ContractTvc tvc;

    public ContractTemplate(ContractAbi abi, ContractTvc tvc) {
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
                                ContractAbi.ofArtifact(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".abi.json")),
                                ContractTvc.of(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".tvc"))
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

    protected CompletableFuture<IContract> doDeploy(Sdk sdk, int workchainId, Address address, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        return Processing.processMessage(
                sdk.context(),
                this.abi.ABI(),
                null,
                new Abi.DeploySet(this.tvc.tvcString(), workchainId, initialData, credentials.publicKey()),
                new Abi.CallSet("constructor", null, constructorInputs),
                credentials.signer(),
                null,
                false,
                null
        ).thenApply(result -> new ControllableContract(sdk, address, credentials, this.abi));

    }

    @Override
    public CompletableFuture<IContract> deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

    @Override
    public CompletableFuture<IContract> deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws
            Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        return doDeploy(sdk, workchainId, address, initialData, credentials, constructorInputs);
    }

}
