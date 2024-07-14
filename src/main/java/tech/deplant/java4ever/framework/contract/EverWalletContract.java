package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;

public class EverWalletContract extends GiverContract {

	public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
		return ContractAbi.ofString(
				"{\n" + "  \"ABI version\": 2,\n" + "  \"version\": \"2.3\",\n" + "  \"header\": [\n" +
				"    \"pubkey\",\n" + "    \"time\",\n" + "    \"expire\"\n" + "  ],\n" + "  \"functions\": [\n" +
				"    {\n" + "      \"name\": \"sendTransaction\",\n" + "      \"inputs\": [\n" + "        {\n" +
				"          \"name\": \"dest\",\n" + "          \"type\": \"address\"\n" + "        },\n" +
				"        {\n" + "          \"name\": \"value\",\n" + "          \"type\": \"uint128\"\n" +
				"        },\n" + "        {\n" + "          \"name\": \"bounce\",\n" +
				"          \"type\": \"bool\"\n" + "        },\n" + "        {\n" + "          \"name\": \"flags\",\n" +
				"          \"type\": \"uint8\"\n" + "        },\n" + "        {\n" +
				"          \"name\": \"payload\",\n" + "          \"type\": \"cell\"\n" + "        }\n" + "      ],\n" +
				"      \"outputs\": []\n" + "    },\n" + "    {\n" + "      \"name\": \"sendTransactionRaw\",\n" +
				"      \"inputs\": [\n" + "        {\n" + "          \"name\": \"flags\",\n" +
				"          \"type\": \"uint8\"\n" + "        },\n" + "        {\n" +
				"          \"name\": \"message\",\n" + "          \"type\": \"cell\"\n" + "        }\n" + "      ],\n" +
				"      \"outputs\": []\n" + "    }\n" + "  ],\n" + "  \"data\": [],\n" + "  \"events\": [],\n" +
				"  \"fields\": [\n" + "    {\n" + "      \"name\": \"_pubkey\",\n" + "      \"type\": \"uint256\"\n" +
				"    },\n" + "    {\n" + "      \"name\": \"_timestamp\",\n" + "      \"type\": \"uint64\"\n" +
				"    }\n" + "  ]\n" + "}");
	}

	/**
	 * Instantiates a new EVER WAllet contract.
	 *
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param credentials the credentials
	 */
	public EverWalletContract(int sdk, Address address, Credentials credentials) throws JsonProcessingException {
		super(sdk, String.valueOf(address), DEFAULT_ABI(), credentials);
	}

	@Override
	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		Map<String, Object> inputMap = Map.of("dest",
		                                      dest,
		                                      "value",
		                                      value,
		                                      // amount in nano EVER
		                                      "bounce",
		                                      bounce,
		                                      "flags",
		                                      3,
		                                      "payload",
		                                      TvmCell.EMPTY.cellBoc());
		return new FunctionHandle<Void>(Void.class,
		                                contextId(),
		                                address(),
		                                abi(),
		                                credentials(),
		                                "sendTransaction",
		                                inputMap,
		                                null);
	}
}
