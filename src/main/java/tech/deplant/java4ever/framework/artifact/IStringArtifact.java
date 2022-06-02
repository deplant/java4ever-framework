package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;

public interface IStringArtifact {
    public void writeString(String content) throws IOException;
    public String readString();
}
