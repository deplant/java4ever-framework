package tech.deplant.java4ever.framework.test;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.SdkBuilder;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.template.MsigTemplate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Log4j2
public class TestsFeatures {

//    public static Account ofAddress(Sdk sdk, tech.deplant.java4ever.framework.type.Address address, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
//        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
//        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
//        return new Account(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//    }

    @Test
    public void testBetterComposition() throws ExecutionException, InterruptedException, JsonProcessingException {
        final Sdk sdkDEV = new SdkBuilder()
                .networkEndpoints(SdkBuilder.Network.DEV_NET.endpoints())
                .create(new JavaLibraryPathLoader());
        final Sdk sdkSE = new SdkBuilder()
                .networkEndpoints(new String[]{"http://80.78.241.3/"})
                .timeout(50L)
                .create(new JavaLibraryPathLoader());


        var giver = new EverOSGiver(sdkSE);
        var msig = MsigTemplate.SAFE_MULTISIG_TEMPLATE.deployWithGiver(sdkSE, 0, Credentials.RANDOM(sdkSE).get(), giver, new BigInteger("2"));
        //msig.get().send();
        IAbi abi = MsigTemplate.SAFE_MULTISIG_ABI;
        //IAbi abi2 = CachedABI.of("{}");
        //IAbi abi3 = ArtifactABI.ofResource("");
        //ArtifactABI abi4 = new ArtifactABI(CachedABI.of("{}"), new LocalJsonArtifact(Paths.get("")));

    }

    // test generate hashes 1000 times
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
    public void testParallelSdkExecution() {

    }

    @Test
    public void testSequentialSdkExecution() throws ExecutionException, InterruptedException {

    }

}
