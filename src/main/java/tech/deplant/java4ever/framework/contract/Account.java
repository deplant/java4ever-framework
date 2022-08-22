package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.GraphQLFilter;
import tech.deplant.java4ever.framework.JSONContext;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.type.Address;

import java.util.HashMap;
import java.util.Map;

public record Account(String id, int acc_type, String balance, String boc,
                      long last_paid) {

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

}
