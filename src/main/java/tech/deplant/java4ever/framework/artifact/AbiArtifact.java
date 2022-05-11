package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Abi;

public record AbiArtifact(Artifact artifact) implements Artifact {

    @Override
    public byte[] getAsBytes() {
        return this.artifact.getAsBytes();
    }

    @Override
    public String getAsString() {
        return this.artifact.getAsString();
    }

    @Override
    public String getAsJsonString() {
        return this.artifact.getAsJsonString();
    }

    @Override
    public String getAsBase64String() {
        return this.artifact.getAsBase64String();
    }

    public Abi.ABI ABI() {
        return new Abi.ABI.Json(getAsJsonString());
    }

}
