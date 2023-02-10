package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.generator.ContractWrapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class WrapperGenerationTests {

	private static System.Logger logger = System.getLogger(CredentialsTests.class.getName());

	@Test
	public void generate() throws IOException, EverSdkException {

		Path targetDirectory = Paths.get("src/gen/java");
		String contractPackage = "tech.deplant.java4ever.framework.contract";
		String templatePackage = "tech.deplant.java4ever.framework.template";

		// Givers

		ContractWrapper.generate("artifacts/giver/GiverV2.abi.json",
		                         targetDirectory,
		                         "GiverV2",
		                         contractPackage,
		                         templatePackage);

		// Multisigs

		ContractWrapper.generate("artifacts/multisig/SafeMultisigWallet.abi.json",
		                         targetDirectory,
		                         "SafeMultisigWallet",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/multisig/SetcodeMultisigWallet.abi.json",
		                         targetDirectory,
		                         "SetcodeMultisigWallet",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/multisig/SurfMultisigWallet.abi.json",
		                         targetDirectory,
		                         "SurfMultisigWallet",
		                         contractPackage,
		                         templatePackage);

		// TIP 3

		ContractWrapper.generate("artifacts/tip31/TokenRoot.abi.json",
		                         targetDirectory,
		                         "TIP3TokenRoot",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/tip31/TokenWallet.abi.json",
		                         targetDirectory,
		                         "TIP3TokenWallet",
		                         contractPackage,
		                         templatePackage);


		// TIP 4

		ContractWrapper.generate("artifacts/tip4/Collection.abi.json",
		                         targetDirectory,
		                         "TIP4Collection",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/tip4/Index.abi.json",
		                         targetDirectory,
		                         "TIP4Index",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/tip4/IndexBasis.abi.json",
		                         targetDirectory,
		                         "TIP4IndexBasis",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/tip4/Nft.abi.json",
		                         targetDirectory,
		                         "TIP4Nft",
		                         contractPackage,
		                         templatePackage);

		ContractWrapper.generate("artifacts/tip4/Wallet.abi.json",
		                         targetDirectory,
		                         "TIP4Wallet",
		                         contractPackage,
		                         templatePackage);

	}

//	@Test
//	public void test_generated() throws EverSdkException, JsonProcessingException {
//		var template = new SafeMultisigWalletTemplate(
//				ContractAbi.ofResource("artifacts/multisig/SafeMultisigWallet.abi.json"),
//				ContractTvc.ofResource("artifacts/multisig/SafeMultisigWallet.tvc")
//		);
//		var deployment = template.deploy(null,
//		                                 Credentials.NONE,
//		                                 null,
//		                                 0);
//		var multisig = deployment.deploy();
//		var functionCall = multisig.getParameters();
//		var resultOfCall = functionCall.call();
//	}

}
