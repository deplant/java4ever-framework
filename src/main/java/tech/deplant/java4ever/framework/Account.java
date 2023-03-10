package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.Tvm;

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
                      long last_paid) {

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
		                                                         filter,
		                                                         "id acc_type balance boc data data_hash code code_hash init_code_hash last_paid",
		                                                         null,
		                                                         null);
		if (result.result().length > 0) {
			return sdk.convertMap(result.result()[0], Account.class);
		} else {
			return new Account(address, 0, "0x00", null, null, null, null, null, null, 0);
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
				                            filter,
				                            "id acc_type balance boc data data_hash code code_hash init_code_hash last_paid",
				                            null,
				                            null).result())
				.map(obj -> sdk.convertMap(obj, Account.class))
				.toList();
	}

	/**
	 * Encodes inputs and run getter method on account's boc then decodes answer.
	 * Important! When you run getters here on Account directly, your local copy of
	 * account's boc can be older than real boc in blockchain.
	 * If you need to run getter on actual boc, you need to use OwnedContract.runGetter() method
	 * that downloads new account before running getter on it.
	 *
	 * @param sdk
	 * @param abi
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @param credentials
	 * @return
	 * @throws EverSdkException
	 */
	public Map<String, Object> runGetter(Sdk sdk,
	                                     ContractAbi abi,
	                                     String functionName,
	                                     Map<String, Object> functionInputs,
	                                     Abi.FunctionHeader functionHeader,
	                                     Credentials credentials) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						sdk.context(),
						abi.ABI(),
						id(),
						null,
						new Abi.CallSet(
								functionName,
								null,
								abi.convertFunctionInputs(functionName, functionInputs)
						),
						requireNonNullElse(credentials, Credentials.NONE).signer(),
						null,
						null
				);
		return Optional.ofNullable(Tvm.runTvm(
				                              sdk.context(),
				                              msg.message(),
				                              boc(),
				                              null,
				                              abi.ABI(),
				                              null,
				                              false).decoded()
		                              .output()).orElse(new HashMap<>());
	}

	/**
	 * Encodes inputs and runs callExternal method on account's boc then decodes answer.
	 * Important! When you run callExternal locally, directly on Account boc,
	 * your blockchain real info remains unchanged.
	 * If you need to make a call to actual boc, you need to use OwnedContract.callExternal() method
	 * that sends transaction to blockchain.
	 *
	 * @param sdk
	 * @param abi
	 * @param functionName
	 * @param functionInputs
	 * @param functionHeader
	 * @param credentials
	 * @return
	 * @throws EverSdkException
	 */
	public Tvm.ResultOfRunExecutor runLocal(Sdk sdk,
	                                        ContractAbi abi,
	                                        String functionName,
	                                        Map<String, Object> functionInputs,
	                                        Abi.FunctionHeader functionHeader,
	                                        Credentials credentials,
	                                        Tvm.ExecutionOptions options) throws EverSdkException {
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						sdk.context(),
						abi.ABI(),
						id(),
						null,
						new Abi.CallSet(
								functionName,
								null,
								abi.convertFunctionInputs(functionName, functionInputs)
						),
						requireNonNullElse(credentials, Credentials.NONE).signer(),
						null,
						null
				);
		return Tvm.runExecutor(sdk.context(),
		                       msg.message(),
		                       new Tvm.AccountForExecutor.Account(boc(), true),
		                       options,
		                       abi.ABI(),
		                       false,
		                       null,
		                       true);
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
