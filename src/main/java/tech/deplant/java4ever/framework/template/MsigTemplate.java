package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ArtifactABI;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.artifact.ArtifactTVC;
import tech.deplant.java4ever.framework.artifact.ITvc;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate {

    public static final ArtifactABI SAFE_MULTISIG_ABI = ArtifactABI.ofResource("/artifacts/multisig/SafeMultisigWallet.abi.json");
    public static final ArtifactABI SETCODE_MULTISIG_ABI = ArtifactABI.ofResource("/artifacts/multisig/SetcodeMultisigWallet.abi.json");
    public static final ArtifactABI SURF_MULTISIG_ABI = ArtifactABI.ofResource("/artifacts/multisig/SurfMultisigWallet.abi.json");

    public static final ArtifactTVC SAFE_MULTISIG_TVC = ArtifactTVC.ofResource("/artifacts/multisig/SafeMultisigWallet.tvc");
    public static final ArtifactTVC SETCODE_MULTISIG_TVC = ArtifactTVC.ofResource("/artifacts/multisig/SetcodeMultisigWallet.tvc");
    public static final ArtifactTVC SURF_MULTISIG_TVC = ArtifactTVC.ofResource("/artifacts/multisig/SurfMultisigWallet.tvc");

    public static final MsigTemplate SAFE_MULTISIG_TEMPLATE = new MsigTemplate(SAFE_MULTISIG_ABI, SAFE_MULTISIG_TVC);
    public static final MsigTemplate SETCODE_MULTISIG_TEMPLATE = new MsigTemplate(SETCODE_MULTISIG_ABI, SETCODE_MULTISIG_TVC);
    public static final MsigTemplate SURF_MULTISIG_TEMPLATE = new MsigTemplate(SURF_MULTISIG_ABI, SURF_MULTISIG_TVC);

    public MsigTemplate(IAbi abi, ITvc tvc) {
        super(abi, tvc);
    }

    public Msig deployWithGiver(Sdk sdk, int wid, Credentials keys, Giver giver, BigInteger value) throws Sdk.SdkException {
        var params = Map.<String, Object>of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        return new Msig(super.deployWithGiver(sdk, giver, value, wid, null, keys, params));
    }

}
