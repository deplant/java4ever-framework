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

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@Log4j2
public class ContractTemplate {

//    public static final ContractTemplate SAFE_MULTISIG = new ContractTemplate(
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json").getAsABI(),
//            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc").getAsTVC()
//    );

    @Getter
    ContractAbi abi;
    @Getter
    ContractTvc tvc;

    //TODO convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)

    public static ContractTemplate ofSoliditySource(Solc solc, TvmLinker tvmLinker, String solidityPath, String buildPath, String filename, String contractName) {
        try {
            Process pSolc = solc.compileContract(
                    contractName,
                    filename,
                    solidityPath,
                    buildPath).get(300L, TimeUnit.SECONDS);
            if (pSolc.exitValue() == 0) {
                Process pLinker = tvmLinker.assemblyContract(
                        contractName,
                        buildPath
                ).get(300L, TimeUnit.SECONDS);
                if (pLinker.exitValue() == 0) {
                    return new ContractTemplate(
                            ContractAbi.ofArtifact(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".abi.json")),
                            ContractTvc.of(FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".tvc"))
                    );
                } else {
                    log.error("TvmLinker exit code:" + pLinker.exitValue());
                    return null;
                }
            } else {
                log.error("Solc exit code:" + pSolc.exitValue());
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public ContractTemplate insertPublicKey() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    public ContractTemplate updateInitialData() {
        //TODO return new ContractTemplate(this.abi, updated(this.tvc));
        return this;
    }

    public Map<String, Object> deploy(Sdk sdk, int workchainId, Map<String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        return Message.decodeOutputMessage(sdk.syncCall(Processing.processMessage(
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
    }

    public Map<String, Object> giveAndDeploy(Sdk sdk, Giver giver, BigInteger value, int workchainId, Map<String, Object> initialData, Credentials credentials, Map<String, Object> constructorInputs) throws Sdk.SdkException {
        var address = Address.ofFutureDeploy(sdk, this, 0, initialData, credentials);
        log.debug("Future address: " + address.makeAddrStd());
        giver.give(address, value);
        var result = sdk.syncCall(Processing.processMessage(
                sdk.context(),
                this.abi.ABI(),
                null,
                new Abi.DeploySet(this.tvc.tvcString(), workchainId, initialData, credentials.publicKey()),
                new Abi.CallSet("constructor", null, constructorInputs),
                credentials.signer(),
                null,
                false,
                null
        ));
        return Message.decodeOutputMessage(result.decoded().get());
    }

}
