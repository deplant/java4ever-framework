package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.CachedAbi;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.artifact.CachedTvc;
import tech.deplant.java4ever.framework.artifact.ITvc;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate {

	public static final ITvc SAFE_MULTISIG_TVC = CachedTvc.ofResource("/artifacts/multisig/SafeMultisigWallet.tvc");
	public static final ITvc SETCODE_MULTISIG_TVC = CachedTvc.ofResource("/artifacts/multisig/SetcodeMultisigWallet.tvc");
	public static final ITvc SURF_MULTISIG_TVC = CachedTvc.ofResource("/artifacts/multisig/SurfMultisigWallet.tvc");

	public MsigTemplate(Sdk sdk, IAbi abi, ITvc tvc) {
		super(abi, tvc);
	}

	public static IAbi SAFE_MULTISIG_ABI() throws JsonProcessingException {
		return CachedAbi.ofResource("/artifacts/multisig/SafeMultisigWallet.abi.json");
	}

	public static IAbi SETCODE_MULTISIG_ABI() throws JsonProcessingException {
		return CachedAbi.ofResource("/artifacts/multisig/SetcodeMultisigWallet.abi.json");
	}

	public static IAbi SURF_MULTISIG_ABI() throws JsonProcessingException {
		return CachedAbi.ofResource("/artifacts/multisig/SurfMultisigWallet.abi.json");
	}

	public static MsigTemplate SAFE(Sdk sdk) throws JsonProcessingException {
		return new MsigTemplate(sdk, SAFE_MULTISIG_ABI(), SAFE_MULTISIG_TVC);
	}

	public static MsigTemplate SETCODE(Sdk sdk) throws JsonProcessingException {
		return new MsigTemplate(sdk, SETCODE_MULTISIG_ABI(), SETCODE_MULTISIG_TVC);
	}

	public static MsigTemplate SURF(Sdk sdk) throws JsonProcessingException {
		return new MsigTemplate(sdk, SURF_MULTISIG_ABI(), SURF_MULTISIG_TVC);
	}

	public Msig deployWithGiver(Sdk sdk,
	                            int wid,
	                            Credentials keys,
	                            Giver giver,
	                            BigInteger value) throws Sdk.SdkException {
		var params = Map.<String, Object>of(
				"owners", new String[]{"0x" + keys.publicKey()},
				"reqConfirms", 1);
		var contract = super.deployWithGiver(sdk, giver, value, wid, null, keys, params);
		return new Msig(contract.sdk(), contract.address(), contract.tvmKey(), contract.abi());
	}

}
