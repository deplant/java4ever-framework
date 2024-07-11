package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.subscription.Subscriptions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SubscribeHandleTests {

	private static System.Logger logger = System.getLogger(SubscribeHandleTests.class.getName());


	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		INIT();
		INIT_LOCAL_WALLETS();
	}

//	@Test
//	public void subscription_until_condition() throws Throwable {
//		var keys = Env.RNG_KEYS();
//		var deployStatement = new SafeMultisigWalletTemplate().prepareDeploy(Env.SDK_LOCAL, 0,
//		                                                                     keys,
//		                                                                     new BigInteger[]{keys.publicKeyBigInt()},
//		                                                                     1);
//		var uninitAddress = deployStatement.toAddress();
//		final CompletableFuture<JsonNode> waiter = new CompletableFuture<>();
//		Consumer<JsonNode> eventConsumer = waiter::complete;
//		Subscriptions.onAccounts("acc_type")
//		             .addFilterOnSubscription("id: { eq: \"%s\" }".formatted(uninitAddress.toString()))
//		             .addCallbackConsumer(eventConsumer);
//		GIVER_LOCAL.give(uninitAddress, EVER_FIVE).call();
//		waiter.get(10, TimeUnit.MINUTES);
//		return deploy(address);
//	}

	@Test
	public void try_account_subscription() throws Throwable {
		String subscribedIdPre = String.valueOf(LOCAL_MSIG_ROOT.address());
		BigInteger subscribedBalancePre = LOCAL_MSIG_ROOT.accountBalance();
		logger.log(System.Logger.Level.INFO, subscribedBalancePre);
		Executors.newVirtualThreadPerTaskExecutor().submit(() -> LOCAL_MSIG_ROOT.subscribeOnTransactions(event -> {
			var transactions = event.get("result").get("transactions");
			String accountAddr = transactions.get("account_addr").asText();
			String accountBalanceDelta = transactions.get("balance_delta").asText();
			assertEquals(subscribedIdPre, accountAddr);
			logger.log(System.Logger.Level.INFO, accountBalanceDelta);
			assertNotEquals(BigInteger.ZERO, Uint.of(128, accountBalanceDelta).toBigInteger());
		}, "account_addr", "balance_delta"));
		var tokenUnit = new CurrencyUnit.CustomToken(8);
		var nanoValue = CurrencyUnit.VALUE(tokenUnit, "2");
		LOCAL_MSIG_WALLET1.sendTransaction(new Address(subscribedIdPre),
		                                   CurrencyUnit.VALUE(MILLIEVER, "2"),
		                                   false,
		                                   MessageFlag.EXACT_VALUE_GAS.flag(),
		                                   TvmCell.EMPTY).call();
		assertNotEquals(subscribedBalancePre, LOCAL_MSIG_ROOT.accountBalance());


	}

}
