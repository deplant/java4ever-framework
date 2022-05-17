package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.artifact.ContractTvc;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.IContract;
import tech.deplant.java4ever.framework.contract.Msig;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate {

    public static final MsigTemplate SET_CODE_MULTISIG = new MsigTemplate(
            ContractAbi.ofArtifact(FileArtifact.ofResourcePath("/artifacts/multisig/SetcodeMultisigWallet.abi.json")),
            ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/multisig/SetcodeMultisigWallet.tvc")));

    public static final MsigTemplate SAFE_MULTISIG = new MsigTemplate(
            ContractAbi.ofArtifact(FileArtifact.ofResourcePath("/artifacts/multisig/SafeMultisigWallet.abi.json")),
            ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/multisig/SafeMultisigWallet.tvc")));

    public static final MsigTemplate SURF_MULTISIG = new MsigTemplate(
            ContractAbi.ofArtifact(FileArtifact.ofResourcePath("/artifacts/multisig/SurfMultisigWallet.abi.json")),
            ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/multisig/SurfMultisigWallet.tvc")));

    public MsigTemplate(ContractAbi abi, ContractTvc tvc) {
        super(abi, tvc);
    }

    public IContract deployWithGiver(Sdk sdk, int wid, Credentials keys, Giver giver, BigInteger value) throws Sdk.SdkException {
        //var template = new MsigTemplate(sdk);
        //var address = Address.ofFutureDeploy(sdk, template, 0, null, keys);
        //giver.give(address, Data.EVER);
        var params = Map.<String, Object>of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        //Map<String, Object> msg = template.deploy(sdk, 0, null, keys, params);
        return new Msig(super.deployWithGiver(sdk, giver, value, wid, null, keys, params));
    }

}
