package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigWalletContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.template.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
	public void contract_variations_hashes() throws JsonProcessingException, EverSdkException {
		// multisig 1
		assertEquals("80d6c47c4a25543c9b397b71716f3fae1e2c5d247174c52e2c19bd896442b105",
		             SafeMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		assertEquals("e2b60b6b602c10ced7ea8ede4bdf96342c97570a3798066f3fb50a4b2b27a208",
		             SetcodeMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		assertEquals("207dc560c5956de1a2c1479356f8f3ee70a59767db2bf4788b1d61ad42cdad82",
		             SurfMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		// multisig 1 with modern compiler
		assertEquals("1974b06efa89ba22d1962d06efaef6d00751b7cdc3156c151bb0cc1c504e7e8c",
		             SafeMultisigSolc064WalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		assertEquals("ad57a772ca0c56462e07a086e447abbb7605bd6ac1424cc4178dc9f4730093ff",
		             SetcodeMultisigSolc063WalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		// multisig 2
		assertEquals("7377910a1b5d0c8073ba02523e139c7f42f9772fe0076a4d0b211ccec071eb7a",
		             SafeMultisigWallet2Template.DEFAULT_TVC().codeHash(SDK_EMPTY));
		assertEquals("d66d198766abdbe1253f3415826c946c371f5112552408625aeb0b31e0ef2df3",
		             SetcodeMultisigWallet2Template.DEFAULT_TVC().codeHash(SDK_EMPTY));
		// multisig 24
		assertEquals("7d0996943406f7d62a4ff291b1228bf06ebd3e048b58436c5b70fb77ff8b4bf2",
		             Safe24MultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		assertEquals("a491804ca55dd5b28cffdff48cb34142930999621a54acee6be83c342051d884",
		             Setcode24MultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
		// specials
		assertEquals("f3a07ae84fc343259d7fa4847b86335b3fdcfc8b31f1ba4b7a9499d5530f0b18",
		             BridgeMultisigWalletTemplate.DEFAULT_TVC().codeHash(SDK_EMPTY));
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


	@Disabled
	@Test
	public void check_devnet_giver_contract() throws JsonProcessingException, EverSdkException {
		//var contextId = SDK_LOCAL;
		var keys = new Credentials(
				"a08ba000d026068f90541f111e2c700a796222d8ab4bb4ae8b4e680f64f875da",
				"17011c9157f3cf9e3c75ce8778be6b1adc42cd7abc1aebc0d288d2c338d2d93b");
		var giverContract = new SafeMultisigWalletContract(SDK_DEV, "0:b238570f9ebe536885b6060c7c9d74a20704e5efa844b17afcf814c7b9ddcfee", keys);

		System.out.println(giverContract.sendTransaction(new Address("0:b238570f9ebe536885b6060c7c9d74a20704e5efa844b17afcf814c7b9ddcfee"),EVER_ONE,true).call());
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
	public void readmeSnippet01() throws JsonProcessingException, EverSdkException, InterruptedException {
		// creates random pair of keys
		var keys = Credentials.ofRandom(SDK_LOCAL);
		// use it to deploy a new contract
		var contract = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK_LOCAL,0, keys, new BigInteger[]{keys.publicKeyBigInt()}, 1)
				.deployWithGiver(EverOSGiver.V3(SDK_LOCAL), EVER_ONE);
		// get the contract info
		assertTrue(contract.account().isActive());
		var custodians = contract.getCustodians().getAsMap().get("custodians");
		var custodians2 = contract.getCustodians().get().custodians();
		System.out.println(JsonContext.ABI_JSON_MAPPER().writeValueAsString(custodians));
		System.out.println(JsonContext.ABI_JSON_MAPPER().writeValueAsString(custodians2));
		assertEquals("0x"+keys.publicKey(),Uint.of(256,custodians2[0].get("pubkey")).toString());
	}



}
