package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate {

    public static final ContractTvc SAFE_MULTISIG_TVC = ContractTvc.ofResource("artifacts/multisig/SafeMultisigWallet.tvc");
    public static final ContractTvc SETCODE_MULTISIG_TVC = ContractTvc.ofResource("artifacts/multisig/SetcodeMultisigWallet.tvc");
    public static final ContractTvc SURF_MULTISIG_TVC = ContractTvc.ofResource("artifacts/multisig/SurfMultisigWallet.tvc");

    public MsigTemplate(ContractAbi abi, ContractTvc tvc) {
        super(abi, tvc);
    }

    public static ContractAbi SAFE_MULTISIG_ABI() throws JsonProcessingException {
        return ContractAbi.ofResource("artifacts/multisig/SafeMultisigWallet.abi.json");
    }

    public static ContractAbi SETCODE_MULTISIG_ABI() throws JsonProcessingException {
        return ContractAbi.ofResource("artifacts/multisig/SetcodeMultisigWallet.abi.json");
    }

    public static ContractAbi SURF_MULTISIG_ABI() throws JsonProcessingException {
        return ContractAbi.ofResource("artifacts/multisig/SurfMultisigWallet.abi.json");
    }

    public static MsigTemplate SAFE() throws JsonProcessingException {
        return new MsigTemplate(SAFE_MULTISIG_ABI(), SAFE_MULTISIG_TVC);
    }

    public static MsigTemplate SETCODE() throws JsonProcessingException {
        return new MsigTemplate(SETCODE_MULTISIG_ABI(), SETCODE_MULTISIG_TVC);
    }

    public static MsigTemplate SURF() throws JsonProcessingException {
        return new MsigTemplate(SURF_MULTISIG_ABI(), SURF_MULTISIG_TVC);
    }

    public Msig deploySingleSig(Sdk sdk,
                                Credentials keys,
                                Giver giver,
                                BigInteger value) throws Sdk.SdkException {
        var params = Map.<String, Object>of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        var contract = super.deployWithGiver(sdk, giver, value, 0, null, keys, params);
        return new Msig(contract.sdk(), contract.address(), contract.tvmKey(), contract.abi());
    }

    public Msig deploy3of5(Sdk sdk,
                           Credentials keys,
                           Giver giver,
                           BigInteger value,
                           String pubkey2,
                           String pubkey3,
                           String pubkey4,
                           String pubkey5) throws Sdk.SdkException {
        var params = Map.<String, Object>of(
                "owners", new String[]{keys.publicKey(), pubkey2, pubkey3, pubkey4, pubkey5},
                "reqConfirms", 1);
        var contract = super.deployWithGiver(sdk, giver, value, 0, null, keys, params);
        return new Msig(contract);
    }

}
