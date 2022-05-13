package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.artifact.ContractTvc;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;

import java.math.BigInteger;
import java.util.Map;

public class MsigTemplate extends ContractTemplate<Msig> {
    public MsigTemplate() {
        super(ContractAbi.ofArtifact(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json")),
                ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc")));
    }

    public Msig deployWithGiver(Sdk sdk, int wid, Credentials keys, Giver giver, BigInteger value) throws Sdk.SdkException {
        //var template = new MsigTemplate(sdk);
        //var address = Address.ofFutureDeploy(sdk, template, 0, null, keys);
        //giver.give(address, Data.EVER);
        var params = Map.<String, Object>of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        //Map<String, Object> msg = template.deploy(sdk, 0, null, keys, params);
        return deployWithGiver(sdk, giver, value, wid, null, keys, params);
    }

}