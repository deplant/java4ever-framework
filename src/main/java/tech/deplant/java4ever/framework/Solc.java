package tech.deplant.java4ever.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public record Solc(String compilerPath) {

    private static final Logger log = LoggerFactory.getLogger(Solc.class);

    private static final String DEFAULT_SOL_EXTENSION = ".sol";

    public Solc {
        if (Files.exists(Path.of(compilerPath)) || Files.exists(Path.of(compilerPath + ".exe"))) {
        } else {
            log.error("ERROR! Compiler executable not found at specified path!");
            throw new RuntimeException();
        }
    }

    public int compileContract(String contractName, String sourceFileName, String sourceFolder, String outputFolder) {

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
            return p.onExit().get(30, TimeUnit.SECONDS).exitValue();
        } catch (IOException e) {
            log.error(e.getMessage());
            return -1;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }


}
