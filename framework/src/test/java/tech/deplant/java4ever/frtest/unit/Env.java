package tech.deplant.java4ever.frtest.unit;

import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.Seed;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigBuilder;
import tech.deplant.java4ever.framework.contract.multisig.MultisigContract;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigWalletContract;
import tech.deplant.java4ever.framework.contract.tip3.TIP3Builder;
import tech.deplant.java4ever.framework.contract.tip3.TIP3TokenRootContract;
import tech.deplant.java4ever.framework.contract.tip3.TIP3TokenWalletContract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;

public class Env {


	final static String LOCAL_ENDPOINT = "https://nodese.truequery.tech/graphql";
	final static String DEV_ENDPOINT = "https://devnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql";
	final static String MAIN_ENDPOINT = "https://mainnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql";
	public static BigInteger EVER_ZERO = CurrencyUnit.VALUE(EVER, "0");
	public static BigInteger MILLI_100 = CurrencyUnit.VALUE(MILLIEVER, "100");
	public static BigInteger EVER_ONE = CurrencyUnit.VALUE(EVER, "1");
	public static BigInteger EVER_TWO = CurrencyUnit.VALUE(EVER, "2");
	public static BigInteger EVER_FIVE = CurrencyUnit.VALUE(EVER, "5");
	public static BigInteger EVER_TEN = CurrencyUnit.VALUE(EVER, "10");
	public static int SDK_EMPTY;
	public static int SDK_LOCAL;
	public static int SDK_DEV;
	public static int SDK_MAIN;
	public static int SDK_OFFLINE;
	public static GiverContract GIVER_LOCAL;
	public static GiverContract GIVER_DEV;
	public static GiverContract GIVER_MAIN;
	public static System.Logger LOG = System.getLogger(Env.class.getName());
	public static int CTX;
	public static String ENDPOINT;
	public static long TIMEOUT = 30000L;
	public static GiverContract GIVER;
	static BigInteger TIP3_NONCE;
	static Credentials LOCAL_KEYS_ROOT;
	static Credentials LOCAL_KEYS_WALLET1;
	static Credentials LOCAL_KEYS_WALLET2;
	static MultisigContract LOCAL_MSIG_ROOT;
	static MultisigContract LOCAL_MSIG_WALLET1;
	//static LibraryLoader LOADER = AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB");
	static MultisigContract LOCAL_MSIG_WALLET2;
	static TIP3TokenRootContract LOCAL_TIP3_ROOT;
	static TIP3TokenWalletContract LOCAL_TIP3_WALLET1;
	static TIP3TokenWalletContract LOCAL_TIP3_WALLET2;
	static LibraryLoader DEFAULT_LOADER = new DefaultLoader(ClassLoader.getSystemClassLoader());//AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB");
	private static boolean isInitialized = false;

	public static void INIT2(String networkName) throws IOException, EverSdkException {
		if (!isInitialized) {
			EverSdk.load();
			ENDPOINT = switch (networkName) {
				case "local" -> System.getenv("LOCAL_NODE_ENDPOINT");
				case "everscale_dev" -> System.getenv("DEV_NET_OSIRIS_ENDPOINT");
				case "everscale_main" -> System.getenv("MAIN_NET_OSIRIS_ENDPOINT");
				case "venom_test" -> System.getenv("DEV_NET_OSIRIS_ENDPOINT");
				case "venom_main" -> System.getenv("MAIN_NET_OSIRIS_ENDPOINT");
				case "ackinacki_test" -> System.getenv("MAIN_NET_OSIRIS_ENDPOINT");
				case "gosh_main" -> System.getenv("MAIN_NET_OSIRIS_ENDPOINT");
				case "ton_main" -> "https://ton-testnet.tvmlabs.dev/graphql";
				default -> throw new IllegalStateException("Unexpected network name: " + networkName);
			};
			CTX = EverSdk.builder().networkEndpoints(ENDPOINT).networkQueryTimeout(TIMEOUT).build();

			GIVER = switch (networkName) {
				case "local" -> EverOSGiver.V3(CTX);
				case "everscale_dev" -> new SafeMultisigWalletContract(CTX,
				                                                       "0:b238570f9ebe536885b6060c7c9d74a20704e5efa844b17afcf814c7b9ddcfee",
				                                                       new Credentials(
						                                                       "a08ba000d026068f90541f111e2c700a796222d8ab4bb4ae8b4e680f64f875da",
						                                                       "17011c9157f3cf9e3c75ce8778be6b1adc42cd7abc1aebc0d288d2c338d2d93b"));
				case "venom_test" -> new SafeMultisigWalletContract(CTX,
				                                                    "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761",
				                                                    new Credentials(
						                                                    "7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
						                                                    "f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba"));
				default -> throw new IllegalStateException("Unexpected network name: " + networkName);
			};
		}
	}

	public synchronized static void INIT() throws IOException, EverSdkException {
		if (!isInitialized) {

			EverSdk.load(new AbsolutePathLoader("c:/opt/sdk/ton_client.dll"));

			// should be first
			SDK_EMPTY = EverSdk.createDefault();
			SDK_LOCAL = EverSdk.createWithEndpoint(LOCAL_ENDPOINT);
			SDK_DEV = EverSdk.builder().networkEndpoints(DEV_ENDPOINT).networkQueryTimeout(300_000L).build();
			SDK_MAIN = EverSdk.builder().networkEndpoints(MAIN_ENDPOINT).networkQueryTimeout(300_000L).build();
			SDK_OFFLINE = EverSdk.builder().networkSignatureId(0L).networkQueryTimeout(300_000L).build();

			GIVER_LOCAL = EverOSGiver.V3(SDK_LOCAL);

			isInitialized = true;
		}
	}

	public synchronized static void INIT_LOCAL_WALLETS() throws IOException, EverSdkException {
		if (LOCAL_MSIG_WALLET2 == null) {
			LOCAL_KEYS_ROOT = RNG_KEYS();
			LOCAL_KEYS_WALLET1 = RNG_KEYS();
			LOCAL_KEYS_WALLET2 = RNG_KEYS();

			LOCAL_MSIG_ROOT = new MultisigBuilder().setType(MultisigContract.Type.SAFE)
			                                       .prepareAndDeploy(SDK_LOCAL,
			                                                         LOCAL_KEYS_ROOT,
			                                                         GIVER_LOCAL,
			                                                         CurrencyUnit.VALUE(EVER, "4.5"));

			LOCAL_MSIG_WALLET1 = new MultisigBuilder().setType(MultisigContract.Type.SURF)
			                                          .prepareAndDeploy(SDK_LOCAL,
			                                                            LOCAL_KEYS_ROOT,
			                                                            GIVER_LOCAL,
			                                                            CurrencyUnit.VALUE(EVER, "4.5"));

			LOCAL_MSIG_WALLET2 = new MultisigBuilder().setType(MultisigContract.Type.SETCODE)
			                                          .prepareAndDeploy(SDK_LOCAL,
			                                                            LOCAL_KEYS_ROOT,
			                                                            GIVER_LOCAL,
			                                                            CurrencyUnit.VALUE(EVER, "4.5"));
		}
	}

	public static void INIT_LOCAL_TIP3() throws IOException, EverSdkException {
		TIP3_NONCE = BigInteger.valueOf(new Random().nextInt());
		LOCAL_TIP3_ROOT = new TIP3Builder().setRootKeys(LOCAL_KEYS_ROOT)
		                                   .setOwnerAddress(LOCAL_MSIG_ROOT.address())
		                                   .setName("Test Token")
		                                   .setSymbol("TST")
		                                   .setDecimals(6)
		                                   .setRandomNonce(new Random().nextInt())
		                                   .build(SDK_LOCAL, GIVER_LOCAL, CurrencyUnit.VALUE(EVER, "1.3"));

		LOCAL_TIP3_ROOT.deployWallet(LOCAL_MSIG_WALLET1.address(), CurrencyUnit.VALUE(EVER, "0.5"))
		               .sendFrom(LOCAL_MSIG_ROOT, CurrencyUnit.VALUE(EVER, "1.5"));

		LOCAL_TIP3_ROOT.deployWallet(LOCAL_MSIG_WALLET2.address(), CurrencyUnit.VALUE(EVER, "0.5"))
		               .sendFrom(LOCAL_MSIG_ROOT, CurrencyUnit.VALUE(EVER, "1.5"));

		LOCAL_TIP3_WALLET1 = new TIP3TokenWalletContract(SDK_LOCAL,
		                                                 LOCAL_TIP3_ROOT.walletOf(LOCAL_MSIG_WALLET1.address())
		                                                                .get()
		                                                                .value0()
		                                                                .toString());
		LOCAL_TIP3_WALLET2 = new TIP3TokenWalletContract(SDK_LOCAL,
		                                                 LOCAL_TIP3_ROOT.walletOf(LOCAL_MSIG_WALLET2.address())
		                                                                .get()
		                                                                .value0()
		                                                                .toString());
	}

	public static Credentials RNG_KEYS() throws EverSdkException {
		return Credentials.ofRandom(SDK_EMPTY);
	}

	public static Seed RNG_SEED() throws EverSdkException {
		return Seed.ofRandom(Env.SDK_EMPTY);
	}
}
