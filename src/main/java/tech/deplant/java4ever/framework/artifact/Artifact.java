package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;

public interface Artifact {

    public byte[] getAsBytes();

    public String getAsString();

    public String getAsJsonString();

    public String getAsBase64String();

    public void saveString(String content) throws IOException;
}
