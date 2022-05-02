package tech.deplant.java4ever.framework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.artifact.FileArtifact;


@AllArgsConstructor
public class ContractTvc {

    public static ContractTvc SAFE_MULTISIG = ContractTvc.ofStored(FileArtifact.ofResourcePath("/artifacts/std/SafeMultisigWallet.tvc"));

    /**
     * TVC, encoded as base64 string.
     */
    @Getter
    String tvcString;

    public static ContractTvc ofStored(Artifact artifact) {
        return artifact.getAsTVC();
    }


    public String code(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Boc.getCodeFromTvc(sdk.context(), this.tvcString)).code();
    }

}
