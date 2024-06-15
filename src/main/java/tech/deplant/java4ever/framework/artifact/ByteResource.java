package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents byte-organized artifacts on Java resource path
 */
public record ByteResource(String resourceName) implements Artifact<byte[],byte[]> {
    @Override
    public byte[] get() {
        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName())) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(byte[] bytes) {
        try {
            Files.write(Paths.get(Thread.currentThread().getContextClassLoader().getResource(resourceName()).getPath()),
                    bytes,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
