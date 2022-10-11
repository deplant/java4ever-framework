package tech.deplant.java4ever.framework.test.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.template.Tip31RootTemplate;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AbiTests {

	private static Logger log = LoggerFactory.getLogger(AbiTests.class);

	@Test
	public void regexpAbiTypes() throws JsonProcessingException, EverSdkException {
		var cachedAbiTip31 = Tip31RootTemplate.DEFAULT_ABI();
		var cachedAbiSafeMsig = MsigTemplate.SAFE_MULTISIG_ABI();
		var uint256ArrayType = cachedAbiSafeMsig.functionInputType("constructor", "owners");
		var tupleArrayType = cachedAbiSafeMsig.functionOutputType("getTransactions", "transactions");
		var uint32Type = cachedAbiTip31.functionInputType("mintDisabled", "answerId");
		var addressType = cachedAbiTip31.functionInputType("burnTokens", "walletOwner");
		var mapAddressTupleType = cachedAbiTip31.functionInputType("transferOwnership", "callbacks");
		var mapAddressArrayType = new Abi.AbiParam("testMapWithArrayKey", "map(address,uint256[])", null);
		var mapAddressNoSizeArrayType = new Abi.AbiParam("testMapWithArrayKey", "map(address,uint[])", null);

		// uint256[]
		var strArr = new String[]{"fa8c63c27b0d7e3dd3a088ef68e0ed549245186ecda3451d88ef34fe98ffd4c3", "d476061baeaac46458c7ca3d678d5225d01de7f9cda3fc964459e6cc9aac0bb8"};
		var str = "fa8c63c27b0d7e3dd3a088ef68e0ed549245186ecda3451d88ef34fe98ffd4c3";
		log.info("Output: " +
		         ContextBuilder.DEFAULT_MAPPER.writeValueAsString(cachedAbiSafeMsig.serializeTree(uint256ArrayType,
		                                                                                          strArr)));
		log.info("Output: " +
		         ContextBuilder.DEFAULT_MAPPER.writeValueAsString(cachedAbiSafeMsig.serializeTree(uint256ArrayType,
		                                                                                          str)));

		// map(address,tuple)
		Map<String, Object> mapInner = Map.of(
				"value", new BigInteger("2"),
				"payload", "");
		Map<Object, Object> mapOuter = Map.of(Address.ZERO, mapInner);
		log.info("Output: " +
		         ContextBuilder.DEFAULT_MAPPER.writeValueAsString(cachedAbiSafeMsig.serializeTree(mapAddressTupleType,
		                                                                                          mapOuter)));

		//map(address,uint256[])
		var arrayInner = new Integer[]{0, 1, 1080};
		var arrayStringInner = "139ee9ed2b6f4ef02a074f8cc5021d44c3a3fa2c42b75c1d17dec6a8281046b6";
		Map<Object, Object> mapOuter2 = Map.of(Address.ZERO, arrayInner);
		log.info("Output: " +
		         ContextBuilder.DEFAULT_MAPPER.writeValueAsString(cachedAbiSafeMsig.serializeTree(mapAddressArrayType,
		                                                                                          mapOuter2)));
		mapOuter2 = Map.of(Address.ZERO, arrayStringInner);
		log.info("Output: " +
		         ContextBuilder.DEFAULT_MAPPER.writeValueAsString(cachedAbiSafeMsig.serializeTree(
				         mapAddressNoSizeArrayType,
				         mapOuter2)));
	}

	/**
	 * Checks that processed & cached ABI representation fully corresponds to initial ABI json.
	 *
	 * @throws JsonProcessingException
	 */
	@Test
	public void testAbiConvert() throws JsonProcessingException {
		var jsonStr = new JsonResource("artifacts/tip31/TokenRoot.abi.json").get();
		var cachedStr = Tip31RootTemplate.DEFAULT_ABI().json();
		assertEquals(ContextBuilder.DEFAULT_MAPPER.readTree(jsonStr),
		             ContextBuilder.DEFAULT_MAPPER.readTree(cachedStr));
	}

	@Test
	public void testAbiMsigSafe() throws JsonProcessingException {
		assertTrue(MsigTemplate.SAFE_MULTISIG_ABI().hasFunction("acceptTransfer"));
	}

	@Test
	public void testAbiMsigSetcode() throws JsonProcessingException {
		assertTrue(MsigTemplate.SETCODE_MULTISIG_ABI().hasFunction("acceptTransfer"));
	}

	@Test
	public void testAbiMsigSurf() throws JsonProcessingException {
		assertTrue(MsigTemplate.SURF_MULTISIG_ABI().hasFunction("acceptTransfer"));
	}


}
