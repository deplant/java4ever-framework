package tech.deplant.java4ever.frtest;

import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.tip3.TIP3TokenWalletContract;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.io.IOException;
import java.math.BigInteger;

import static tech.deplant.java4ever.frtest.unit.Env.EVER_ONE;

public class DevTests {

	public static int CTX;
	private static System.Logger logger = System.getLogger(DevTests.class.getName());

//	@Test
//	public void venomBot() throws IOException {
//		EverSdk.load();
//		CTX = EverSdk.builder()
//		             .networkEndpoints("https://venom-mainnet.tvmlabs.dev/graphql")
//		             .networkQueryTimeout(300000L)
//		             .build()
//		             .orElseThrow();
//		var myEverWallet = new EverWallet(CTX, "0:b8fb645ea683d242bf4586d1f678e175a1bd19816acd0cbb8abb68c309572585");
//		var myUsdtWallet = new TIP3TokenWalletContract(CTX,
//		                                               "0:313d90bf3f1bfeb32dae0a693898c1c8c8bedfb4ca8347f9dd8851797f834709");
//		var w3wUsdtWallet = new TIP3TokenWalletContract(CTX,
//		                                                "0:16b5e7dceb434fb5cdccd6d992987af207d50f20566a1439cb80f9d3e7d1fafd");
//
//		// credentials from System ENV
//		myUsdtWallet.transfer(new BigInteger("1000"),
//		                      w3wUsdtWallet.address(),
//		                      BigInteger.ZERO,
//		                      myEverWallet.address(),
//		                      true,
//		                      new TvmCell("te6ccgEBAwEAlwACtwYAAAAAerCwAAAAAAAAAAAAAAAAAAAAAACAFx9si9TQekhX6LDaPs8cLrQ3ozAtWaGXcVdtGGEq5LCwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAgEAYwAAAAAAAAAAAAAAAAAwQS2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAFA")
//		            ).sendFrom(myEverWallet, EVER_ONE, MessageFlag.EXACT_VALUE_GAS);
//		// Subscribe on update of W3W wallet
//		// Repeat
//
//		// 10 times
//	}

}
