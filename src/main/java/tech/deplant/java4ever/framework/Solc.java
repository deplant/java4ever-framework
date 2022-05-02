package tech.deplant.java4ever.framework;

import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Value
public class Solc {

    private final String DEFAULT_SOL_EXTENSION = ".sol";
    String compilerPath;

    public Solc(String path) {
        if (Files.exists(Path.of(path)) || Files.exists(Path.of(path + ".exe"))) {
            this.compilerPath = path;
        } else {
            log.error("ERROR! Compiler executable not found at specified path!");
            throw new RuntimeException();
        }
    }

    public void compileContract(String contractName, String sourceFolder, String outputFolder) {
        compileContract(contractName, contractName + DEFAULT_SOL_EXTENSION, sourceFolder, outputFolder);
    }

    public CompletableFuture<Process> compileContract(String contractName, String sourceFileName, String sourceFolder, String outputFolder) {

        File sourceFolderFile = null;
        if (Files.isDirectory(Path.of(sourceFolder))) {
            sourceFolderFile = new File(sourceFolder);
        } else {
            log.error("ERROR! Source path is not a folder!");
        }
        if (!Files.isDirectory(Path.of(outputFolder))) {
            log.error("ERROR! Output path is not a folder!");
        }
        if (!Files.exists(Path.of(sourceFolder + "/" + sourceFileName))) {
            log.error("ERROR! No such contract file in source folder!");
        }
        try {
            log.info("Begging compilation of Solidity source...");
            Process p = new ProcessBuilder()
                    .inheritIO()
                    .directory(sourceFolderFile)
                    .command(
                            this.compilerPath,
                            sourceFileName,
                            "--output-dir",
                            outputFolder,
                            "-c",
                            contractName
                    )
                    .start();
            //return outputFolder + "/" + contractName + ".code";
            return p.onExit();
        } catch (IOException e) {
            log.error(e.getMessage());
            return CompletableFuture.failedFuture(new RuntimeException(e.getMessage()));
        }
    }


}
