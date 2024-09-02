package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class MultisigContractTests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
	}

	@Test
	public void use_giver_as_abstract_contract() throws JsonProcessingException, EverSdkException {
		var deployedContractAbi = ContractAbi.ofResource("artifacts/giver/GiverV2.abi.json");
		var deployedContractAddress = new Address("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
		var deployedContractCredentials = new Credentials("2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16",
		                                                  "172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3");
		var contextId = Env.SDK_LOCAL;
		var giverContract = new AbstractContract(contextId,
		                                         deployedContractAddress,
		                                         deployedContractAbi,
		                                         deployedContractCredentials);
		var functionCallPrepare = giverContract.functionCallBuilder()
		             .setFunctionName("getMessages")
				.setFunctionInputs(Map.of())
				.setReturnClass(Map.class)
				.build();
		System.out.println(functionCallPrepare.getAsMap().toPrettyString());
	}

	/**
	 * Checks if Multisig builder creates same addresses as regular prepareDeploy call
	 */
	@Test
	public void address_from_template_equals_address_from_builder() throws JsonProcessingException, EverSdkException {
		var keys = Env.RNG_KEYS();

		var addressTemplate = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicKeyBigInt()},
		                                                                     1).toAddress();
		var addressBuilder = new MultisigBuilder().setType(MultisigContract.Type.SAFE).prepare(SDK_LOCAL, keys).toAddress();

		assertNotEquals(addressTemplate.makeAddrStd(), Address.ZERO.makeAddrStd());
		assertEquals(addressTemplate.makeAddrStd(),addressBuilder.makeAddrStd());
	}

	@Test
	public void readmeSnippet01() throws JsonProcessingException, EverSdkException {
		// initialize EVER-SDK library
		EverSdk.load();
		// create config context, save its id
		int contextId = EverSdk.createWithEndpoint("https://nodese.truequery.tech/graphql");
		// creates random pair of keys
		var keys = Credentials.ofRandom(contextId);
		// use it to deploy a new contract
		var contract = new SafeMultisigWalletTemplate()
				.prepareDeploy(contextId,0, keys, new BigInteger[]{keys.publicKeyBigInt()}, 1)
				.deployWithGiver(EverOSGiver.V2(contextId), EVER_ONE);
		// get the contract info
		System.out.println(contract.account().id() + " is active: " + contract.account().isActive());
	}



}
