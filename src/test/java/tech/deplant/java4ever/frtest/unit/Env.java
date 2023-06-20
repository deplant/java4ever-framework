package tech.deplant.java4ever.frtest.unit;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.contract.*;
import tech.deplant.java4ever.framework.datatype.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Random;

import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;

public class Env {



	public static BigInteger EVER_ZERO = CurrencyUnit.VALUE(EVER, "0");
	public static BigInteger MILLI_100 = CurrencyUnit.VALUE(MILLIEVER, "100");
	public static BigInteger EVER_ONE = CurrencyUnit.VALUE(EVER, "1");
	public static BigInteger EVER_TWO = CurrencyUnit.VALUE(EVER, "2");
	public static BigInteger EVER_FIVE = CurrencyUnit.VALUE(EVER, "5");
	public static BigInteger EVER_TEN = CurrencyUnit.VALUE(EVER, "10");

	public static Sdk SDK_EMPTY;
	public static Sdk SDK_LOCAL;
	public static Sdk SDK_DEV;
	public static Sdk SDK_MAIN;
	public static EverOSGiver GIVER_LOCAL;
	public static MultisigWallet GIVER_DEV;
	public static MultisigWallet GIVER_MAIN;
	public static System.Logger LOG = System.getLogger(Env.class.getName());
	private static boolean isInitialized = false;
	static BigInteger TIP3_NONCE;

	static Credentials LOCAL_KEYS_ROOT;
	static Credentials LOCAL_KEYS_WALLET1;
	static Credentials LOCAL_KEYS_WALLET2;

	static MultisigWallet LOCAL_MSIG_ROOT;
	static MultisigWallet LOCAL_MSIG_WALLET1;
	static MultisigWallet LOCAL_MSIG_WALLET2;

	static TIP3TokenRoot LOCAL_TIP3_ROOT;
	static TIP3TokenWallet LOCAL_TIP3_WALLET1;
	static TIP3TokenWallet LOCAL_TIP3_WALLET2;

	final static String LOCAL_ENDPOINT = System.getenv("LOCAL_NODE_ENDPOINT");
	final static String DEV_ENDPOINT = System.getenv("DEV_NET_OSIRIS_ENDPOINT");

	final static String MAIN_ENDPOINT = System.getenv("MAIN_NET_OSIRIS_ENDPOINT");

	static LibraryLoader LOADER = AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB");

	public static void INIT() throws IOException, EverSdkException {
		if (!isInitialized) {

			// should be first
			SDK_EMPTY = Sdk.builder().build(LOADER);

			SDK_LOCAL = Sdk.builder()
			               .networkEndpoints(LOCAL_ENDPOINT)
			               .build(LOADER);
			GIVER_LOCAL = EverOSGiver.V2(SDK_LOCAL);

			SDK_DEV = Sdk.builder()
			             .networkEndpoints(DEV_ENDPOINT)
			             .build(LOADER);
			SDK_MAIN = Sdk.builder()
			              .networkEndpoints(MAIN_ENDPOINT)
			              .build(LOADER);

			isInitialized = true;
		}
	}

	public static void INIT_LOCAL_WALLETS() throws IOException, EverSdkException {
		LOCAL_KEYS_ROOT = RNG_KEYS();
		LOCAL_KEYS_WALLET1 = RNG_KEYS();
		LOCAL_KEYS_WALLET2 = RNG_KEYS();

		LOCAL_MSIG_ROOT = MultisigWallet.deploySingleSig(MultisigWallet.Type.SAFE, SDK_LOCAL, GIVER_LOCAL, LOCAL_KEYS_ROOT, EVER_TEN);
		LOCAL_MSIG_WALLET1 = MultisigWallet.deploySingleSig(MultisigWallet.Type.SURF, SDK_LOCAL, GIVER_LOCAL, LOCAL_KEYS_WALLET1, EVER_TWO);
		LOCAL_MSIG_WALLET2 =  MultisigWallet.deploySingleSig(MultisigWallet.Type.SETCODE, SDK_LOCAL, GIVER_LOCAL, LOCAL_KEYS_WALLET2, EVER_TWO);
	}

	public static void INIT_LOCAL_TIP3() throws IOException, EverSdkException {
		TIP3_NONCE = BigInteger.valueOf(new Random().nextInt());

		LOCAL_TIP3_ROOT = TIP3.deployRoot(SDK_LOCAL, LOCAL_KEYS_ROOT, TIP3_NONCE, LOCAL_MSIG_ROOT, GIVER_LOCAL, "Test Token", "TST", 6);
		LOCAL_TIP3_WALLET1 = TIP3.deployWallet(SDK_LOCAL, LOCAL_TIP3_ROOT, LOCAL_MSIG_ROOT, new Address(LOCAL_MSIG_WALLET1.address()));
		LOCAL_TIP3_WALLET2 = TIP3.deployWallet(SDK_LOCAL, LOCAL_TIP3_ROOT, LOCAL_MSIG_ROOT, new Address(LOCAL_MSIG_WALLET2.address()));
	}

	public static Credentials RNG_KEYS() throws EverSdkException {
		return Credentials.RANDOM(SDK_EMPTY);
	}

	public static Seed RNG_SEED() throws EverSdkException {
		return Seed.RANDOM(Env.SDK_EMPTY);
	}
}
