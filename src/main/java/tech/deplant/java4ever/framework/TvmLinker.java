package tech.deplant.java4ever.framework;

import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Value
public class TvmLinker {


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

