package tech.deplant.java4ever.framework.test;

import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;

public class TempTests {

	@Test
	public void test1() throws IOException, EverSdkException {
		var sdk = Sdk.builder()
		             .networkEndpoints("https://mainnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql")
		             .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));

		var keys = Credentials.ofSeed(sdk,
		                              new Seed(
				                              "vintage hire wheel physical jump mesh benefit cause attack neither reveal dumb",
				                              12));
		System.out.println(keys.publicKey());
		System.out.println(keys.secretKey());

		var acc = Account.ofAddress(sdk, "0:315e946e49e0f149989c82c9b5025697b92797985a3d66c6d18a4130edc3ca7f");
		//System.out.println(ContractTvc.ofBase64String(acc.data())
		//                              .decodeInitialData(sdk, MsigTemplate.SAFE_MULTISIG_ABI()));
		System.out.println(Address.ofFutureDeploy(sdk, new SafeMultisigWalletTemplate(), 0, null, Credentials.NONE));
	}
}
