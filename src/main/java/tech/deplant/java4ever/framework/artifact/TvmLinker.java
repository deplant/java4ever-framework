package tech.deplant.java4ever.framework.artifact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public record TvmLinker(String linkerPath, String stdlibSolPath) {

	private static System.Logger logger = System.getLogger(TvmLinker.class.getName());

	public TvmLinker {
		if (
				(Files.exists(Path.of(linkerPath)) || Files.exists(Path.of(linkerPath + ".exe")))
				&&
				Files.exists(Path.of(stdlibSolPath))
		) {
		} else {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Linker executable not found at specified path!");
			throw new RuntimeException();
		}
	}

	public int assemblyContract(String contractName, String buildFolder) {
		return assemblyContract(
				buildFolder + "/" + contractName + ".code",
				buildFolder + "/" + contractName + ".abi.json",
				buildFolder + "/" + contractName + ".tvc"
		);
	}

	public int assemblyContract(String codePath, String abiPath, String outputPath) {
		try {
			logger.log(System.Logger.Level.INFO, () -> "Begging assembly of TVM Assembly source...");
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
			return p.onExit().get(30, TimeUnit.SECONDS).exitValue();
		} catch (IOException e) {
			logger.log(System.Logger.Level.ERROR, () -> e.getMessage());
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

