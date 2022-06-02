package tech.deplant.java4ever.framework.artifact;

import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.framework.Sdk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;

public record ArtifactTVC(byte[] content, ) implements ITvc, Persistable {

    public static ArtifactTVC ofPath(Path path) {
        return new ArtifactTVC(new CachedByteArtifact(new LocalByteArtifact(path)));
    }

    @Override
    public String base64String() {
        return Base64.getEncoder().encodeToString(this.artifact.read());
    }

    @Override
    public String code(Sdk sdk) throws Sdk.SdkException {
        return sdk.syncCall(Boc.getCodeFromTvc(sdk.context(), base64String())).code();
    }

    @Override
    public void persist() throws IOException {
        this.artifact.write(this.artifact.read());
    }
}
