package tech.deplant.java4ever.frtest.unit;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.MultisigWallet;

import java.io.IOException;
import java.math.BigInteger;

import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;

public class Env {

	public static Sdk SDK_EMPTY;
	public static Sdk SDK_LOCAL;
	public static Sdk SDK_DEV;
	public static Sdk SDK_MAIN;
	public static EverOSGiver GIVER_LOCAL;
	public static MultisigWallet GIVER_DEV;
	public static MultisigWallet GIVER_MAIN;
	public static System.Logger LOG = System.getLogger(Env.class.getName());
	private static boolean isInitialized = false;

	public static BigInteger EVER_ZERO = CurrencyUnit.VALUE(EVER, "0");
	public static BigInteger MILLI_100 = CurrencyUnit.VALUE(MILLIEVER, "100");
	public static BigInteger EVER_ONE = CurrencyUnit.VALUE(EVER, "1");
	public static BigInteger EVER_TWO = CurrencyUnit.VALUE(EVER, "2");
	public static BigInteger EVER_FIVE = CurrencyUnit.VALUE(EVER, "5");
	public static BigInteger EVER_TEN = CurrencyUnit.VALUE(EVER, "10");

	public static void INIT() throws IOException {
		if (!isInitialized) {
			isInitialized = true;
			SDK_EMPTY = Sdk.builder().build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
			SDK_LOCAL = Sdk.builder()
			               .networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
			               .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
			SDK_DEV = Sdk.builder()
			             .networkEndpoints(System.getenv("DEV_NET_OSIRIS_ENDPOINT"))
			             .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
			SDK_MAIN = Sdk.builder()
			              .networkEndpoints(System.getenv("MAIN_NET_OSIRIS_ENDPOINT"))
			              .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
			GIVER_LOCAL = EverOSGiver.V2(SDK_LOCAL);
		}
	}

	public static Credentials RNG_KEYS() throws EverSdkException {
		return Credentials.RANDOM(SDK_EMPTY);
	}

	public static Seed RNG_SEED() throws EverSdkException {
		return Seed.RANDOM(Env.SDK_EMPTY);
	}
}
