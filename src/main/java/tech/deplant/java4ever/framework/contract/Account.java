package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.GraphQLFilter;
import tech.deplant.java4ever.framework.JSONContext;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.type.Address;

import java.util.HashMap;
import java.util.Map;

public record Account(String id, int acc_type, String balance, String boc,
                      long last_paid) {

    private static Logger log = LoggerFactory.getLogger(Account.class);

    protected static Account graphQLRequest(Sdk sdk, Address address) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQLFilter.In(new String[]{address.makeAddrStd()}));
        Net.ResultOfQueryCollection result = Net.queryCollection(sdk.context(),
                "accounts",
                filter,
                "id acc_type balance boc last_paid",
                null,
                null);
        try {
            return JSONContext.MAPPER.readValue(result.result()[0].toString(), Account.class);
        } catch (JsonProcessingException e) {
            log.error("JSON Parsing error! " + e.getMessage());
            return null;
        }
    }

    public Boolean isActive() {
        return 1 == this.acc_type;
    }

    //    public static Collection<Account> ofAddressList(Sdk sdk, Iterable<Address> addresses, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(addresses.stream().map(tech.deplant.java4ever.framework.type.Address::makeAddrStd).toArray(String[]::new)));
//        return Arrays
//                .stream(sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result())
//                .map(
//                        obj -> {
//                            var collection = new Gson().fromJson(new Gson().toJson(obj), Account.AccountQueryCollection.class);
//                            return new Account(sdk, new Address(collection.id()), abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//                        }
//                ).toList();
//    }

}
