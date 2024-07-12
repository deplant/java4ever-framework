package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.artifact.ByteResource;
import tech.deplant.java4ever.framework.datatype.*;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class DeployHandleTest {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws Throwable {
		Env.INIT();
	}

	@Test
	public void check_ever_wallet_send() throws Throwable {
		// рандомные ключи
		var keys = Env.RNG_KEYS();
		// тащим код и аби EVER Wallet
		var pubKey0x = "0x" + keys.publicKey();
		var everWalletCode = Base64.getEncoder().encodeToString(new ByteResource("artifacts/everwallet/Wallet.code.boc").get());
		Map<String, Object> initDataMap = Map.of("publicKey", pubKey0x, "timestamp", 0);
		var everWalletAbi = ContractAbi.ofResource("artifacts/everwallet/Wallet.abi.json");

		// кодируем начальное состояние

		List<AbiValue> types = List.of(Uint.of(256, keys.publicKeyBigInt()),
				                       Uint.of(64, "0"));

		var builder = new TvmBuilder();
		builder.store(types.toArray(AbiValue[]::new));

		var initData = builder.toCell(SDK_EMPTY).cellBoc();

//		var initialData = EverSdk.await(Abi.encodeInitialData(Env.SDK_LOCAL,
//		                                               everWalletAbi.ABI(),
//		                                               (ObjectNode) JsonContext.convertAbiMap(initDataMap,
//		                                                                                      JsonNode.class),
//														pubKey0x,
//		                                               null)).data();

		var stateInit = EverSdk.await(Boc.encodeStateInit(Env.SDK_LOCAL, everWalletCode, initData, null, null, null, null, null))
		                       .stateInit();
		System.out.println("State Init variable - " + stateInit);
		// адрес получился
		var everWalletAddress = "0:%s".formatted(new TvmCell(stateInit).bocHash(Env.SDK_LOCAL));
		System.out.println(everWalletAddress);
		// дадим туда денег
		GIVER_LOCAL.give(Address.fromJava(everWalletAddress), EVER_TWO).call();
		// параметры функции sendTransaction, которую мы вызываем и одновременно деплоим сам контракт
		Map<String, Object> inputMap = Map.of("dest",
		                                      "0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7",
		                                      "value",
		                                      EVER_ONE, // amount in nano EVER
		                                      "bounce",
		                                      false,
		                                      "flags",
		                                      3,
		                                      "payload",
		                                      TvmCell.EMPTY.cellBoc()
		);

		var callHandle = new FunctionHandle<Map>(Map.class,
		                                         SDK_LOCAL,
		                                         new Address(everWalletAddress),
		                                         everWalletAbi,
		                                         keys,
		                                         "sendTransaction",
		                                         inputMap,
		                                         null);
		var body = EverSdk.await(Abi.encodeMessageBody(Env.SDK_LOCAL,
		                                               everWalletAbi.ABI(),
				                                       callHandle.toCallSet(),
		                                               false,
		                                               keys.signer(),
		                                               null,
		                                               everWalletAddress,
		                                               null)).body();

		var message = EverSdk.await(Boc.encodeExternalInMessage(
				Env.SDK_LOCAL,
				null,
				everWalletAddress,
				stateInit,
				body,
				null)).message();

		// отправляем сообщение и ждем результат

		var request = EverSdk.await(Processing.sendMessage(SDK_LOCAL,message,everWalletAbi.ABI(),false,null));

		var transaction = EverSdk.await(
				Processing.waitForTransaction(
						SDK_LOCAL,
						everWalletAbi.ABI(),
						message,
						request.shardBlockId(),
						false,
						request.sendingEndpoints(),
						null))
				.transaction();

		System.out.println("Contract deployed");
		System.out.println("Address: " + everWalletAddress);
		System.out.println("Contract deployed. Transaction hash: " + transaction.get("id").asText());
		assertEquals(transaction.get("status").asInt(), 3);
	}

	@Test
	public void contract_is_active_after_deploy() throws Throwable {
		var keys = Env.RNG_KEYS();
		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL, 0,
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

		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL, 0,
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
