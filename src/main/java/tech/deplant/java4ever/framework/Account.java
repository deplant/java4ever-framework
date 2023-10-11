package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.*;

import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNullElse;

public record Account(String id,
                      int accType,
                      String balance,
                      String boc,
                      String data,
                      String dataHash,
                      String code,
                      String codeHash,
                      String initCodeHash,
                      String lastTransLt) {

	private static System.Logger logger = System.getLogger(Account.class.getName());

	/**
	 * Factory method to create Account object.
	 * Makes a net.query_collection request to EVER-SDK (accounts collection query), then
	 * constructs Account object from the result. If query doesn't find account,
	 * method will construct empty Account object with accType=0 (not deployed).
	 *
	 * @param sdk     that will be used for query
	 * @param address of account
	 * @return GraphQL result with fields (id acc_type balance boc data data_hash code code_hash init_code_hash last_paid) as Account object
	 * @throws EverSdkException
	 */
	public static Account ofAddress(Sdk sdk, String address) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new GraphQLFilter.In(new String[]{address}));
		Net.ResultOfQueryCollection result = Net.queryCollection(sdk.context(),
		                                                         "accounts",
		                                                         JsonContext.ABI_JSON_MAPPER().valueToTree(filter),
		                                                         "id acc_type balance boc data data_hash code code_hash init_code_hash last_trans_lt",
		                                                         null,
		                                                         null);
		if (result.result().length > 0) {
			try {
				return JsonContext.SDK_JSON_MAPPER().readValue(result.result()[0].traverse(),Account.class);
			} catch (IOException e) {
				logger.log(System.Logger.Level.ERROR, e);
				return new Account(address, 0, "0x00", null, null, null, null, null, null, "0x00");
			}
		} else {
			return new Account(address, 0, "0x00", null, null, null, null, null, null, "0x00");
		}
	}

	/**
	 * Does the same as ofAddress() factory, but uses an array of addresses as "in" filter for GraphQL query
	 *
	 * @param sdk       that will be used for query
	 * @param addresses vararg/array of account addresses to check
	 * @return list of created Account objects
	 * @throws EverSdkException
	 */
	public static List<Account> ofAddressList(Sdk sdk,
	                                          String... addresses) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id",
		           new GraphQLFilter.In(addresses));
		return Arrays
				.stream(Net.queryCollection(sdk.context(),
				                            "accounts",
				                            JsonContext.ABI_JSON_MAPPER().valueToTree(filter),
				                            "id acc_type balance boc data data_hash code code_hash init_code_hash last_trans_lt",
				                            null,
				                            null).result())
				.map(obj -> JsonContext.SDK_JSON_MAPPER().convertValue(obj, Account.class))
				.toList();
	}

	/**
	 * Checks if accType field of account query equals to 1.
	 *
	 * @return true if account is active
	 */
	public Boolean isActive() {
		return 1 == this.accType;
	}

	/**
	 * Method to extract pubkey from data field of account query.
	 *
	 * @param sdk that will be used for extraction
	 * @param abi of contract (needed to correctly find place of pubkey in the account data)
	 * @return pubkey string
	 * @throws EverSdkException
	 */
	public String tvmPubkey(Sdk sdk, ContractAbi abi) throws EverSdkException {
		return Abi.decodeInitialData(sdk.context(), abi.ABI(), data(), false).initialPubkey();
	}

	public interface GraphQLFilter {
		record In(String[] in) implements GraphQLFilter {
		}

		record Eq(Integer eq) implements GraphQLFilter {
		}

		record Gt(String gt) implements GraphQLFilter {
		}

		record Lt(String lt) implements GraphQLFilter {
		}
	}
}
