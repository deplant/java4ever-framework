package tech.deplant.java4ever.frtest.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SubscriptionTests {

	private static System.Logger logger = System.getLogger(SubscriptionTests.class.getName());


	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		INIT();
		INIT_LOCAL_WALLETS();
	}

	@Test
	public void try_account_subscription() throws Throwable {
		String subscribedIdPre = LOCAL_MSIG_ROOT.address();
		BigInteger subscribedBalancePre = LOCAL_MSIG_ROOT.accountBalance();
		logger.log(System.Logger.Level.INFO, LOCAL_MSIG_ROOT.accountBalance());
		var handle = LOCAL_MSIG_ROOT.subscribeOnTransactions("account_addr balance_delta", event -> {
				//String accountAddr = ((Map<String, Object>)((Map<String, Object>)event.result().get("result")).get("transactions")).get("account_addr").toString();
				var transactions = event.result().get("result").get("transactions");
				String accountAddr = transactions.get("account_addr").asText();
				String accountBalanceDelta = transactions.get("balance_delta").asText();
				assertEquals(subscribedIdPre, accountAddr);
				logger.log(System.Logger.Level.INFO, accountBalanceDelta);
				assertNotEquals(BigInteger.ZERO, Uint.fromJava(128, accountBalanceDelta).toJava());
		});
		LOCAL_MSIG_ROOT.sendTransaction(new Address(LOCAL_MSIG_ROOT.address()),
		                                CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER, "2"),
		                                false,
		                                MessageFlag.EXACT_VALUE_GAS.flag(),
		                                TvmCell.EMPTY()).call();
		assertNotEquals(subscribedBalancePre, LOCAL_MSIG_ROOT.accountBalance());
		handle.unsubscribe();
	}

}
