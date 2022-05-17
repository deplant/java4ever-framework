package tech.deplant.java4ever.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.GraphQL;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.Msig;
import tech.deplant.java4ever.framework.template.ContractTemplate;
import tech.deplant.java4ever.framework.template.MsigTemplate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Log4j2
public class TestsFeatures {

//    public static Account ofAddress(Sdk sdk, tech.deplant.java4ever.framework.Address address, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
//        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
//        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
//        return new Account(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//    }

    @Test
    public void testBetterComposition() {
        final Sdk sdkDEV = new SdkBuilder()
                .networkEndpoints(SdkBuilder.Network.DEV_NET.endpoints())
                .create(new JavaLibraryPathLoader());
        final Sdk sdkSE = new SdkBuilder()
                .networkEndpoints(new String[]{"http://80.78.254.199/"})
                .timeout(50L)
                .create(new JavaLibraryPathLoader());


        var giver = new Msig(sdkDEV, new Address("0:b238570f9ebe536885b6060c7c9d74a20704e5efa844b17afcf814c7b9ddcfee"), credentialsGiver);
        ContractTemplate<Msig> template = new MsigTemplate();
        Msig msig = template.deployWithGiver(sdkDEV, 0, Credentials.RANDOM(sdkDEV), giver, new BigInteger("2"));
        msig.callExternal("", functionInputs);

        Msig msig2 = new Msig(sdkDEV, address);

    }


    @Test
    public void testJacksonConvert() throws ExecutionException, InterruptedException {
        final Sdk sdk = new SdkBuilder()
                .networkEndpoints(new String[]{"http://80.78.254.199/"})
                .timeout(50L)
                .create(new JavaLibraryPathLoader());

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();

        record AccountQuery(String id, int acc_type, String balance, String boc, long last_paid) {
        }

        Map<String, Object> filter = new HashMap<>();
        filter.put("id", new GraphQL.Filter.In(new String[]{"0:e2a1dcec8bebff29c207d8944aef1bc8a5a9500789096c6a83a3a9bd71dd75fa"}));
        Object[] results = Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null).get().result();
        var query = mapper.convertValue(results[0], AccountQuery.class);

        log.debug(query.id() + ", " + query.balance());
    }

    @Test
    public void testParallelSdkExecution() throws ExecutionException, InterruptedException {


//        final Sdk sdk = new SdkBuilder()
//                .networkEndpoints(new String[]{"http://80.78.254.199/"})
//                .timeout(50L)
//                .create(new JavaLibraryPathLoader());
//
//        Map<String, Object> filter1 = Map.of("id", new GraphQL.Filter.In(new String[]{"0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415"}));
//        Map<String, Object> filter2 = Map.of("id", new GraphQL.Filter.In(new String[]{"0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415"}));
//        Map<String, Object> filterWrong = Map.of("id", new GraphQL.Filter.In(new String[]{"0:2ce57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415"}));
//
//        CompletableFuture<Void> empty = CompletableFuture.runAsync(() -> log.debug("Run started"));
//
//        CompletableFuture<Net.ResultOfQueryCollection> fut =  Net.queryCollection(
//                sdk.context(),
//                "accounts",
//                filter1,
//                "id acc_type balance boc last_paid",
//                null,
//                null
//        );
//
//        CompletableFuture<Net.ResultOfQueryCollection> fut2 =  Net.queryCollection(
//                sdk.context(),
//                "accounts",
//                filter2,
//                "id acc_type balance boc last_paid",
//                null,
//                null
//        );
//
//        CompletableFuture<Net.ResultOfQueryCollection> futWrong =  Net.queryCollection(
//                sdk.context(),
//                "accounts",
//                filterWrong,
//                "id acc_type balance boc last_paid",
//                null,
//                null
//        );
//
//
//        var future = CompletableFuture.allOf(fut,fut2,futWrong);
//
//        Map<String,Object> params = Map.of(
//                "dest", "to.makeAddrStd()",
//                "value", "1000000000",
//                "bounce", false
//        );
//
//        future.thenApply(Processing.processMessage(sdk.context(),
//                this.abi.abiJson(),
//                this.address().makeAddrStd(),
//                null,
//                new Abi.CallSet("sendTransaction", null, functionInputs),
//                credentials.signer(), null, false, null))
//
//        this.account.callExternal(this.credentials, "sendTransaction", params);


        Message.decodeOutputMessage().decoded().orElseThrow())


//
//
//                sdk
//                .first()
//                .also()
//                .next()
//                .next()
//                .next();
//
//        sdk
//                .first()
//                .next()
//                .next()
//                .waitFor()
//                .next()
//                .await();


    }

    @Test
    public void testSequentialSdkExecution() throws ExecutionException, InterruptedException {

    }

}
