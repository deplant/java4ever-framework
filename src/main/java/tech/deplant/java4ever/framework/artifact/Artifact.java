package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;

public interface Artifact <T> {
    public void write(T content) throws IOException;
    public T read();
    public void persist() throws IOException;
}
