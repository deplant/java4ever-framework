package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.ContractTvc;

public interface Artifact {

    public byte[] getAsBytes();

    public String getAsString();

    public String getAsJsonString();

    public String getAsBase64String();

    public ContractAbi getAsABI();

    public ContractTvc getAsTVC();
}
