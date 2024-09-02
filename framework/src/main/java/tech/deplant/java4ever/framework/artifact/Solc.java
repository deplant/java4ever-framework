package tech.deplant.java4ever.framework.artifact;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Wrapper class around solc compiler library (for compiling TMV-Solidity files)
 */
public record Solc(String compilerPath) {

	private static final String DEFAULT_SOL_EXTENSION = ".sol";
	private static System.Logger logger = System.getLogger(Solc.class.getName());

	/**
	 * Instantiates a new Solc compiler.
	 *
	 * @param compilerPath path to the compiler solc (or solc.exe) file
	 */
	public Solc {
		if (Files.exists(Path.of(compilerPath)) || Files.exists(Path.of(compilerPath + ".exe"))) {
		} else {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Compiler executable not found at specified path!");
			throw new RuntimeException();
		}
	}


	/**
	 * Compile contract int.
	 *
	 * @param contractName   the contract name
	 * @param sourceFileName the source file name
	 * @param sourceFolder   the source folder
	 * @param outputFolder   the output folder
	 * @param libsFolder     the libs folder
	 * @return the int
	 */
	public int compileContract(String contractName, String sourceFileName, String sourceFolder, String outputFolder, String libsFolder) {

		File sourceFolderFile = null;
		if (Files.isDirectory(Path.of(sourceFolder))) {
			sourceFolderFile = new File(sourceFolder);
		} else {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Source path is not a folder!");
		}
		if (!Files.isDirectory(Path.of(outputFolder))) {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Output path is not a folder!");
		}
		if (!Files.exists(Path.of(sourceFolder + "/" + sourceFileName))) {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! No such contract file in source folder!");
		}
		try {
			logger.log(System.Logger.Level.INFO, () -> "Begging compilation of Solidity source...");
			Process p = new ProcessBuilder()
					.inheritIO()
					.directory(sourceFolderFile)
					.command(
							this.compilerPath,
							"--contract",
							contractName,
							"--base-path",
							sourceFolder,
//							"--include-path",
//							libsFolder,
							"--output-dir",
							outputFolder,
							"--tvm-version",
							"ever",
							sourceFileName
					)
					.start();
			//return outputFolder + "/" + contractName + ".code";
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

	/**
	 * Compile contract int.
	 *
	 * @param contractName   the contract name
	 * @param sourceFileName the source file name
	 * @param sourceFolder   the source folder
	 * @param outputFolder   the output folder
	 * @return the int
	 */
	public int compileContract(String contractName, String sourceFileName, String sourceFolder, String outputFolder) {

		File sourceFolderFile = null;
		if (Files.isDirectory(Path.of(sourceFolder))) {
			sourceFolderFile = new File(sourceFolder);
		} else {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Source path is not a folder!");
		}
		if (!Files.isDirectory(Path.of(outputFolder))) {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! Output path is not a folder!");
		}
		if (!Files.exists(Path.of(sourceFolder + "/" + sourceFileName))) {
			logger.log(System.Logger.Level.ERROR, () -> "ERROR! No such contract file in source folder!");
		}
		try {
			logger.log(System.Logger.Level.INFO, () -> "Begging compilation of Solidity source...");
			Process p = new ProcessBuilder()
					.inheritIO()
					.directory(sourceFolderFile)
					.command(
							this.compilerPath,
							sourceFileName,
							"--base-path",
							sourceFolder,
							"--include-path",
							sourceFolder,
							"--output-dir",
							outputFolder,
							"-c",
							contractName
					)
					.start();
			//return outputFolder + "/" + contractName + ".code";
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
