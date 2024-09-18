package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.artifact.ByteResource;
import tech.deplant.java4ever.framework.contract.EverWalletContract;
import tech.deplant.java4ever.framework.datatype.*;
import tech.deplant.java4ever.framework.template.EverWalletTemplate;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class DeployHandleTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws Throwable {
		Env.INIT();
	}

	@Test
	public void box_signing_example() throws Throwable {
		var keys = Env.RNG_KEYS();
		var boxHandle = EverSdk.await(Crypto.registerSigningBox(SDK_LOCAL, new AppSigningBox() {
			@Override
			public String getPublicKey() {
				return "";
			}

			@Override
			public String sign(String unsigned) {
				return "";
			}
		})).handle();
		var signer = new Abi.Signer.SigningBox(boxHandle);
		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,
		                                                                     0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicKeyBigInt()},
		                                                                     1);
		var contract1 = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		assertTrue(contract1.account().isActive());
	}


//	@Test
//	public void check_signature_id() throws Throwable {
//		Client.config(SDK_LOCAL).
//	}

	@Disabled
	@Test
	public void test_signature_ids() throws Throwable {
		assertNull(EverSdk.await(Net.getSignatureId(SDK_DEV)).signatureId());
		assertNull(EverSdk.await(Net.getSignatureId(SDK_MAIN)).signatureId());
		assertEquals(1,
		             EverSdk.await(Net.getSignatureId(EverSdk.createWithEndpoint("https://gql.venom.foundation/graphql"))).signatureId());
	}

	@Test
	public void test_single_contract_dev() throws Throwable {
		int offlineContext = EverSdk.builder().networkSignatureId(1L).networkQueryTimeout(300_000L).build();
		var template = new EverWalletTemplate();
		var seed = new Seed("year blur lounge can net tackle bonus mention loop churn dash inspire",
		                    12); // creates new seed
		var keys = seed.deriveCredentials(offlineContext); // derives keys from seed
		// let's calculate future EVER Wallet address
		var stateInit = template.getStateInit(offlineContext, keys.publicKey(), BigInteger.ZERO);
		var address = template.getAddress(offlineContext, 0, stateInit);
		var body = new EverWalletContract(offlineContext, address, keys).sendTransaction(address, EVER_ONE, false)
		                                                                .toPayload(false);
		//GIVER_LOCAL.give(address, EVER_TWO).call();
		// deploy EVER Wallet
		//var contract = template.deployAndSend(Env.SDK_LOCAL, 0, keys, BigInteger.ZERO, Address.ZERO, EVER_ONE);
		System.out.printf("Address funded: %s%n", address);
		System.out.printf("Seed: %s, public: %s%n", seed.phrase(), keys.publicKey());

		// let's send messages when we're online
		EverSdk.sendExternalMessage(EverSdk.createWithEndpoint("https://gql.venom.foundation/graphql"),
		                            address.makeAddrStd(),
		                            EverWalletTemplate.DEFAULT_ABI().ABI(),
		                            stateInit.cellBoc(),
		                            body.cellBoc(),
		                            null);

//		var message = EverSdk.await(Boc.encodeExternalInMessage(offlineContext,
//		                                                        null,
//		                                                        address.makeAddrStd(),
//		                                                        stateInit.cellBoc(),
//		                                                        body.cellBoc(),
//		                                                        null)).message();
//
//		var request = EverSdk.await(Processing.sendMessage(SDK_DEV, message, template.abi().ABI(), false, null));
//
//		EverSdk.await(Processing.waitForTransaction(SDK_DEV,
//		                                                   template.abi().ABI(),
//		                                                   message,
//		                                                   request.shardBlockId(),
//		                                                   false,
//		                                                   request.sendingEndpoints(),
//		                                                   null));
	}

	@Disabled
	@Test
	public void find_addresses_for_ever_wallet() throws Throwable {
		int offlineContext = EverSdk.builder().networkSignatureId(100L).networkQueryTimeout(300_000L).build();
//		int offlineContext = SDK_LOCAL;
		int i = 0;
		var template = new EverWalletTemplate();
		// let's generate 5 addresses that match certain condition and send them 1 ever
		Predicate<Address> addressCondition = address -> address.makeAddrStd().contains("7777");
		while (i < 5) {
			var seed = Env.RNG_SEED(); // creates new seed
			var keys = seed.deriveCredentials(offlineContext); // derives keys from seed
			// let's calculate future EVER Wallet address
			var stateInit = template.getStateInit(offlineContext, keys.publicKey(), BigInteger.ZERO);
			var address = template.getAddress(offlineContext, 0, stateInit);
			// specify our conditions when we want to deploy contract
			if (addressCondition.test(address)) {
				// let's create message bodies offline
				// here we send sendTransaction to ourselves
				var body = new EverWalletContract(offlineContext, address, keys).sendTransaction(address,
				                                                                                 EVER_ONE,
				                                                                                 false)
				                                                                .toPayload(false);
				System.out.printf("Address: %s%n", address);
				System.out.printf("Seed: %s, public: %s%n", seed.phrase(), keys.publicKey());
				System.out.printf("Message body: %s%n", body);
				System.out.printf("State init: %s%n", stateInit);
				// let's send messages when we're online
				EverSdk.sendExternalMessage(SDK_LOCAL,
				                            address.makeAddrStd(),
				                            EverWalletTemplate.DEFAULT_ABI().ABI(),
				                            stateInit.cellBoc(),
				                            body.cellBoc(),
				                            null);
				i++;
			}
		}
	}

	@Disabled
	@Test
	public void check_ever_wallet_send() throws Throwable {
		// рандомные ключи
		var keys = Env.RNG_KEYS();
		// тащим код и аби EVER Wallet
		var everWalletCode = Base64.getEncoder()
		                           .encodeToString(new ByteResource("artifacts/everwallet/Wallet.code.boc").get());
		Map<String, Object> initDataMap = Map.of("publicKey", "0x" + keys.publicKey(), "timestamp", 0);
		var everWalletAbi = ContractAbi.ofResource("artifacts/everwallet/Wallet.abi.json");
		// кодируем начальное состояние
		var initialData = EverSdk.await(Abi.encodeInitialData(Env.SDK_OFFLINE,
		                                                      everWalletAbi.ABI(),
		                                                      (ObjectNode) JsonContext.convertAbiMap(initDataMap,
		                                                                                             JsonNode.class),
		                                                      "0x" + keys.publicKey(),
		                                                      null)).data();
		List<AbiValue> types = List.of(Uint.of(256, keys.publicKeyBigInt()), Uint.of(64, 0));
		var builder = new TvmBuilder();
		builder.store(types.toArray(AbiValue[]::new));
		var cellData = builder.toCell(Env.SDK_OFFLINE);
		var stateInit = EverSdk.await(Boc.encodeStateInit(Env.SDK_OFFLINE,
		                                                  everWalletCode,
		                                                  cellData.cellBoc(),
		                                                  null,
		                                                  null,
		                                                  null,
		                                                  null,
		                                                  null)).stateInit();
		// адрес получился
		var everWalletAddress = "0:%s".formatted(new TvmCell(stateInit).bocHash(Env.SDK_OFFLINE));
		System.out.println(everWalletAddress);
		// дадим туда денег
		GIVER_LOCAL.give(Address.fromJava(everWalletAddress), EVER_TWO).call();
		// параметры функции sendTransaction, которую мы вызываем и одновременно деплоим сам контракт
		Map<String, Object> inputMap = Map.of("dest",
		                                      "0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7",
		                                      "value",
		                                      EVER_ONE,
		                                      // amount in nano EVER
		                                      "bounce",
		                                      false,
		                                      "flags",
		                                      3,
		                                      "payload",
		                                      TvmCell.EMPTY.cellBoc());
		var callHandle = new FunctionHandle<Map>(Map.class,
		                                         SDK_OFFLINE,
		                                         new Address(everWalletAddress),
		                                         everWalletAbi,
		                                         keys,
		                                         "sendTransaction",
		                                         inputMap,
		                                         null);

		var body = EverSdk.await(Abi.encodeMessageBody(SDK_OFFLINE,
		                                               everWalletAbi.ABI(),
		                                               callHandle.toCallSet(),
		                                               false,
		                                               keys.signer(),
		                                               null,
		                                               everWalletAddress,
		                                               null)).body();

		var message = EverSdk.await(Boc.encodeExternalInMessage(SDK_LOCAL,
		                                                        null,
		                                                        everWalletAddress,
		                                                        stateInit,
		                                                        body,
		                                                        null)).message();

		// отправляем сообщение и ждем результат

		var request = EverSdk.await(Processing.sendMessage(SDK_LOCAL, message, everWalletAbi.ABI(), false, null));

		var transaction = EverSdk.await(Processing.waitForTransaction(SDK_LOCAL,
		                                                              everWalletAbi.ABI(),
		                                                              message,
		                                                              request.shardBlockId(),
		                                                              false,
		                                                              request.sendingEndpoints(),
		                                                              null)).transaction();

//		EverSdk.await(Processing.processMessage(Env.SDK_LOCAL,
//		                                        everWalletAbi.ABI(),
//		                                        everWalletAddress,
//		                                        new Abi.DeploySet(null, null, stateInit, 0L, null, null),
//		                                        callHandle.toCallSet(),
//		                                        keys.signer(),
//		                                        null,
//		                                        null,
//		                                        false));

		System.out.println("Contract deployed");
		System.out.println("Address: " + everWalletAddress);
		System.out.println("Contract deployed. Transaction hash: " + transaction.get("id").asText());
		assertEquals(transaction.get("status").asInt(), 3);
	}

	@Test
	public void contract_is_active_after_deploy() throws Throwable {
		var keys = Env.RNG_KEYS();
		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,
		                                                                     0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicKeyBigInt()},
		                                                                     1);
		var contract1 = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
		assertTrue(contract1.account().isActive());
	}

//	@Test
//	public void deployed_contract_pubkey_in_gql_equals_to_expected() throws Throwable {
//		var keys = Env.RNG_KEYS();
//		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,0,
//		                                                                     keys,
//		                                                                     new BigInteger[]{keys.publicBigInt()},
//		                                                                     1);
//		var contract1 = deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE);
//		assertEquals(keys.publicKey(),contract1.tvmPubkey());
//	}

	@Test
	public void first_deployment_passes_second_throws_with_414() throws Throwable {
		var keys = Env.RNG_KEYS();

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL,
		                                                                     0,
		                                                                     keys,
		                                                                     new BigInteger[]{keys.publicKeyBigInt()},
		                                                                     1);
		deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE); // first try
		try {
			deployStatement.deployWithGiver(Env.GIVER_LOCAL, EVER_ONE); // second try
		} catch (EverSdkException e) {
			assertEquals(414, e.errorResponse().code());
		}
	}
}
