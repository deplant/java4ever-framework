package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.CompletableFuture;

/**
 * <strong>Crypto</strong>
 * Contains methods of "crypto" module of EVER-SDK API
 * <p>
 * Crypto functions. 
 * @version 1.45.0
 */
public final class Crypto {
  /**
   * Performs prime factorization – decomposition of a composite number
   * into a product of smaller prime integers (factors).
   * See [https://en.wikipedia.org/wiki/Integer_factorization] Integer factorization
   *
   * @param composite  Hexadecimal representation of u64 composite number.
   */
  public static CompletableFuture<Crypto.ResultOfFactorize> factorize(int ctxId, String composite)
      throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.factorize", new Crypto.ParamsOfFactorize(composite), Crypto.ResultOfFactorize.class);
  }

  /**
   * Performs modular exponentiation for big integers (`base`^`exponent` mod `modulus`).
   * See [https://en.wikipedia.org/wiki/Modular_exponentiation] Modular exponentiation
   *
   * @param base  `base` argument of calculation.
   * @param exponent  `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  public static CompletableFuture<Crypto.ResultOfModularPower> modularPower(int ctxId, String base,
      String exponent, String modulus) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.modular_power", new Crypto.ParamsOfModularPower(base, exponent, modulus), Crypto.ResultOfModularPower.class);
  }

  /**
   *  Calculates CRC16 using TON algorithm.
   *
   * @param data Encoded with `base64`. Input data for CRC calculation.
   */
  public static CompletableFuture<Crypto.ResultOfTonCrc16> tonCrc16(int ctxId, String data) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.ton_crc16", new Crypto.ParamsOfTonCrc16(data), Crypto.ResultOfTonCrc16.class);
  }

  /**
   *  Generates random byte array of the specified length and returns it in `base64` format
   *
   * @param length  Size of random byte array.
   */
  public static CompletableFuture<Crypto.ResultOfGenerateRandomBytes> generateRandomBytes(int ctxId,
      Long length) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.generate_random_bytes", new Crypto.ParamsOfGenerateRandomBytes(length), Crypto.ResultOfGenerateRandomBytes.class);
  }

  /**
   *  Converts public key to ton safe_format
   *
   * @param publicKey  Public key - 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfConvertPublicKeyToTonSafeFormat> convertPublicKeyToTonSafeFormat(
      int ctxId, String publicKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.convert_public_key_to_ton_safe_format", new Crypto.ParamsOfConvertPublicKeyToTonSafeFormat(publicKey), Crypto.ResultOfConvertPublicKeyToTonSafeFormat.class);
  }

  /**
   *  Generates random ed25519 key pair.
   */
  public static CompletableFuture<Crypto.KeyPair> generateRandomSignKeys(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.generate_random_sign_keys", null, Crypto.KeyPair.class);
  }

  /**
   *  Signs a data using the provided keys.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param keys  Sign keys.
   */
  public static CompletableFuture<Crypto.ResultOfSign> sign(int ctxId, String unsigned,
      Crypto.KeyPair keys) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.sign", new Crypto.ParamsOfSign(unsigned, keys), Crypto.ResultOfSign.class);
  }

  /**
   *  Verifies signed data using the provided public key. Raises error if verification is failed.
   *
   * @param signed  Signed data that must be verified encoded in `base64`.
   * @param publicKey  Signer's public key - 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfVerifySignature> verifySignature(int ctxId,
      String signed, @JsonProperty("public") String publicKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.verify_signature", new Crypto.ParamsOfVerifySignature(signed, publicKey), Crypto.ResultOfVerifySignature.class);
  }

  /**
   *  Calculates SHA256 hash of the specified data.
   *
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public static CompletableFuture<Crypto.ResultOfHash> sha256(int ctxId, String data) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.sha256", new Crypto.ParamsOfHash(data), Crypto.ResultOfHash.class);
  }

  /**
   *  Calculates SHA512 hash of the specified data.
   *
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public static CompletableFuture<Crypto.ResultOfHash> sha512(int ctxId, String data) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.sha512", new Crypto.ParamsOfHash(data), Crypto.ResultOfHash.class);
  }

  /**
   * Derives key from `password` and `key` using `scrypt` algorithm.
   * See [https://en.wikipedia.org/wiki/Scrypt].
   *
   * # Arguments
   * - `log_n` - The log2 of the Scrypt parameter `N`
   * - `r` - The Scrypt parameter `r`
   * - `p` - The Scrypt parameter `p`
   * # Conditions
   * - `log_n` must be less than `64`
   * - `r` must be greater than `0` and less than or equal to `4294967295`
   * - `p` must be greater than `0` and less than `4294967295`
   * # Recommended values sufficient for most use-cases
   * - `log_n = 15` (`n = 32768`)
   * - `r = 8`
   * - `p = 1` Perform `scrypt` encryption
   *
   * @param password  The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt  Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param logN  CPU/memory cost parameter
   * @param r  The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p  Parallelization parameter.
   * @param dkLen  Intended output length in octets of the derived key.
   */
  public static CompletableFuture<Crypto.ResultOfScrypt> scrypt(int ctxId, String password,
      String salt, Integer logN, Long r, Long p, Long dkLen) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.scrypt", new Crypto.ParamsOfScrypt(password, salt, logN, r, p, dkLen), Crypto.ResultOfScrypt.class);
  }

  /**
   * **NOTE:** In the result the secret key is actually the concatenation
   * of secret and public keys (128 symbols hex string) by design of [NaCL](http://nacl.cr.yp.to/sign.html).
   * See also [the stackexchange question](https://crypto.stackexchange.com/questions/54353/). Generates a key pair for signing from the secret key
   *
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.KeyPair> naclSignKeypairFromSecretKey(int ctxId,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_sign_keypair_from_secret_key", new Crypto.ParamsOfNaclSignKeyPairFromSecret(secretKey), Crypto.KeyPair.class);
  }

  /**
   *  Signs data using the signer's secret key.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public static CompletableFuture<Crypto.ResultOfNaclSign> naclSign(int ctxId, String unsigned,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_sign", new Crypto.ParamsOfNaclSign(unsigned, secretKey), Crypto.ResultOfNaclSign.class);
  }

  /**
   * Verifies the signature in `signed` using the signer's public key `public`
   * and returns the message `unsigned`.
   *
   * If the signature fails verification, crypto_sign_open raises an exception. Verifies the signature and returns the unsigned message
   *
   * @param signed Encoded with `base64`. Signed data that must be unsigned.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfNaclSignOpen> naclSignOpen(int ctxId,
      String signed, @JsonProperty("public") String publicKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_sign_open", new Crypto.ParamsOfNaclSignOpen(signed, publicKey), Crypto.ResultOfNaclSignOpen.class);
  }

  /**
   * Signs the message `unsigned` using the secret key `secret`
   * and returns a signature `signature`. Signs the message using the secret key and returns a signature.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public static CompletableFuture<Crypto.ResultOfNaclSignDetached> naclSignDetached(int ctxId,
      String unsigned, @JsonProperty("secret") String secretKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_sign_detached", new Crypto.ParamsOfNaclSign(unsigned, secretKey), Crypto.ResultOfNaclSignDetached.class);
  }

  /**
   *  Verifies the signature with public key and `unsigned` data.
   *
   * @param unsigned Encoded with `base64`. Unsigned data that must be verified.
   * @param signature Encoded with `hex`. Signature that must be verified.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string.
   */
  public static CompletableFuture<Crypto.ResultOfNaclSignDetachedVerify> naclSignDetachedVerify(
      int ctxId, String unsigned, String signature, @JsonProperty("public") String publicKey) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_sign_detached_verify", new Crypto.ParamsOfNaclSignDetachedVerify(unsigned, signature, publicKey), Crypto.ResultOfNaclSignDetachedVerify.class);
  }

  /**
   *  Generates a random NaCl key pair
   */
  public static CompletableFuture<Crypto.KeyPair> naclBoxKeypair(int ctxId) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_box_keypair", null, Crypto.KeyPair.class);
  }

  /**
   *  Generates key pair from a secret key
   *
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.KeyPair> naclBoxKeypairFromSecretKey(int ctxId,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_box_keypair_from_secret_key", new Crypto.ParamsOfNaclBoxKeyPairFromSecret(secretKey), Crypto.KeyPair.class);
  }

  /**
   * Encrypt and authenticate a message using the senders secret key, the receivers public
   * key, and a nonce. Public key authenticated encryption
   *
   * @param decrypted  Data that must be encrypted encoded in `base64`.
   * @param nonce  Nonce, encoded in `hex`
   * @param theirPublic  Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfNaclBox> naclBox(int ctxId, String decrypted,
      String nonce, String theirPublic, @JsonProperty("secret") String secretKey) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_box", new Crypto.ParamsOfNaclBox(decrypted, nonce, theirPublic, secretKey), Crypto.ResultOfNaclBox.class);
  }

  /**
   *  Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.
   *
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce
   * @param theirPublic  Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfNaclBoxOpen> naclBoxOpen(int ctxId,
      String encrypted, String nonce, String theirPublic, @JsonProperty("secret") String secretKey)
      throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_box_open", new Crypto.ParamsOfNaclBoxOpen(encrypted, nonce, theirPublic, secretKey), Crypto.ResultOfNaclBoxOpen.class);
  }

  /**
   *  Encrypt and authenticate message using nonce and secret key.
   *
   * @param decrypted Encoded with `base64`. Data that must be encrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfNaclBox> naclSecretBox(int ctxId, String decrypted,
      String nonce, String key) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_secret_box", new Crypto.ParamsOfNaclSecretBox(decrypted, nonce, key), Crypto.ResultOfNaclBox.class);
  }

  /**
   *  Decrypts and verifies cipher text using `nonce` and secret `key`.
   *
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static CompletableFuture<Crypto.ResultOfNaclBoxOpen> naclSecretBoxOpen(int ctxId,
      String encrypted, String nonce, String key) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.nacl_secret_box_open", new Crypto.ParamsOfNaclSecretBoxOpen(encrypted, nonce, key), Crypto.ResultOfNaclBoxOpen.class);
  }

  /**
   *  Prints the list of words from the specified dictionary
   *
   * @param dictionary  Dictionary identifier
   */
  public static CompletableFuture<Crypto.ResultOfMnemonicWords> mnemonicWords(int ctxId,
      Crypto.MnemonicDictionary dictionary) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.mnemonic_words", new Crypto.ParamsOfMnemonicWords(dictionary), Crypto.ResultOfMnemonicWords.class);
  }

  /**
   * Generates a random mnemonic from the specified dictionary and word count Generates a random mnemonic
   *
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static CompletableFuture<Crypto.ResultOfMnemonicFromRandom> mnemonicFromRandom(int ctxId,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.mnemonic_from_random", new Crypto.ParamsOfMnemonicFromRandom(dictionary, wordCount), Crypto.ResultOfMnemonicFromRandom.class);
  }

  /**
   *  Generates mnemonic from pre-generated entropy
   *
   * @param entropy Hex encoded. Entropy bytes.
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static CompletableFuture<Crypto.ResultOfMnemonicFromEntropy> mnemonicFromEntropy(int ctxId,
      String entropy, Crypto.MnemonicDictionary dictionary, Integer wordCount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.mnemonic_from_entropy", new Crypto.ParamsOfMnemonicFromEntropy(entropy, dictionary, wordCount), Crypto.ResultOfMnemonicFromEntropy.class);
  }

  /**
   * The phrase supplied will be checked for word length and validated according to the checksum
   * specified in BIP0039. Validates a mnemonic phrase
   *
   * @param phrase  Phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static CompletableFuture<Crypto.ResultOfMnemonicVerify> mnemonicVerify(int ctxId,
      String phrase, Crypto.MnemonicDictionary dictionary, Integer wordCount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.mnemonic_verify", new Crypto.ParamsOfMnemonicVerify(phrase, dictionary, wordCount), Crypto.ResultOfMnemonicVerify.class);
  }

  /**
   * Validates the seed phrase, generates master key and then derives
   * the key pair from the master key and the specified path Derives a key pair for signing from the seed phrase
   *
   * @param phrase  Phrase
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static CompletableFuture<Crypto.KeyPair> mnemonicDeriveSignKeys(int ctxId, String phrase,
      String path, Crypto.MnemonicDictionary dictionary, Integer wordCount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.mnemonic_derive_sign_keys", new Crypto.ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, wordCount), Crypto.KeyPair.class);
  }

  /**
   *  Generates an extended master private key that will be the root for all the derived keys
   *
   * @param phrase  String with seed phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static CompletableFuture<Crypto.ResultOfHDKeyXPrvFromMnemonic> hdkeyXprvFromMnemonic(
      int ctxId, String phrase, Crypto.MnemonicDictionary dictionary, Integer wordCount) throws
      EverSdkException {
    return EverSdk.async(ctxId, "crypto.hdkey_xprv_from_mnemonic", new Crypto.ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, wordCount), Crypto.ResultOfHDKeyXPrvFromMnemonic.class);
  }

  /**
   *  Returns extended private key derived from the specified extended private key and child index
   *
   * @param xprv  Serialized extended private key
   * @param childIndex  Child index (see BIP-0032)
   * @param hardened  Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  public static CompletableFuture<Crypto.ResultOfHDKeyDeriveFromXPrv> hdkeyDeriveFromXprv(int ctxId,
      String xprv, Long childIndex, Boolean hardened) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.hdkey_derive_from_xprv", new Crypto.ParamsOfHDKeyDeriveFromXPrv(xprv, childIndex, hardened), Crypto.ResultOfHDKeyDeriveFromXPrv.class);
  }

  /**
   *  Derives the extended private key from the specified key and path
   *
   * @param xprv  Serialized extended private key
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  public static CompletableFuture<Crypto.ResultOfHDKeyDeriveFromXPrvPath> hdkeyDeriveFromXprvPath(
      int ctxId, String xprv, String path) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.hdkey_derive_from_xprv_path", new Crypto.ParamsOfHDKeyDeriveFromXPrvPath(xprv, path), Crypto.ResultOfHDKeyDeriveFromXPrvPath.class);
  }

  /**
   *  Extracts the private key from the serialized extended private key
   *
   * @param xprv  Serialized extended private key
   */
  public static CompletableFuture<Crypto.ResultOfHDKeySecretFromXPrv> hdkeySecretFromXprv(int ctxId,
      String xprv) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.hdkey_secret_from_xprv", new Crypto.ParamsOfHDKeySecretFromXPrv(xprv), Crypto.ResultOfHDKeySecretFromXPrv.class);
  }

  /**
   *  Extracts the public key from the serialized extended private key
   *
   * @param xprv  Serialized extended private key
   */
  public static CompletableFuture<Crypto.ResultOfHDKeyPublicFromXPrv> hdkeyPublicFromXprv(int ctxId,
      String xprv) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.hdkey_public_from_xprv", new Crypto.ParamsOfHDKeyPublicFromXPrv(xprv), Crypto.ResultOfHDKeyPublicFromXPrv.class);
  }

  /**
   *  Performs symmetric `chacha20` encryption.
   *
   * @param data Must be encoded with `base64`. Source data to be encrypted or decrypted.
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static CompletableFuture<Crypto.ResultOfChaCha20> chacha20(int ctxId, String data,
      String key, String nonce) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.chacha20", new Crypto.ParamsOfChaCha20(data, key, nonce), Crypto.ResultOfChaCha20.class);
  }

  /**
   * Crypto Box is a root crypto object, that encapsulates some secret (seed phrase usually)
   * in encrypted form and acts as a factory for all crypto primitives used in SDK:
   * keys for signing and encryption, derived from this secret.
   *
   * Crypto Box encrypts original Seed Phrase with salt and password that is retrieved
   * from `password_provider` callback, implemented on Application side.
   *
   * When used, decrypted secret shows up in core library's memory for a very short period
   * of time and then is immediately overwritten with zeroes. Creates a Crypto Box instance.
   *
   * @param secretEncryptionSalt  Salt used for secret encryption. For example, a mobile device can use device ID as salt.
   * @param secretKey  Cryptobox secret
   */
  public static CompletableFuture<Crypto.RegisteredCryptoBox> createCryptoBox(int ctxId,
      String secretEncryptionSalt, @JsonProperty("secret") Crypto.CryptoBoxSecret secretKey,
      AppPasswordProvider appObject) throws EverSdkException {
    return EverSdk.asyncAppObject(ctxId, "crypto.create_crypto_box", new Crypto.ParamsOfCreateCryptoBox(secretEncryptionSalt, secretKey), Crypto.RegisteredCryptoBox.class, appObject);
  }

  /**
   *  Removes Crypto Box. Clears all secret data.
   */
  public static void removeCryptoBox(int ctxId, Crypto.RegisteredCryptoBox params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "crypto.remove_crypto_box", params);
  }

  /**
   *  Get Crypto Box Info. Used to get `encrypted_secret` that should be used for all the cryptobox initializations except the first one.
   */
  public static CompletableFuture<Crypto.ResultOfGetCryptoBoxInfo> getCryptoBoxInfo(int ctxId,
      Crypto.RegisteredCryptoBox params) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.get_crypto_box_info", params, Crypto.ResultOfGetCryptoBoxInfo.class);
  }

  /**
   * Attention! Store this data in your application for a very short period of time and overwrite it with zeroes ASAP. Get Crypto Box Seed Phrase.
   */
  public static CompletableFuture<Crypto.ResultOfGetCryptoBoxSeedPhrase> getCryptoBoxSeedPhrase(
      int ctxId, Crypto.RegisteredCryptoBox params) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.get_crypto_box_seed_phrase", params, Crypto.ResultOfGetCryptoBoxSeedPhrase.class);
  }

  /**
   *  Get handle of Signing Box derived from Crypto Box.
   *
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param secretLifetime  Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set.
   */
  public static CompletableFuture<Crypto.RegisteredSigningBox> getSigningBoxFromCryptoBox(int ctxId,
      Long handle, String hdpath, Long secretLifetime) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.get_signing_box_from_crypto_box", new Crypto.ParamsOfGetSigningBoxFromCryptoBox(handle, hdpath, secretLifetime), Crypto.RegisteredSigningBox.class);
  }

  /**
   * Derives encryption keypair from cryptobox secret and hdpath and
   * stores it in cache for `secret_lifetime`
   * or until explicitly cleared by `clear_crypto_box_secret_cache` method.
   * If `secret_lifetime` is not specified - overwrites encryption secret with zeroes immediately after
   * encryption operation. Gets Encryption Box from Crypto Box.
   *
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param algorithm  Encryption algorithm.
   * @param secretLifetime  Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set.
   */
  public static CompletableFuture<Crypto.RegisteredEncryptionBox> getEncryptionBoxFromCryptoBox(
      int ctxId, Long handle, String hdpath, Crypto.BoxEncryptionAlgorithm algorithm,
      Long secretLifetime) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.get_encryption_box_from_crypto_box", new Crypto.ParamsOfGetEncryptionBoxFromCryptoBox(handle, hdpath, algorithm, secretLifetime), Crypto.RegisteredEncryptionBox.class);
  }

  /**
   *  Removes cached secrets (overwrites with zeroes) from all signing and encryption boxes, derived from crypto box.
   */
  public static void clearCryptoBoxSecretCache(int ctxId, Crypto.RegisteredCryptoBox params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "crypto.clear_crypto_box_secret_cache", params);
  }

  /**
   *  Register an application implemented signing box.
   */
  public static CompletableFuture<Crypto.RegisteredSigningBox> registerSigningBox(int ctxId,
      AppSigningBox appObject) throws EverSdkException {
    return EverSdk.asyncAppObject(ctxId, "crypto.register_signing_box", null, Crypto.RegisteredSigningBox.class, appObject);
  }

  /**
   *  Creates a default signing box implementation.
   */
  public static CompletableFuture<Crypto.RegisteredSigningBox> getSigningBox(int ctxId,
      Crypto.KeyPair params) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.get_signing_box", params, Crypto.RegisteredSigningBox.class);
  }

  /**
   *  Returns public key of signing key pair.
   */
  public static CompletableFuture<Crypto.ResultOfSigningBoxGetPublicKey> signingBoxGetPublicKey(
      int ctxId, Crypto.RegisteredSigningBox params) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.signing_box_get_public_key", params, Crypto.ResultOfSigningBoxGetPublicKey.class);
  }

  /**
   *  Returns signed user data.
   *
   * @param signingBox  Signing Box handle.
   * @param unsigned Must be encoded with `base64`. Unsigned user data.
   */
  public static CompletableFuture<Crypto.ResultOfSigningBoxSign> signingBoxSign(int ctxId,
      Long signingBox, String unsigned) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.signing_box_sign", new Crypto.ParamsOfSigningBoxSign(signingBox, unsigned), Crypto.ResultOfSigningBoxSign.class);
  }

  /**
   *  Removes signing box from SDK.
   */
  public static void removeSigningBox(int ctxId, Crypto.RegisteredSigningBox params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "crypto.remove_signing_box", params);
  }

  /**
   *  Register an application implemented encryption box.
   */
  public static CompletableFuture<Crypto.RegisteredEncryptionBox> registerEncryptionBox(int ctxId,
      AppEncryptionBox appObject) throws EverSdkException {
    return EverSdk.asyncAppObject(ctxId, "crypto.register_encryption_box", null, Crypto.RegisteredEncryptionBox.class, appObject);
  }

  /**
   *  Removes encryption box from SDK
   */
  public static void removeEncryptionBox(int ctxId, Crypto.RegisteredEncryptionBox params) throws
      EverSdkException {
    EverSdk.asyncVoid(ctxId, "crypto.remove_encryption_box", params);
  }

  /**
   *  Queries info from the given encryption box
   *
   * @param encryptionBox  Encryption box handle
   */
  public static CompletableFuture<Crypto.ResultOfEncryptionBoxGetInfo> encryptionBoxGetInfo(
      int ctxId, Long encryptionBox) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.encryption_box_get_info", new Crypto.ParamsOfEncryptionBoxGetInfo(encryptionBox), Crypto.ResultOfEncryptionBoxGetInfo.class);
  }

  /**
   * Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data. Encrypts data using given encryption box Note.
   *
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be encrypted, encoded in Base64
   */
  public static CompletableFuture<Crypto.ResultOfEncryptionBoxEncrypt> encryptionBoxEncrypt(
      int ctxId, Long encryptionBox, String data) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.encryption_box_encrypt", new Crypto.ParamsOfEncryptionBoxEncrypt(encryptionBox, data), Crypto.ResultOfEncryptionBoxEncrypt.class);
  }

  /**
   * Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data. Decrypts data using given encryption box Note.
   *
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be decrypted, encoded in Base64
   */
  public static CompletableFuture<Crypto.ResultOfEncryptionBoxDecrypt> encryptionBoxDecrypt(
      int ctxId, Long encryptionBox, String data) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.encryption_box_decrypt", new Crypto.ParamsOfEncryptionBoxDecrypt(encryptionBox, data), Crypto.ResultOfEncryptionBoxDecrypt.class);
  }

  /**
   *  Creates encryption box with specified algorithm
   *
   * @param algorithm  Encryption algorithm specifier including cipher parameters (key, IV, etc)
   */
  public static CompletableFuture<Crypto.RegisteredEncryptionBox> createEncryptionBox(int ctxId,
      Crypto.EncryptionAlgorithm algorithm) throws EverSdkException {
    return EverSdk.async(ctxId, "crypto.create_encryption_box", new Crypto.ParamsOfCreateEncryptionBox(algorithm), Crypto.RegisteredEncryptionBox.class);
  }

  /**
   * @param modularPower  Result of modular exponentiation
   */
  public record ResultOfModularPower(String modularPower) {
  }

  /**
   * @param theirPublic Must be encoded with `hex`. 256-bit key.
   * @param secretKey Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public record NaclBoxParamsEB(String theirPublic, @JsonProperty("secret") String secretKey,
      String nonce) {
  }

  /**
   *  Signing box callbacks.
   */
  public sealed interface ParamsOfAppSigningBox {
    /**
     *  Get signing box public key
     */
    record GetPublicKey() implements ParamsOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "GetPublicKey";
      }
    }

    /**
     *  Sign data
     *
     * @param unsigned  Data to sign encoded as base64
     */
    record Sign(String unsigned) implements ParamsOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "Sign";
      }
    }
  }

  /**
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be encrypted, encoded in Base64
   */
  public record ParamsOfEncryptionBoxEncrypt(Long encryptionBox, String data) {
  }

  /**
   * @param data  Decrypted data, encoded in Base64.
   */
  public record ResultOfEncryptionBoxDecrypt(String data) {
  }

  /**
   * @param factors  Two factors of composite or empty if composite can't be factorized.
   */
  public record ResultOfFactorize(String[] factors) {
  }

  /**
   *  Crypto Box Secret.
   */
  public sealed interface CryptoBoxSecret {
    /**
     * This type should be used upon the first wallet initialization, all further initializations
     * should use `EncryptedSecret` type instead.
     *
     * Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side. Creates Crypto Box from a random seed phrase. This option can be used if a developer doesn't want the seed phrase to leave the core library's memory, where it is stored encrypted.
     */
    record RandomSeedPhrase(Crypto.MnemonicDictionary dictionary,
        Integer wordcount) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "RandomSeedPhrase";
      }
    }

    /**
     * This type should be used only upon the first wallet initialization, all further
     * initializations should use `EncryptedSecret` type instead.
     *
     * Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side. Restores crypto box instance from an existing seed phrase. This type should be used when Crypto Box is initialized from a seed phrase, entered by a user.
     */
    record PredefinedSeedPhrase(String phrase, Crypto.MnemonicDictionary dictionary,
        Integer wordcount) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "PredefinedSeedPhrase";
      }
    }

    /**
     * It is an object, containing seed phrase or private key, encrypted with
     * `secret_encryption_salt` and password from `password_provider`.
     *
     * Note that if you want to change salt or password provider, then you need to reinitialize
     * the wallet with `PredefinedSeedPhrase`, then get `EncryptedSecret` via `get_crypto_box_info`,
     * store it somewhere, and only after that initialize the wallet with `EncryptedSecret` type. Use this type for wallet reinitializations, when you already have `encrypted_secret` on hands. To get `encrypted_secret`, use `get_crypto_box_info` function after you initialized your crypto box for the first time.
     *
     * @param encryptedSecret  It is an object, containing encrypted seed phrase or private key (now we support only seed phrase).
     */
    record EncryptedSecret(String encryptedSecret) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "EncryptedSecret";
      }
    }
  }

  /**
   * @param signed  Signed data that must be verified encoded in `base64`.
   * @param publicKey  Signer's public key - 64 symbols hex string
   */
  public record ParamsOfVerifySignature(String signed, @JsonProperty("public") String publicKey) {
  }

  /**
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public record ChaCha20ParamsEB(String key, String nonce) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   */
  public record ParamsOfConvertPublicKeyToTonSafeFormat(String publicKey) {
  }

  /**
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce
   * @param theirPublic  Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclBoxOpen(String encrypted, String nonce, String theirPublic,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param entropy Hex encoded. Entropy bytes.
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public record ParamsOfMnemonicFromEntropy(String entropy, Crypto.MnemonicDictionary dictionary,
      Integer wordCount) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   */
  public record ResultOfHDKeyPublicFromXPrv(@JsonProperty("public") String publicKey) {
  }

  /**
   * @param base  `base` argument of calculation.
   * @param exponent  `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  public record ParamsOfModularPower(String base, String exponent, String modulus) {
  }

  /**
   * @param phrase  String of mnemonic words
   */
  public record ResultOfMnemonicFromRandom(String phrase) {
  }

  /**
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public record ParamsOfNaclSign(String unsigned, @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param secretKey  Private key - 64 symbols hex string
   */
  public record ResultOfHDKeySecretFromXPrv(@JsonProperty("secret") String secretKey) {
  }

  /**
   * @param signed  Signed data, encoded in `base64`.
   */
  public record ResultOfNaclSign(String signed) {
  }

  public enum CipherMode {
    CBC,

    CFB,

    CTR,

    ECB,

    OFB
  }

  public enum CryptoErrorCode {
    InvalidPublicKey(100),

    InvalidSecretKey(101),

    InvalidKey(102),

    InvalidFactorizeChallenge(106),

    InvalidBigInt(107),

    ScryptFailed(108),

    InvalidKeySize(109),

    NaclSecretBoxFailed(110),

    NaclBoxFailed(111),

    NaclSignFailed(112),

    Bip39InvalidEntropy(113),

    Bip39InvalidPhrase(114),

    Bip32InvalidKey(115),

    Bip32InvalidDerivePath(116),

    Bip39InvalidDictionary(117),

    Bip39InvalidWordCount(118),

    MnemonicGenerationFailed(119),

    MnemonicFromEntropyFailed(120),

    SigningBoxNotRegistered(121),

    InvalidSignature(122),

    EncryptionBoxNotRegistered(123),

    InvalidIvSize(124),

    UnsupportedCipherMode(125),

    CannotCreateCipher(126),

    EncryptDataError(127),

    DecryptDataError(128),

    IvRequired(129),

    CryptoBoxNotRegistered(130),

    InvalidCryptoBoxType(131),

    CryptoBoxSecretSerializationError(132),

    CryptoBoxSecretDeserializationError(133),

    InvalidNonceSize(134);

    private final Integer value;

    CryptoErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public record ChaCha20ParamsCB(String nonce) {
  }

  /**
   * @param unsigned  Unsigned data, encoded in `base64`.
   */
  public record ResultOfNaclSignOpen(String unsigned) {
  }

  /**
   *  Returning values from signing box callbacks.
   */
  public sealed interface ResultOfAppEncryptionBox {
    /**
     *  Result of getting encryption box info
     */
    record GetInfo(Crypto.EncryptionBoxInfo info) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "GetInfo";
      }
    }

    /**
     *  Result of encrypting data
     *
     * @param data  Encrypted data, encoded in Base64
     */
    record Encrypt(String data) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Encrypt";
      }
    }

    /**
     *  Result of decrypting data
     *
     * @param data  Decrypted data, encoded in Base64
     */
    record Decrypt(String data) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Decrypt";
      }
    }
  }

  public record AesParamsEB(Crypto.CipherMode mode, String key, String iv) {
  }

  /**
   * @param pubkey Encoded with hex Public key of signing box.
   */
  public record ResultOfSigningBoxGetPublicKey(String pubkey) {
  }

  /**
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclBoxKeyPairFromSecret(@JsonProperty("secret") String secretKey) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   * @param secretKey  Private key - u64 symbols hex string
   */
  public record KeyPair(@JsonProperty("public") String publicKey,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be decrypted, encoded in Base64
   */
  public record ParamsOfEncryptionBoxDecrypt(Long encryptionBox, String data) {
  }

  /**
   * @param decrypted Encoded with `base64`. Data that must be encrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclSecretBox(String decrypted, String nonce, String key) {
  }

  /**
   * @param theirPublic Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public record NaclBoxParamsCB(String theirPublic, String nonce) {
  }

  /**
   * @param valid  Flag indicating if the mnemonic is valid or not
   */
  public record ResultOfMnemonicVerify(Boolean valid) {
  }

  public record ResultOfGetCryptoBoxSeedPhrase(String phrase, Crypto.MnemonicDictionary dictionary,
      Integer wordcount) {
  }

  /**
   * @param signed  Signed data combined with signature encoded in `base64`.
   * @param signature  Signature encoded in `hex`.
   */
  public record ResultOfSign(String signed, String signature) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public record ParamsOfHDKeyPublicFromXPrv(String xprv) {
  }

  public sealed interface BoxEncryptionAlgorithm {
    record ChaCha20(Crypto.ChaCha20ParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "ChaCha20";
      }
    }

    record NaclBox(Crypto.NaclBoxParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclBox";
      }
    }

    record NaclSecretBox(Crypto.NaclSecretBoxParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclSecretBox";
      }
    }
  }

  public enum MnemonicDictionary {
    Ton(0),

    English(1),

    ChineseSimplified(2),

    ChineseTraditional(3),

    French(4),

    Italian(5),

    Japanese(6),

    Korean(7),

    Spanish(8);

    private final Integer value;

    MnemonicDictionary(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param data Must be encoded with `base64`. Source data to be encrypted or decrypted.
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public record ParamsOfChaCha20(String data, String key, String nonce) {
  }

  /**
   * @param signingBox  Signing Box handle.
   * @param unsigned Must be encoded with `base64`. Unsigned user data.
   */
  public record ParamsOfSigningBoxSign(Long signingBox, String unsigned) {
  }

  /**
   * @param bytes  Generated bytes encoded in `base64`.
   */
  public record ResultOfGenerateRandomBytes(String bytes) {
  }

  /**
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param algorithm  Encryption algorithm.
   * @param secretLifetime  Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set.
   */
  public record ParamsOfGetEncryptionBoxFromCryptoBox(Long handle, String hdpath,
      Crypto.BoxEncryptionAlgorithm algorithm, Long secretLifetime) {
  }

  /**
   * @param signed Encoded with `base64`. Signed data that must be unsigned.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclSignOpen(String signed, @JsonProperty("public") String publicKey) {
  }

  /**
   * @param phrase  Phrase
   */
  public record ResultOfMnemonicFromEntropy(String phrase) {
  }

  /**
   * @param algorithm  Encryption algorithm specifier including cipher parameters (key, IV, etc)
   */
  public record ParamsOfCreateEncryptionBox(Crypto.EncryptionAlgorithm algorithm) {
  }

  /**
   * @param phrase  Phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public record ParamsOfMnemonicVerify(String phrase, Crypto.MnemonicDictionary dictionary,
      Integer wordCount) {
  }

  /**
   * @param xprv  Serialized extended private key
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  public record ParamsOfHDKeyDeriveFromXPrvPath(String xprv, String path) {
  }

  /**
   * @param key Encoded with `hex`. Derived key.
   */
  public record ResultOfScrypt(String key) {
  }

  /**
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param secretLifetime  Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set.
   */
  public record ParamsOfGetSigningBoxFromCryptoBox(Long handle, String hdpath,
      Long secretLifetime) {
  }

  /**
   * @param handle  Handle of the signing box.
   */
  public record RegisteredSigningBox(Long handle) {
  }

  /**
   * @param encrypted  Encrypted data encoded in `base64`.
   */
  public record ResultOfNaclBox(String encrypted) {
  }

  /**
   * @param words  The list of mnemonic words
   */
  public record ResultOfMnemonicWords(String words) {
  }

  /**
   * @param data Encoded with `base64`. Encrypted/decrypted data.
   */
  public record ResultOfChaCha20(String data) {
  }

  /**
   *  Encryption box information.
   *
   * @param hdpath  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param algorithm  Cryptographic algorithm, used by this encryption box
   * @param options  Options, depends on algorithm and specific encryption box implementation
   * @param publicKey  Public information, depends on algorithm
   */
  public record EncryptionBoxInfo(String hdpath, String algorithm, JsonNode options,
      @JsonProperty("public") JsonNode publicKey) {
  }

  /**
   * @param xprv  Serialized extended master private key
   */
  public record ResultOfHDKeyXPrvFromMnemonic(String xprv) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public record ResultOfHDKeyDeriveFromXPrv(String xprv) {
  }

  public sealed interface EncryptionAlgorithm {
    record AES(Crypto.AesParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "AES";
      }
    }

    record ChaCha20(Crypto.ChaCha20ParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "ChaCha20";
      }
    }

    record NaclBox(Crypto.NaclBoxParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclBox";
      }
    }

    record NaclSecretBox(Crypto.NaclSecretBoxParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclSecretBox";
      }
    }
  }

  /**
   *  Interface for data encryption/decryption
   */
  public sealed interface ParamsOfAppEncryptionBox {
    /**
     *  Get encryption box info
     */
    record GetInfo() implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "GetInfo";
      }
    }

    /**
     *  Encrypt data
     *
     * @param data  Data, encoded in Base64
     */
    record Encrypt(String data) implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Encrypt";
      }
    }

    /**
     *  Decrypt data
     *
     * @param data  Data, encoded in Base64
     */
    record Decrypt(String data) implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Decrypt";
      }
    }
  }

  /**
   * @param decrypted  Data that must be encrypted encoded in `base64`.
   * @param nonce  Nonce, encoded in `hex`
   * @param theirPublic  Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclBox(String decrypted, String nonce, String theirPublic,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclSignKeyPairFromSecret(@JsonProperty("secret") String secretKey) {
  }

  /**
   *  Returning values from signing box callbacks.
   */
  public sealed interface ResultOfAppSigningBox {
    /**
     *  Result of getting public key
     *
     * @param publicKey  Signing box public key
     */
    record GetPublicKey(String publicKey) implements ResultOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "GetPublicKey";
      }
    }

    /**
     *  Result of signing data
     *
     * @param signature  Data signature encoded as hex
     */
    record Sign(String signature) implements ResultOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "Sign";
      }
    }
  }

  /**
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public record ParamsOfHash(String data) {
  }

  public sealed interface ResultOfAppPasswordProvider {
    /**
     * @param encryptedPassword  Password, encrypted and encoded to base64. Crypto box uses this password to decrypt its secret (seed phrase).
     * @param appEncryptionPubkey Used together with `encryption_public_key` to decode `encrypted_password`. Hex encoded public key of a temporary key pair, used for password encryption on application side.
     */
    record GetPassword(String encryptedPassword,
        String appEncryptionPubkey) implements ResultOfAppPasswordProvider {
      @JsonProperty("type")
      public String type() {
        return "GetPassword";
      }
    }
  }

  /**
   * @param unsigned Encoded with `base64`. Unsigned data that must be verified.
   * @param signature Encoded with `hex`. Signature that must be verified.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string.
   */
  public record ParamsOfNaclSignDetachedVerify(String unsigned, String signature,
      @JsonProperty("public") String publicKey) {
  }

  /**
   * @param length  Size of random byte array.
   */
  public record ParamsOfGenerateRandomBytes(Long length) {
  }

  /**
   * @param password  The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt  Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param logN  CPU/memory cost parameter
   * @param r  The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p  Parallelization parameter.
   * @param dkLen  Intended output length in octets of the derived key.
   */
  public record ParamsOfScrypt(String password, String salt, Integer logN, Long r, Long p,
      Long dkLen) {
  }

  /**
   * @param secretEncryptionSalt  Salt used for secret encryption. For example, a mobile device can use device ID as salt.
   * @param secretKey  Cryptobox secret
   */
  public record ParamsOfCreateCryptoBox(String secretEncryptionSalt,
      @JsonProperty("secret") Crypto.CryptoBoxSecret secretKey) {
  }

  public record RegisteredCryptoBox(Long handle) {
  }

  /**
   * @param handle  Handle of the encryption box.
   */
  public record RegisteredEncryptionBox(Long handle) {
  }

  /**
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public record ParamsOfMnemonicFromRandom(Crypto.MnemonicDictionary dictionary,
      Integer wordCount) {
  }

  /**
   * @param data Encoded with `base64`. Input data for CRC calculation.
   */
  public record ParamsOfTonCrc16(String data) {
  }

  /**
   * @param hash Encoded with 'hex'. Hash of input `data`.
   */
  public record ResultOfHash(String hash) {
  }

  /**
   * @param encryptionBox  Encryption box handle
   */
  public record ParamsOfEncryptionBoxGetInfo(Long encryptionBox) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public record ParamsOfHDKeySecretFromXPrv(String xprv) {
  }

  /**
   * @param phrase  Phrase
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public record ParamsOfMnemonicDeriveSignKeys(String phrase, String path,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) {
  }

  /**
   * @param encryptedSecret  Secret (seed phrase) encrypted with salt and password.
   */
  public record ResultOfGetCryptoBoxInfo(String encryptedSecret) {
  }

  /**
   * @param nonce  Nonce in `hex`
   */
  public record NaclSecretBoxParamsCB(String nonce) {
  }

  /**
   * @param phrase  String with seed phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public record ParamsOfHDKeyXPrvFromMnemonic(String phrase, Crypto.MnemonicDictionary dictionary,
      Integer wordCount) {
  }

  /**
   * @param tonPublicKey  Public key represented in TON safe format.
   */
  public record ResultOfConvertPublicKeyToTonSafeFormat(String tonPublicKey) {
  }

  /**
   * @param composite  Hexadecimal representation of u64 composite number.
   */
  public record ParamsOfFactorize(String composite) {
  }

  /**
   * @param data Padded to cipher block size Encrypted data, encoded in Base64.
   */
  public record ResultOfEncryptionBoxEncrypt(String data) {
  }

  /**
   * @param dictionary  Dictionary identifier
   */
  public record ParamsOfMnemonicWords(Crypto.MnemonicDictionary dictionary) {
  }

  public record AesInfo(Crypto.CipherMode mode, String iv) {
  }

  /**
   * @param xprv  Derived serialized extended private key
   */
  public record ResultOfHDKeyDeriveFromXPrvPath(String xprv) {
  }

  /**
   * @param xprv  Serialized extended private key
   * @param childIndex  Child index (see BIP-0032)
   * @param hardened  Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  public record ParamsOfHDKeyDeriveFromXPrv(String xprv, Long childIndex, Boolean hardened) {
  }

  /**
   * To secure the password while passing it from application to the library,
   * the library generates a temporary key pair, passes the pubkey
   * to the passwordProvider, decrypts the received password with private key,
   * and deletes the key pair right away.
   *
   * Application should generate a temporary nacl_box_keypair
   * and encrypt the password with naclbox function using nacl_box_keypair.secret
   * and encryption_public_key keys + nonce = 24-byte prefix of encryption_public_key. Interface that provides a callback that returns an encrypted password, used for cryptobox secret encryption
   */
  public sealed interface ParamsOfAppPasswordProvider {
    /**
     * @param encryptionPublicKey  Temporary library pubkey, that is used on application side for password encryption, along with application temporary private key and nonce. Used for password decryption on library side.
     */
    record GetPassword(String encryptionPublicKey) implements ParamsOfAppPasswordProvider {
      @JsonProperty("type")
      public String type() {
        return "GetPassword";
      }
    }
  }

  /**
   * @param unsigned  Unsigned data encoded in `base64`.
   */
  public record ResultOfVerifySignature(String unsigned) {
  }

  /**
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public record ParamsOfNaclSecretBoxOpen(String encrypted, String nonce, String key) {
  }

  /**
   * @param signature Encoded with `hex`. Data signature.
   */
  public record ResultOfSigningBoxSign(String signature) {
  }

  /**
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   * @param nonce  Nonce in `hex`
   */
  public record NaclSecretBoxParamsEB(String key, String nonce) {
  }

  /**
   * @param decrypted  Decrypted data encoded in `base64`.
   */
  public record ResultOfNaclBoxOpen(String decrypted) {
  }

  /**
   * @param signature  Signature encoded in `hex`.
   */
  public record ResultOfNaclSignDetached(String signature) {
  }

  /**
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param keys  Sign keys.
   */
  public record ParamsOfSign(String unsigned, Crypto.KeyPair keys) {
  }

  /**
   * @param succeeded  `true` if verification succeeded or `false` if it failed
   */
  public record ResultOfNaclSignDetachedVerify(Boolean succeeded) {
  }

  /**
   * @param info  Encryption box information
   */
  public record ResultOfEncryptionBoxGetInfo(Crypto.EncryptionBoxInfo info) {
  }

  /**
   * @param crc  Calculated CRC for input data.
   */
  public record ResultOfTonCrc16(Integer crc) {
  }
}
