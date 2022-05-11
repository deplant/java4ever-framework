package tech.deplant.java4ever.framework;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.giver.Giver;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Value
@Log4j2
public record ContractTemplate(ContractAbi abi,
                               ContractTvc tvc) {

    public static final ContractTemplate SAFE_MULTISIG = new ContractTemplate(
            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json").getAsABI(),
            FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc").getAsTVC()
    );

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
                            FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".abi.json").getAsABI(),
                            FileArtifact.ofAbsolutePath(buildPath + "/" + contractName + ".tvc").getAsTVC()
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
                this.abi.abiJson(),
                null,
                new Abi.DeploySet(tvc().tvcString(), workchainId, initialData, credentials.publicKey()),
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
                this.abi.abiJson(),
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

    public Map<String, Object> deployFromMsig(Sdk sdk,
                                              Credentials credentials,
                                              BigInteger value,
                                              Address msigAddress,
                                              int workchainId,
                                              Map<String, Object> initialData,
                                              Map<String, Object> constructorInputs,
                                              BigInteger constructorValue) throws Sdk.SdkException {
        var deployAddr = Address.ofFutureDeploy(sdk, this, workchainId, initialData, Credentials.NONE);
        String payload = sdk.syncCall(
                Abi.encodeInternalMessage(
                        sdk.context(),
                        this.abi.abiJson(),
                        deployAddr.makeAddrStd(),
                        msigAddress.makeAddrStd(),
                        new Abi.DeploySet(this.tvc.tvcString(), workchainId, initialData, Credentials.NONE.publicKey()),
                        new Abi.CallSet("constructor", null, constructorInputs),
                        constructorValue.toString(),
                        false,
                        false
                )
        ).message();

        var msigInputs = new HashMap<String, Object>();
        //				{"name":"dest","type":"address"},
        //				{"name":"value","type":"uint128"},
        //				{"name":"bounce","type":"bool"},
        //				{"name":"flags","type":"uint8"},
        //				{"name":"payload","type":"cell"}'
        msigInputs.put("dest", deployAddr.makeAddrStd());
        msigInputs.put("value", value.toString());
        msigInputs.put("bounce", false);
        msigInputs.put("flags", 0);
        msigInputs.put("payload", payload);
        return Message.decodeOutputMessage(sdk.syncCall(Processing.processMessage(sdk.context(),
                ContractAbi.SAFE_MULTISIG.abiJson(),
                msigAddress.makeAddrStd(),
                null,
                new Abi.CallSet("sendTransaction", null, msigInputs),
                credentials.signer(), null, false, null)).decoded().orElseThrow());
    }
}
