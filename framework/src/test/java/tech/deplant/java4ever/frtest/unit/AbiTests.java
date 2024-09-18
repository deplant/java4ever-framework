package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Boc;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SetcodeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.SurfMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static tech.deplant.java4ever.frtest.unit.Env.INIT;
import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AbiTests {

	private static System.Logger logger = System.getLogger(AbiTests.class.getName());

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		INIT();
	}


	@Test
	public void convertInputs_from_complex_structure() throws JsonProcessingException, EverSdkException {
		var abi = ContractAbi.ofString("""
				                               {
				                               	"ABI version": 2,
				                               	"header": [
				                               		"pubkey",
				                               		"time",
				                               		"expire"
				                               	],
				                               	"functions": [
				                               		{
				                               			"name": "sendAddresses",
				                               			"inputs": [
				                               				{
				                               					"components": [
				                               						{
				                               							"name": "index",
				                               							"type": "uint8"
				                               						},
				                               						{
				                               							"name": "addr",
				                               							"type": "address"
				                               						}
				                               					],
				                               					"name": "addresses",
				                               					"type": "tuple[]"
				                               				}
				                               			],
				                               			"outputs": [
				                               
				                               			]
				                               		}
				                               	]
				                               }
				                               """);

		var tuple1 = new HashMap<String, Object>();
		tuple1.put("index", "0x01");
		tuple1.put("addr", Address.ZERO);
		var tuple2 = new HashMap<String, Object>();
		tuple2.put("index", "0x01");
		tuple2.put("addr", Address.ZERO);
		var input = new HashMap<String, Object>();
		input.put("addresses", new HashMap[]{ tuple1, tuple2 });
		System.out.println(abi.convertFunctionInputs("sendAddresses", input));
	}

	@Test
	public void get_data_from_template() throws JsonProcessingException, EverSdkException {
		var tvc = new SafeMultisigWalletTemplate().tvc();
		var data = tvc.data(SDK_EMPTY);
		System.out.println(data);
		System.out.println(tvc.decodeInitialData(SDK_EMPTY, SafeMultisigWalletTemplate.DEFAULT_ABI()));
	}

	@Test
	public void pubkey_update_gives_correct_address() throws JsonProcessingException, EverSdkException, ExecutionException, InterruptedException {
		var tvc = new SafeMultisigWalletTemplate().tvc();
		assertEquals("6dc5dcb2bbdfe497a8706f6bc52aab8a0bc943b7994978772af723ceb516933f",Boc.getBocHash(SDK_EMPTY, tvc.base64String()).get().hash());
		tvc = tvc.withUpdatedInitialData(SDK_EMPTY,SafeMultisigWalletTemplate.DEFAULT_ABI(),Map.of(),"a08ba000d026068f90541f111e2c700a796222d8ab4bb4ae8b4e680f64f875da");
		assertEquals("b238570f9ebe536885b6060c7c9d74a20704e5efa844b17afcf814c7b9ddcfee",Boc.getBocHash(SDK_EMPTY, tvc.base64String()).get().hash());
		//System.out.println(data);
		//System.out.println(tvc.decodeInitialData(SDK_EMPTY, SafeMultisigWalletTemplate.DEFAULT_ABI()));
	}

	@Test
	public void get_data_with_public_template() throws JsonProcessingException, EverSdkException {
		var stateInit = new SafeMultisigWalletTemplate().tvc().withUpdatedInitialData(SDK_EMPTY, SafeMultisigWalletTemplate.DEFAULT_ABI(),Map.of(),"a08ba000d026068f90541f111e2c700a796222d8ab4bb4ae8b4e680f64f875da");
		//System.out.println(stateInit.);
	}

	@Test
	public void deserialized_abi_equals_original_json() throws JsonProcessingException {
		var jsonStr = new JsonResource("artifacts/tip31/TokenRoot.abi.json").get();
		var cachedStr = TIP3TokenRootTemplate.DEFAULT_ABI().json();
		assertEquals(JsonContext.ABI_JSON_MAPPER().readTree(jsonStr),
		             JsonContext.ABI_JSON_MAPPER().readTree(cachedStr));
	}

	@Test
	public void true_if_abi_has_function() throws JsonProcessingException {
		assertTrue(SafeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
		assertTrue(SetcodeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
		assertTrue(SurfMultisigWalletTemplate.DEFAULT_ABI().hasFunction("acceptTransfer"));
	}

	@Test
	public void false_if_abi_lacks_function() throws JsonProcessingException {
		assertFalse(SafeMultisigWalletTemplate.DEFAULT_ABI().hasFunction("releaseKraken"));
	}

	@Test
	public void conversion_of_bigint_input_should_be_correct() throws JsonProcessingException, EverSdkException {
		Map<String,Object> converted = SafeMultisigWalletTemplate
				.DEFAULT_ABI()
				.convertFunctionInputs("sendTransaction",
				                       Map.of("value", new BigInteger("123")));
		assertEquals("0x000000000000007b", converted.get("value"));
	}

}
