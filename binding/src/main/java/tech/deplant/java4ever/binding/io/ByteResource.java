package tech.deplant.java4ever.binding.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Helper class for working with byte-organized project resources.
 */
public record ByteResource(String resourceName) implements Supplier<byte[]>, Consumer<byte[]> {
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
