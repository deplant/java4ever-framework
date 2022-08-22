package tech.deplant.java4ever.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class TvmLinker {

    private static Logger log = LoggerFactory.getLogger(TvmLinker.class);

    String linkerPath;
    String stdlibSolPath;

    public TvmLinker(String linkerPath, String stdlibSolPath) {
        if (
                (Files.exists(Path.of(linkerPath)) || Files.exists(Path.of(linkerPath + ".exe")))
                        &&
                        Files.exists(Path.of(stdlibSolPath))
        ) {
            this.linkerPath = linkerPath;
            this.stdlibSolPath = stdlibSolPath;
        } else {
            log.error("ERROR! Linker executable not found at specified path!");
            throw new RuntimeException();
        }
    }

    public CompletableFuture<Process> assemblyContract(String contractName, String buildFolder) {
        return assemblyContract(
                buildFolder + "/" + contractName + ".code",
                buildFolder + "/" + contractName + ".abi.json",
                buildFolder + "/" + contractName + ".tvc"
        );
    }

    public CompletableFuture<Process> assemblyContract(String codePath, String abiPath, String outputPath) {
        try {
            log.info("Begging assembly of TVM Assembly source...");
            Process p = new ProcessBuilder()
                    .inheritIO()
                    .command(
                            this.linkerPath,
                            "compile",
                            "-o",
                            outputPath,
                            "--lib",
                            this.stdlibSolPath,
                            "--abi-json",
                            abiPath,
                            codePath
                    )
                    .start();
            return p.onExit();
        } catch (IOException e) {
            log.error(e.getMessage());
            return CompletableFuture.failedFuture(new RuntimeException(e.getMessage()));
        }
    }


}

