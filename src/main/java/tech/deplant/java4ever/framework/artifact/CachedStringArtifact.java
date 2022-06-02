package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;

public record CachedStringArtifact(IStringArtifact stringReader, String content) implements IStringArtifact {

    public CachedStringArtifact(IStringArtifact stringReader) {
        this(stringReader, stringReader.readString());
    }

    @Override
    public void writeString(String content) throws IOException {
        this.stringReader.writeString(this.content);
    }

    @Override
    public String readString() {
        return this.content;
    }
}

