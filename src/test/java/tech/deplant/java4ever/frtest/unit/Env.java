package tech.deplant.java4ever.frtest.unit;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.contract.tip3.*;
import tech.deplant.java4ever.framework.datatype.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;

public class Env {


	final static String LOCAL_ENDPOINT = System.getenv("LOCAL_NODE_ENDPOINT");
	final static String DEV_ENDPOINT = System.getenv("DEV_NET_OSIRIS_ENDPOINT");
	final static String MAIN_ENDPOINT = System.getenv("MAIN_NET_OSIRIS_ENDPOINT");
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
	public static GiverContract GIVER_LOCAL;
	public static GiverContract GIVER_DEV;
	public static GiverContract GIVER_MAIN;
	public static System.Logger LOG = System.getLogger(Env.class.getName());
	static BigInteger TIP3_NONCE;
	static Credentials LOCAL_KEYS_ROOT;
	static Credentials LOCAL_KEYS_WALLET1;
	static Credentials LOCAL_KEYS_WALLET2;
	static MultisigContract LOCAL_MSIG_ROOT;
	static MultisigContract LOCAL_MSIG_WALLET1;
	static MultisigContract LOCAL_MSIG_WALLET2;
	static TIP3TokenRootContract LOCAL_TIP3_ROOT;
	static TIP3TokenWalletContract LOCAL_TIP3_WALLET1;
	static TIP3TokenWalletContract LOCAL_TIP3_WALLET2;
	//static LibraryLoader LOADER = AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB");

	static LibraryLoader DEFAULT_LOADER = new DefaultLoader(ClassLoader.getSystemClassLoader());//AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB");

	private static boolean isInitialized = false;

	public static void INIT() throws IOException, EverSdkException {
		if (!isInitialized) {

			// should be first
			SDK_EMPTY = Sdk.builder().build(DEFAULT_LOADER);
			SDK_LOCAL = Sdk.builder().networkEndpoints(LOCAL_ENDPOINT).build(DEFAULT_LOADER);
			SDK_DEV = Sdk.builder().networkEndpoints(DEV_ENDPOINT).timeout(600_000).networkQueryTimeout(300_000L).build(DEFAULT_LOADER);
			SDK_MAIN = Sdk.builder().networkEndpoints(MAIN_ENDPOINT).timeout(600_000).networkQueryTimeout(300_000L).build(DEFAULT_LOADER);

			GIVER_LOCAL = EverOSGiver.V2(SDK_LOCAL);

			isInitialized = true;
		}
	}

	public static void INIT_LOCAL_WALLETS() throws IOException, EverSdkException {
		LOCAL_KEYS_ROOT = RNG_KEYS();
		LOCAL_KEYS_WALLET1 = RNG_KEYS();
		LOCAL_KEYS_WALLET2 = RNG_KEYS();

		LOCAL_MSIG_ROOT = new MultisigBuilder().setType(MultisigContract.Type.SAFE)
		                                       .build(SDK_LOCAL,
		                                              LOCAL_KEYS_ROOT,
		                                              GIVER_LOCAL,
		                                              CurrencyUnit.VALUE(EVER, "4.5"));

		LOCAL_MSIG_WALLET1 = new MultisigBuilder().setType(MultisigContract.Type.SURF)
		                                          .build(SDK_LOCAL,
		                                                 LOCAL_KEYS_ROOT,
		                                                 GIVER_LOCAL,
		                                                 CurrencyUnit.VALUE(EVER, "4.5"));

		LOCAL_MSIG_WALLET2 = new MultisigBuilder().setType(MultisigContract.Type.SETCODE)
		                                          .build(SDK_LOCAL,
		                                                 LOCAL_KEYS_ROOT,
		                                                 GIVER_LOCAL,
		                                                 CurrencyUnit.VALUE(EVER, "4.5"));
	}

	public static void INIT_LOCAL_TIP3() throws IOException, EverSdkException {
		TIP3_NONCE = BigInteger.valueOf(new Random().nextInt());
		LOCAL_TIP3_ROOT = new TIP3Builder()
				.setRootKeys(LOCAL_KEYS_ROOT)
				.setOwnerAddress(LOCAL_MSIG_ROOT.address())
				.setName("Test Token")
				.setSymbol("TST")
				.setDecimals(6)
				.setRandomNonce(new Random().nextInt())
				.build(SDK_LOCAL, GIVER_LOCAL, CurrencyUnit.VALUE(EVER, "1.3"));

		LOCAL_TIP3_ROOT.deployWallet(new Address(LOCAL_MSIG_WALLET1.address()),
		                             CurrencyUnit.VALUE(EVER, "0.5"))
		               .sendFrom(LOCAL_MSIG_ROOT,
		                         CurrencyUnit.VALUE(EVER, "1.5"));

		LOCAL_TIP3_ROOT.deployWallet(new Address(LOCAL_MSIG_WALLET2.address()),
		                             CurrencyUnit.VALUE(EVER, "0.5"))
		               .sendFrom(LOCAL_MSIG_ROOT,
		                         CurrencyUnit.VALUE(EVER, "1.5"));

		LOCAL_TIP3_WALLET1 = new TIP3TokenWalletContract(SDK_LOCAL,LOCAL_TIP3_ROOT.walletOf(new Address(LOCAL_MSIG_WALLET1.address())).get().value0().toString());
		LOCAL_TIP3_WALLET2 = new TIP3TokenWalletContract(SDK_LOCAL,LOCAL_TIP3_ROOT.walletOf(new Address(LOCAL_MSIG_WALLET2.address())).get().value0().toString());
	}

	public static Credentials RNG_KEYS() throws EverSdkException {
		return Credentials.RANDOM(SDK_EMPTY);
	}

	public static Seed RNG_SEED() throws EverSdkException {
		return Seed.RANDOM(Env.SDK_EMPTY);
	}
}
