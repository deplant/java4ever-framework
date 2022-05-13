package tech.deplant.java4ever.framework.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.artifact.ContractTvc;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Log4j2
public class ContractTemplate<T extends IContract> {

//    public static final ContractTemplate SAFE_MULTISIG = new ContractTemplate(
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json").getAsABI(),
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc").getAsTVC()
//    );


    @Getter
    ContractAbi abi;
    @Getter
    ContractTvc tvc;

    //TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

    public static <U extends IContract> CompletableFuture<ContractTemplate<U>> ofSoliditySource(Class<U> type, Solc solc, TvmLinker tvmLinker, String solidityPath, String buildPath, String filename, String contractName) {
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
                        return new ContractTemplate<>(
                                ContractAbi.ofArtifact(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".abi.json")),
                                ContractTvc.of(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".tvc"))
                        );
                    } else {
                        log.error("TvmLinker exit code:" + linkerResult.exitValue());
                        return null;
                    }
                });
    }

    static <R extends Record> Constructor<R> canonicalConstructorOfRecord(Class<R> recordClass)
            throws NoSuchMethodException, SecurityException {
        Class<?>[] componentTypes = Arrays.stream(recordClass.getRecordComponents())
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
        return recordClass.getDeclaredConstructor(componentTypes);
    }

    public ContractTemplate<T> insertPublicKey() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    public ContractTemplate<T> updateInitialData() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    public T deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials
            credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        Message.decodeOutputMessage(sdk.syncCall(Processing.processMessage(
                sdk.context(),
                this.abi.ABI(),
                null,
                new Abi.DeploySet(this.tvc.tvcString(), workchainId, initialData, credentials.publicKey()),
                new Abi.CallSet("constructor", null, constructorInputs),
                credentials.signer(),
                null,
                false,
                null
        )).decoded().orElseThrow());

        Constructor<T> c = canonicalConstructorOfRecord(T.class);
        return c.newInstance(1, 2);

    }

    public T deployWithGiver(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<
            String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws
            Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        return deploy(sdk, workchainId, initialData, credentials, constructorInputs);
    }

}
