package tech.deplant.java4ever.unit;

import com.fasterxml.jackson.databind.node.NullNode;
import com.yegor256.OnlineMeans;
import com.yegor256.WeAreOnline;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.*;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class CryptoTests {

	private static final Logger log = LoggerFactory.getLogger(CryptoTests.class);

	@BeforeAll
	public static void loadSdk() {
		TestEnv.loadEverSdk();
	}

	@ParameterizedTest
	@ValueSource(ints = {12, 24})
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void mnemonic_from_random_should_pass_regexp_for_word_count(int words) throws EverSdkException, ExecutionException, InterruptedException {
		int ctxId = TestEnv.newContext();
		Pattern pattern = Pattern.compile("[\\w-]+");
		Matcher matcher = pattern.matcher(Crypto.mnemonicFromRandom(ctxId, Crypto.MnemonicDictionary.English, words)
		                                        .get()
		                                        .phrase());
		int count = 0;
		while (matcher.find())
			count++;
		assertEquals(words, count);
	}

	@Test
	public void create_app_signing_box_and_get_pk() throws EverSdkException {
		int ctxId = TestEnv.newContext();
		int ctxId2 = TestEnv.newContext();
		var keys = new Crypto.KeyPair("23d11faa84bf49596c9571706763605ad5ff644dc79a39c9c5a675208402a6d8","629f9e557069d98cf841960a5e0658807038a7fa70f37500dc920d7290964958");
		long signingBoxHandle = EverSdk.await(Crypto.registerSigningBox(ctxId, new AppSigningBox() {
			@Override
			public String getPublicKey() {
				return keys.publicKey();
			}

			@Override
			public String sign(String unsigned) {
				try {
					return EverSdk.await(Crypto.sign(ctxId2, unsigned, keys)).signature();
				} catch (EverSdkException e) {
					throw new RuntimeException(e);
				}
			}
		})).handle();
		String signingBoxPubkey = EverSdk.await(Crypto.signingBoxGetPublicKey(ctxId, new Crypto.RegisteredSigningBox(signingBoxHandle))).pubkey();
		assertEquals(keys.publicKey(),signingBoxPubkey);
	}

	@Test
	public void create_app_signing_box_and_sign() throws EverSdkException {
		int ctxId = TestEnv.newContext();
		int ctxId2 = TestEnv.newContext();
		var keys = new Crypto.KeyPair("23d11faa84bf49596c9571706763605ad5ff644dc79a39c9c5a675208402a6d8","629f9e557069d98cf841960a5e0658807038a7fa70f37500dc920d7290964958");
		long signingBoxHandle = EverSdk.await(Crypto.registerSigningBox(ctxId, new AppSigningBox() {
			@Override
			public String getPublicKey() {
				return keys.publicKey();
			}

			@Override
			public String sign(String unsigned) {
				try {
					return EverSdk.await(Crypto.sign(ctxId2, unsigned, keys)).signature();
				} catch (EverSdkException e) {
					throw new RuntimeException(e);
				}
			}
		})).handle();
		var signer = new Abi.Signer.SigningBox(signingBoxHandle);
		System.out.println(EverSdk.await(Crypto.signingBoxSign(ctxId, signingBoxHandle, "te6ccgEBAQEAAgAAAA=="))
		                          .signature());
	}


	@Test
	public void create_app_encryption_box_and_get() throws EverSdkException {
		int ctxId = TestEnv.newContext();
		int ctxId2 = TestEnv.newContext();
		var keys = new Crypto.KeyPair("23d11faa84bf49596c9571706763605ad5ff644dc79a39c9c5a675208402a6d8",
		                              "629f9e557069d98cf841960a5e0658807038a7fa70f37500dc920d7290964958");
		long boxHandle = EverSdk.await(Crypto.registerEncryptionBox(ctxId, new AppEncryptionBox() {
			@Override
			public Crypto.EncryptionBoxInfo getInfo() {
				return new Crypto.EncryptionBoxInfo("", "", null, null);
			}

			@Override
			public String encrypt(String data) {
				return "te6ccgEBAQEAAgAAAA==";
			}

			@Override
			public String decrypt(String data) {
				return "te6ccgEBAQEAAgAAAA==";
			}
		})).handle();
		System.out.println(EverSdk.await(Crypto.encryptionBoxGetInfo(ctxId, boxHandle)).info());
	}

}
