package tech.deplant.java4ever.framework.artifact;

public interface Artifact {

    public byte[] getAsBytes();

    public String getAsString();

    public String getAsJsonString();

    public String getAsBase64String();
}
