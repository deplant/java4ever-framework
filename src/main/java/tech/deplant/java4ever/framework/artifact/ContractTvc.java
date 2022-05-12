package tech.deplant.java4ever.framework.artifact;

import lombok.AllArgsConstructor;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;


@AllArgsConstructor
public record ContractTvc(String tvcString) implements ITvc {

    //public static ContractTvc SAFE_MULTISIG = ContractTvc.of(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc"));

    public static ContractTvc of(Artifact artifact) {
        return new ContractTvc(artifact.getAsBase64String());
    }

    @Override
    public String code(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Boc.getCodeFromTvc(sdk.context(), this.tvcString)).code();
    }

}
