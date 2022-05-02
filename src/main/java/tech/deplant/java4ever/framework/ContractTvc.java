package tech.deplant.java4ever.framework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.deplant.java4ever.binding.Boc;


@AllArgsConstructor
public class ContractTvc {

    public static ContractTvc SAFE_MULTISIG = ofBundled("SafeMultisigWallet.tvc");

    /**
     * TVC, encoded as base64 string.
     */
    @Getter
    String tvcString;

    public static ContractTvc ofStored(String path) {
        return new ContractTvc(FileData.fileToBase64String(path));
    }

    public static ContractTvc ofBundled(String fileName) {
        return ofStored(FileData.storedContractPath(fileName));
    }


    public String code(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Boc.getCodeFromTvc(sdk.context(), this.tvcString)).code();
    }

}
