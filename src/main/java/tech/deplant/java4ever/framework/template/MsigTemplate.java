package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ArtifactABI;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.ITvc;
import tech.deplant.java4ever.framework.artifact.LocalByteArtifact;
import tech.deplant.java4ever.framework.artifact.LocalJsonArtifact;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate {

    public static final LocalJsonArtifact SAFE_MULTISIG_ABI = new LocalJsonArtifact(Artifact.resourceToPath("/artifacts/multisig/SafeMultisigWallet.abi.json"));
    public static final LocalJsonArtifact SETCODE_MULTISIG_ABI = new LocalJsonArtifact(Artifact.resourceToPath("/artifacts/multisig/SetcodeMultisigWallet.abi.json"));
    public static final LocalJsonArtifact SURF_MULTISIG_ABI = new LocalJsonArtifact(Artifact.resourceToPath("/artifacts/multisig/SurfMultisigWallet.abi.json"));

    public static final LocalByteArtifact SAFE_MULTISIG_TVC = new LocalByteArtifact(Artifact.resourceToPath("/artifacts/multisig/SafeMultisigWallet.tvc"));
    public static final LocalByteArtifact SETCODE_MULTISIG_TVC = new LocalByteArtifact(Artifact.resourceToPath("/artifacts/multisig/SetcodeMultisigWallet.tvc"));
    public static final LocalByteArtifact SURF_MULTISIG_TVC = new LocalByteArtifact(Artifact.resourceToPath("/artifacts/multisig/SurfMultisigWallet.tvc"));

    //public static final MsigTemplate SAFE_MULTISIG_TEMPLATE = new MsigTemplate(SAFE_MULTISIG_ABI, SAFE_MULTISIG_TVC);
    //public static final MsigTemplate SETCODE_MULTISIG_TEMPLATE = new MsigTemplate(SETCODE_MULTISIG_ABI, SETCODE_MULTISIG_TVC);
    //public static final MsigTemplate SURF_MULTISIG_TEMPLATE = new MsigTemplate(SURF_MULTISIG_ABI, SURF_MULTISIG_TVC);

    public MsigTemplate(Sdk sdk, IAbi abi, ITvc tvc) {
        super(abi, tvc);
    }

    public static ArtifactABI MsigAbiSafe(Sdk sdk) throws JsonProcessingException {
        return new ArtifactABI(sdk, SAFE_MULTISIG_ABI);
    }

    public static ArtifactABI MsigAbiSetcode(Sdk sdk) throws JsonProcessingException {
        return new ArtifactABI(sdk, SETCODE_MULTISIG_ABI);
    }

    public static ArtifactABI MsigAbiSurf(Sdk sdk) throws JsonProcessingException {
        return new ArtifactABI(sdk, SURF_MULTISIG_ABI);
    }

    public Msig deployWithGiver(Sdk sdk, int wid, Credentials keys, Giver giver, BigInteger value) throws Sdk.SdkException {
        var params = Map.<String, Object>of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        var contract = super.deployWithGiver(sdk, giver, value, wid, null, keys, params);
        return new Msig(contract.sdk(), contract.address(), contract.tvmKey(), contract.abi());
    }

}
