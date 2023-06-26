package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.frtest.unit.Env.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class Tip3Tests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		Env.INIT();
		Env.INIT_LOCAL_WALLETS();
		Env.INIT_LOCAL_TIP3();
	}


	@Test
	public void tip3_root_deployed() throws Throwable {
		assertTrue(Account.ofAddress(SDK_LOCAL, LOCAL_TIP3_ROOT.address()).isActive());
	}

	@Test
	public void tip3_wallets_deployed() throws Throwable {
		assertTrue(Account.ofAddress(SDK_LOCAL, LOCAL_TIP3_WALLET1.address()).isActive());
		assertTrue(Account.ofAddress(SDK_LOCAL, LOCAL_TIP3_WALLET2.address()).isActive());
	}

	@Test
	public void tip3_mint() throws Throwable {
		var mintAmount = new BigInteger("9000000");
		var receiverAddress = new Address(LOCAL_MSIG_WALLET1.address());
		GIVER_LOCAL.give(LOCAL_TIP3_ROOT.address(), EVER_TEN);
		GIVER_LOCAL.give(LOCAL_MSIG_ROOT.address(), EVER_TEN);
		LOCAL_TIP3_ROOT.mint(mintAmount, receiverAddress, EVER_ONE, receiverAddress, false,
		                     TvmCell.EMPTY())
		               .sendFromTree(LOCAL_MSIG_ROOT,
		                             CurrencyUnit.VALUE(EVER, "0.3"),
		                             true,
		                             MessageFlag.FEE_EXTRA,
		                             true,
		                             TIP3TokenWallet.DEFAULT_ABI());
		assertEquals(mintAmount, LOCAL_TIP3_ROOT.totalSupply().get().value0());
		assertEquals(mintAmount, LOCAL_TIP3_WALLET1.balance().get().value0());
	}

}
