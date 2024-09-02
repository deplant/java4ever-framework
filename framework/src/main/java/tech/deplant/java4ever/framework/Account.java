package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNullElse;

/**
 * The type Account.
 */
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
	 * @param sdk           that will be used for query
	 * @param addressString address of account
	 * @return GraphQL result with fields (id acc_type balance boc data data_hash code code_hash init_code_hash last_paid) as Account object
	 * @throws EverSdkException the ever sdk exception
	 */
	public static Account ofAddress(int sdk, String addressString) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new GraphQLFilter.In(new String[]{addressString}));
		Net.ResultOfQueryCollection result = EverSdk.await(Net.queryCollection(sdk,
		                                                         "accounts",
		                                                         JsonContext.ABI_JSON_MAPPER().valueToTree(filter),
		                                                         "id acc_type balance boc data data_hash code code_hash init_code_hash last_trans_lt",
		                                                         null,
		                                                         null));
		if (result.result().length > 0) {
			try {
				return JsonContext.SDK_JSON_MAPPER().readValue(result.result()[0].traverse(),Account.class);
			} catch (IOException e) {
				logger.log(System.Logger.Level.ERROR, e);
				return ofUninit(addressString);
			}
		} else {
			return ofUninit(addressString);
		}
	}

	/**
	 * Of address account.
	 *
	 * @param sdk     the sdk
	 * @param address the address
	 * @return the account
	 * @throws EverSdkException the ever sdk exception
	 */
	public static Account ofAddress(int sdk, Address address) throws EverSdkException {
		return ofAddress(sdk, address.makeAddrStd());
	}

	/**
	 * Of uninit account.
	 *
	 * @param address the address
	 * @return the account
	 */
	public static Account ofUninit(Address address) {
		return ofUninit(address.makeAddrStd());
	}

	/**
	 * Of uninit account.
	 *
	 * @param addressString the address string
	 * @return the account
	 */
	public static Account ofUninit(String addressString) {
		return new Account(addressString, 0, "0x00", TvmCell.EMPTY.cellBoc(), TvmCell.EMPTY.cellBoc(), "0x00", TvmCell.EMPTY.cellBoc(), "0x00", "0x00", "0x00");
	}

	/**
	 * Does the same as ofAddress() factory, but uses an array of addresses as "in" filter for GraphQL query
	 *
	 * @param sdk       that will be used for query
	 * @param addresses vararg/array of account addresses to check
	 * @return list of created Account objects
	 * @throws EverSdkException the ever sdk exception
	 */
	public static List<Account> ofAddressList(int sdk,
	                                          String... addresses) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id",
		           new GraphQLFilter.In(addresses));
		return Arrays
				.stream(EverSdk.await(Net.queryCollection(sdk,
				                            "accounts",
				                            JsonContext.ABI_JSON_MAPPER().valueToTree(filter),
				                            "id acc_type balance boc data data_hash code code_hash init_code_hash last_trans_lt",
				                            null,
				                            null)).result())
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

//	/**
//	 * Method to extract pubkey from data field of account query.
//	 *
//	 * @param contextId that will be used for extraction
//	 * @param abi of contract (needed to correctly find place of pubkey in the account data)
//	 * @return pubkey string
//	 * @throws EverSdkException
//	 */
//	public String tvmPubkey(int contextId, ContractAbi abi) throws EverSdkException {
//		return switch (abi.abiContract().version()) {
//			case "2.4" -> throw new UnsupportedOperationException("ABI 2.4 doesn't contain separate pubkey field!");
//			default -> Abi.decodeInitialData(contextId, abi.ABI(), data(), false).initialPubkey();
//		};
//	}

	/**
	 * The interface Graph ql filter.
	 */
	public interface GraphQLFilter {
		/**
		 * The type In.
		 */
		record In(String[] in) implements GraphQLFilter {
		}

		/**
		 * The type Eq.
		 */
		record Eq(Integer eq) implements GraphQLFilter {
		}

		/**
		 * The type Gt.
		 */
		record Gt(String gt) implements GraphQLFilter {
		}

		/**
		 * The type Lt.
		 */
		record Lt(String lt) implements GraphQLFilter {
		}
	}
}
