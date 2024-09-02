package tech.deplant.java4ever.frtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigWalletContract;
import tech.deplant.java4ever.framework.contract.multisig2.SafeMultisigWallet2Contract;
import tech.deplant.java4ever.framework.contract.tip3.TIP3TokenWalletContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWallet2Template;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import static tech.deplant.java4ever.frtest.unit.Env.EVER_ONE;
import static tech.deplant.java4ever.frtest.unit.Env.MILLI_100;

public class DevTests {

	public static int CTX;
	private static System.Logger logger = System.getLogger(DevTests.class.getName());

	@Test
	public void venomBot() throws IOException, EverSdkException {
		EverSdk.load(new AbsolutePathLoader("c:/opt/sdk/ton_client.dll"));
		CTX = EverSdk.builder()
		             .networkEndpoints("https://venom-mainnet.tvmlabs.dev/graphql")
		             .networkQueryTimeout(300000L)
		             .build();
		var keys = Credentials.ofFile("c:/Users/Laugan/.deplant/venomKeys.json");
		var myEverWallet = new SafeMultisigWallet2Contract(CTX, "0:b8fb645ea683d242bf4586d1f678e175a1bd19816acd0cbb8abb68c309572585",keys);
		var myUsdtWallet = new TIP3TokenWalletContract(CTX,
		                                               "0:313d90bf3f1bfeb32dae0a693898c1c8c8bedfb4ca8347f9dd8851797f834709");
		var w3wUsdtWallet = new TIP3TokenWalletContract(CTX,
		                                                "0:35a07549497a802463e527e495153923c532273cd691b0c631f686b2332d2a2c");

		//for (int i = 0; i < 100; i++) {
			// credentials from System ENV
			myUsdtWallet.transfer(new BigInteger("1000"),
			                      new Address("0:16b5e7dceb434fb5cdccd6d992987af207d50f20566a1439cb80f9d3e7d1fafd"),
			                      BigInteger.ZERO,
			                      myEverWallet.address(),
			                      true,
			                      new TvmCell(
					                      "te6ccgEBAwEAlwACtwYAAAAAerCwAAAAAAAAAAAAAAAAAAAAAACAFx9si9TQekhX6LDaPs8cLrQ3ozAtWaGXcVdtGGEq5LCwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAgEAYwAAAAAAAAAAAAAAAAAwQS2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAFA")
			).sendFrom(myEverWallet, MILLI_100, true, MessageFlag.EXACT_VALUE_GAS);
			// Subscribe on update of W3W wallet
			// Repeat

			// 10 times
		//}
	}

}
