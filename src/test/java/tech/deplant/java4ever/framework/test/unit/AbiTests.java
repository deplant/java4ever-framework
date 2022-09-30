package tech.deplant.java4ever.framework.test.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.template.Tip31RootTemplate;
import tech.deplant.java4ever.framework.template.type.*;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbiTests {

	private static Logger log = LoggerFactory.getLogger(AbiTests.class);

	public Object serializeSingle(AbiValueType type, int size, Object inputValue) {
		return type.name();
	}

	public Object serializeSingle2(AbiValueType type, int size, Object inputValue) {
		return switch (type) {
			case UINT, INT -> switch (inputValue) {
				case BigInteger b -> new AbiUint(b).serialize();
				case Instant i -> new AbiUint(i).serialize();
				case String strPrefixed
						when strPrefixed.length() >= 2 && "0x".equals(strPrefixed.substring(0, 2)) ->
						new AbiUint(new BigInteger(strPrefixed.substring(2))).serialize();
				case String str -> new AbiUint(new BigInteger(str)).serialize();
				default -> inputValue;
			};
			case STRING, BYTES, BYTE -> switch (inputValue) {
				case String s -> new AbiString(s).serialize();
				default -> inputValue.toString();
			};
			case ADDRESS -> switch (inputValue) {
				case Address a -> new AbiAddress(a).serialize();
				case String s -> new AbiAddress(s).serialize();
				default -> inputValue;
			};
			case BOOL -> switch (inputValue) {
				case Boolean b -> b;
				default -> inputValue;
			};
			case CELL, SLICE, BUILDER -> switch (inputValue) {
				case String s -> s;
				case AbiTvmCell abiCell -> abiCell.serialize();
				default -> inputValue;
			};
			case TUPLE -> throw new Sdk.SdkException(new Sdk.Error(101, "Shouldn't get here!"));
		};
	}

	public TypePair typeParser(String typeString) {
		var sizePattern = Pattern.compile("([a-zA-Z]+)(\\d{1,3})");
		var matcher = sizePattern.matcher(typeString);
		while (matcher.find()) {
			log.info("Size found!");
			log.info("Type: " + matcher.group(1));
			log.info("Size: " + matcher.group(2));
			return new TypePair(AbiValueType.valueOf(matcher.group(1).toUpperCase()),
			                    Integer.parseInt(matcher.group(2)));
		}
		return null;
	}

	public boolean arrayMatcher(String typeString) {
		var arrayPattern = Pattern.compile("([a-zA-Z]+\\d{0,3})(\\[\\])");
		var matcher = arrayPattern.matcher(typeString);
		while (matcher.find()) {
			log.info("Type: " + matcher.group(1));
			return true;
		}
		return false;
	}

	public Object serializeTree(Abi.AbiParam param, Object inputValue) {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" +
		                                 typeStringPattern +
		                                 "(,)" +
		                                 typeStringPattern +
		                                 "(\\))");
		boolean rootIsMap = false;
		String rootTypeString = param.type();
		String keyTypeString = null;
		String valueTypeString = null;

		log.info(rootTypeString);
		var matcher = mapPattern.matcher(rootTypeString);
		while (matcher.find()) {
			rootIsMap = true;
			keyTypeString = matcher.group(2);
			valueTypeString = matcher.group(4);
		}

		// map = Map<String,Object> from types
		// map(type,tuple) = Map<String,Map<String,Object>>
		if (rootIsMap) {
			// Key Parse
			final boolean keyIsArray = arrayMatcher(keyTypeString);
			var keyTypePair = typeParser(keyTypeString);
			final AbiValueType keyType = keyTypePair.type();
			final int keySize = keyTypePair.size();
			log.info("MapKey Is Array: " + keyIsArray);
			log.info("MapKey Type: " + keyType.name());
			log.info("MapKey Size: " + keySize);
			// Value Parse
			final boolean valueIsArray = arrayMatcher(valueTypeString);
			var valueTypePair = typeParser(valueTypeString);
			final AbiValueType valueType = valueTypePair.type();
			final int valueSize = valueTypePair.size();
			log.info("MapValue Is Array: " + valueIsArray);
			log.info("MapValue Type: " + valueType.name());
			log.info("MapValue Size: " + valueSize);

			Map<Object, Object> mapValue = (Map<Object, Object>) inputValue;
			if (mapValue.size() == 1) {
				final Object key = mapValue.keySet().toArray()[0];
				final Object value = mapValue.values().toArray()[0];
				return Map.of(serializeSingle(keyType, keySize, key),
				              // serializeTree is used for map(type,tuple) cases,
				              // thus it will continue to serialize tuple part
				              serializeTree(new Abi.AbiParam(valueTypeString, valueTypeString, param.components()),
				                            value));
			} else {
				// for map(type,type) we can only process single key-value pair
				throw new Sdk.SdkException(new Sdk.Error(101, "Wrong argument"));
			}
		} else {
			final boolean rootIsArray = arrayMatcher(rootTypeString);
			var rootTypePair = typeParser(rootTypeString);
			final AbiValueType rootType = rootTypePair.type();
			final int rootSize = rootTypePair.size();
			log.info("Root Is Array: " + rootIsArray);
			log.info("Root Type: " + rootType.name());
			log.info("Root Size: " + rootSize);
			if (rootType.equals(AbiValueType.TUPLE)) {
				// tuple = Map<String,Object> from components
				Map<String, Object> mapValue = (Map<String, Object>) inputValue;
				return Arrays.stream(param.components()).collect(
						Collectors.toMap(component -> component.name(),
						                 component -> serializeTree(component, mapValue.get(component.name()))
						));
			} else if (rootIsArray) {
				return switch (inputValue) {
					case Object[] arr ->
							Arrays.stream(arr).map(element -> serializeSingle(rootType, rootSize, element)).toArray();
					case List list ->
							list.stream().map(element -> serializeSingle(rootType, rootSize, element)).toArray();
					default -> new Object[]{};
				};
			} else {
				// type = Object
				return serializeSingle(rootType, rootSize, inputValue);
			}
		}
	}

	@Test
	public void regexpAbiTypes() throws JsonProcessingException {

		var cachedAbiTip31 = Tip31RootTemplate.DEFAULT_ABI();
		var cachedAbiSafeMsig = MsigTemplate.SAFE_MULTISIG_ABI();
		var uint256ArrayType = cachedAbiSafeMsig.functionInputType("constructor", "owners");
		var tupleArrayType = cachedAbiSafeMsig.functionOutputType("getTransactions", "transactions");
		var uint32Type = cachedAbiTip31.functionInputType("mintDisabled", "answerId");
		var addressType = cachedAbiTip31.functionInputType("burnTokens", "walletOwner");
		var mapAddressTupleType = cachedAbiTip31.functionInputType("transferOwnership", "callbacks");
		List<Abi.AbiParam> types = new ArrayList<>();
		types.add(uint256ArrayType);
		types.add(tupleArrayType);
		types.add(uint32Type);
		types.add(addressType);
		types.add(mapAddressTupleType);

		for (Abi.AbiParam type : types) {
			serializeTree(type, "");
		}

		//		int matches = 0;
		//		while (matcher.find()) {
		//			matches++;
		//		}
		//		return matches;
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
		assertEquals(Sdk.DEFAULT_MAPPER.readTree(jsonStr), Sdk.DEFAULT_MAPPER.readTree(cachedStr));
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

	public record TypePair(AbiValueType type, Integer size) {
	}


}
