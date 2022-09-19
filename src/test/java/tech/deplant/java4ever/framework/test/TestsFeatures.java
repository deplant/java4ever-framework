package tech.deplant.java4ever.framework.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;
import tech.deplant.java4ever.framework.GraphQLFilter;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.SdkBuilder;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.template.abi.IAbi;
import tech.deplant.java4ever.framework.template.abi.JsonAbi;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TestsFeatures {

	private static Logger log = LoggerFactory.getLogger(TestsFeatures.class);

//    public static Account ofAddress(Sdk sdk, tech.deplant.java4ever.framework.type.Address address, ContractAbi abi) throws Sdk.SdkException {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("id", new GraphQL.Filter.In(new String[]{address.makeAddrStd()}));
//        Object[] results = sdk.syncCall(Net.queryCollection(sdk.context(), "accounts", filter, "id acc_type balance boc last_paid", null, null)).result();
//        var collection = new Gson().fromJson(new Gson().toJson(results[0]), Account.AccountQueryCollection.class);
//        return new Account(sdk, address, abi, collection.acc_type(), Data.hexToDec(collection.balance(), 9), collection.boc(), Instant.ofEpochSecond(collection.last_paid()));
//    }

	static {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "TRACE");
	}

	@Test
	public void testBetterComposition() throws ExecutionException, InterruptedException, JsonProcessingException {
		final Sdk sdkDEV = new SdkBuilder()
				.networkEndpoints(Sdk.Network.DEV_NET.endpoints())
				.create(JavaLibraryPathLoader.TON_CLIENT);
		final Sdk sdkSE = new SdkBuilder()
				.networkEndpoints("http://80.78.241.3")
				.timeout(50L)
				.create(JavaLibraryPathLoader.TON_CLIENT);


		var giver = new EverOSGiver(sdkSE);
		var msig = MsigTemplate.SAFE()
		                       .deployWithGiver(sdkSE, 0, Credentials.RANDOM(sdkSE), giver, new BigInteger("2"));
		//.send();
		//msig.get().send();
		IAbi abi = MsigTemplate.SAFE().abi();
		IAbi abi2 = JsonAbi.ofString("{}");
		//msig.callExternal("", null,);
		//IAbi abi3 = ArtifactABI.ofResource("");
		//ArtifactABI abi4 = new ArtifactABI(CachedABI.of("{}"), new LocalJsonArtifact(Paths.get("")));

	}

	// test generate hashes 1000 times
	@Test
	public void testJacksonConvert() throws ExecutionException, InterruptedException {
		System.out.println("");

		final Sdk sdk = new SdkBuilder()
				.networkEndpoints(new String[]{"http://80.78.254.199/"})
				.timeout(50L)
				.create(new JavaLibraryPathLoader("ton_client"));

		ObjectMapper mapper = JsonMapper.builder()
		                                .addModule(new ParameterNamesModule())
		                                .addModule(new Jdk8Module())
		                                .addModule(new JavaTimeModule())
		                                .build();

		record AccountQuery(String id, int acc_type, String balance, String boc, long last_paid) {
		}

		Map<String, Object> filter = new HashMap<>();
		filter.put("id",
		           new GraphQLFilter.In(new String[]{"0:e2a1dcec8bebff29c207d8944aef1bc8a5a9500789096c6a83a3a9bd71dd75fa"}));
		Object[] results = Net.queryCollection(sdk.context(),
		                                       "accounts",
		                                       filter,
		                                       "id acc_type balance boc last_paid",
		                                       null,
		                                       null).result();
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
