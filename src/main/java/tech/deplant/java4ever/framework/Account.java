package tech.deplant.java4ever.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.abi.ContractAbi;

import java.util.*;

public record Account(String id, int acc_type, String balance, String boc,
                      long last_paid) {

	private static Logger log = LoggerFactory.getLogger(Account.class);

	public static Account ofAddress(Sdk sdk, Address address) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new GraphQLFilter.In(new String[]{address.makeAddrStd()}));
		Net.ResultOfQueryCollection result = Net.queryCollection(sdk.context(),
		                                                         "accounts",
		                                                         filter,
		                                                         "id acc_type balance boc last_paid",
		                                                         null,
		                                                         null);
		if (result.result().length > 0) {
			return sdk.mapper().convertValue(result.result()[0], Account.class);
		} else {
			return new Account(address.makeAddrStd(), 0, "0", null, 0);
		}
	}

	public static List<Account> ofAddressList(Sdk sdk,
	                                          ContractAbi abi,
	                                          Address... addresses) throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id",
		           new GraphQLFilter.In(Arrays.stream(addresses)
		                                      .map(Address::makeAddrStd)
		                                      .toArray(String[]::new)));
		return Arrays
				.stream(Net.queryCollection(sdk.context(),
				                            "accounts",
				                            filter,
				                            "id acc_type balance boc last_paid",
				                            null,
				                            null).result())
				.map(obj -> sdk.mapper().convertValue(obj, Account.class))
				.toList();
	}

	public Map<String, Object> runLocally(Sdk sdk, String message, ContractAbi abi) throws EverSdkException {
		return Optional.ofNullable(Tvm.runTvm(
				                              sdk.context(),
				                              message,
				                              boc(),
				                              null,
				                              abi.ABI(),
				                              null,
				                              false)
		                              .decoded()
		                              .output()).orElse(new HashMap<>());
	}

	public Boolean isActive() {
		return 1 == this.acc_type;
	}

	public static interface GraphQLFilter {
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
