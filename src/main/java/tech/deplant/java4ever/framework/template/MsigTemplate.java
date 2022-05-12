package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.framework.Address;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Data;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ContractAbi;
import tech.deplant.java4ever.framework.artifact.ContractTvc;
import tech.deplant.java4ever.framework.artifact.FileArtifact;
import tech.deplant.java4ever.framework.contract.Account;
import tech.deplant.java4ever.framework.contract.Giver;
import tech.deplant.java4ever.framework.contract.Msig;

import java.util.Map;

public class MsigTemplate extends ContractTemplate {
    public MsigTemplate(Sdk sdk) {
        super(ContractAbi.ofArtifact(sdk, FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.abi.json")),
                ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc")));
    }

    public Msig deploy(Credentials keys, Giver giver, Sdk sdk) throws Sdk.SdkException {
        var template = new MsigTemplate(sdk);
        var address = Address.ofFutureDeploy(sdk, template, 0, null, keys);
        giver.give(address, Data.EVER);
        Map<String, Object> params = Map.of(
                "owners", new String[]{"0x" + keys.publicKey()},
                "reqConfirms", 1);
        Map<String, Object> msg = template.deploy(sdk, 0, null, keys, params);
        return new Msig(Account.ofAddress(sdk, address, template.abi()), template);
    }

}
