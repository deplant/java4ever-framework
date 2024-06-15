package tech.deplant.java4ever.framework.contract.tip3;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;

import java.math.BigInteger;

/**
 * The type Tip 3 builder.
 */
public class TIP3Builder {

	private Tvc walletTvc = TIP3TokenWalletTemplate.DEFAULT_TVC();

	private Credentials rootKeys = Credentials.NONE;
	private Integer randomNonce = 0;
	private Address ownerAddress = Address.ZERO;
	private String name = "TEST-TOKEN";
	private String symbol = "TST";
	private int decimals = 9;

	private boolean mintDisabled = false;

	private boolean burnByRootDisabled = false;

	private boolean burnPaused = false;

	/**
	 * Instantiates a new Tip 3 builder.
	 */
	public TIP3Builder() {
	}

	/**
	 * Build tip 3 token root contract.
	 *
	 * @param contextId the context id
	 * @param giver     the giver
	 * @param value     the value
	 * @return the tip 3 token root contract
	 * @throws JsonProcessingException the json processing exception
	 * @throws EverSdkException        the ever sdk exception
	 */
	public TIP3TokenRootContract build(int contextId,
	                                   GiverContract giver,
	                                   BigInteger value) throws JsonProcessingException, EverSdkException {
		return new TIP3TokenRootTemplate().prepareDeploy(contextId, 0,
		                                                 this.rootKeys,
		                                                 this.name,
		                                                 this.symbol,
		                                                 this.decimals,
		                                                 this.ownerAddress,
		                                                 this.walletTvc.codeCell(contextId),
		                                                 BigInteger.valueOf(this.randomNonce),
		                                                 Address.ZERO,
		                                                 Address.ZERO,
		                                                 BigInteger.ZERO,
		                                                 BigInteger.ZERO,
		                                                 this.mintDisabled,
		                                                 this.burnByRootDisabled,
		                                                 this.burnPaused,
		                                                 this.ownerAddress)
		                                  .deployWithGiver(giver, value);
	}

	/**
	 * Root keys credentials.
	 *
	 * @return the credentials
	 */
	public Credentials rootKeys() {
		return rootKeys;
	}

	/**
	 * Sets root keys.
	 *
	 * @param rootKeys the root keys
	 * @return the root keys
	 */
	public TIP3Builder setRootKeys(Credentials rootKeys) {
		this.rootKeys = rootKeys;
		return this;
	}

	/**
	 * Random nonce integer.
	 *
	 * @return the integer
	 */
	public Integer randomNonce() {
		return randomNonce;
	}

	/**
	 * Sets random nonce.
	 *
	 * @param randomNonce the random nonce
	 * @return the random nonce
	 */
	public TIP3Builder setRandomNonce(Integer randomNonce) {
		this.randomNonce = randomNonce;
		return this;
	}

	/**
	 * Owner address address.
	 *
	 * @return the address
	 */
	public Address ownerAddress() {
		return ownerAddress;
	}

	/**
	 * Sets owner address.
	 *
	 * @param ownerAddress the owner address
	 * @return the owner address
	 */
	public TIP3Builder setOwnerAddress(Address ownerAddress) {
		this.ownerAddress = ownerAddress;
		return this;
	}

	/**
	 * Sets owner address.
	 *
	 * @param ownerAddress the owner address
	 * @return the owner address
	 */
	public TIP3Builder setOwnerAddress(String ownerAddress) {
		this.ownerAddress = new Address(ownerAddress);
		return this;
	}

	/**
	 * Name string.
	 *
	 * @return the string
	 */
	public String name() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 * @return the name
	 */
	public TIP3Builder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Symbol string.
	 *
	 * @return the string
	 */
	public String symbol() {
		return symbol;
	}

	/**
	 * Sets symbol.
	 *
	 * @param symbol the symbol
	 * @return the symbol
	 */
	public TIP3Builder setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	/**
	 * Decimals int.
	 *
	 * @return the int
	 */
	public int decimals() {
		return decimals;
	}

	/**
	 * Sets decimals.
	 *
	 * @param decimals the decimals
	 * @return the decimals
	 */
	public TIP3Builder setDecimals(int decimals) {
		this.decimals = decimals;
		return this;
	}

	/**
	 * Mint disabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean mintDisabled() {
		return mintDisabled;
	}

	/**
	 * Sets mint disabled.
	 *
	 * @param mintDisabled the mint disabled
	 * @return the mint disabled
	 */
	public TIP3Builder setMintDisabled(boolean mintDisabled) {
		this.mintDisabled = mintDisabled;
		return this;
	}

	/**
	 * Burn by root disabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean burnByRootDisabled() {
		return burnByRootDisabled;
	}

	/**
	 * Sets burn by root disabled.
	 *
	 * @param burnByRootDisabled the burn by root disabled
	 * @return the burn by root disabled
	 */
	public TIP3Builder setBurnByRootDisabled(boolean burnByRootDisabled) {
		this.burnByRootDisabled = burnByRootDisabled;
		return this;
	}

	/**
	 * Burn paused boolean.
	 *
	 * @return the boolean
	 */
	public boolean burnPaused() {
		return burnPaused;
	}

	/**
	 * Sets burn paused.
	 *
	 * @param burnPaused the burn paused
	 * @return the burn paused
	 */
	public TIP3Builder setBurnPaused(boolean burnPaused) {
		this.burnPaused = burnPaused;
		return this;
	}

	/**
	 * Wallet tvc tvc.
	 *
	 * @return the tvc
	 */
	public Tvc walletTvc() {
		return walletTvc;
	}

	/**
	 * Sets wallet tvc.
	 *
	 * @param walletTvc the wallet tvc
	 * @return the wallet tvc
	 */
	public TIP3Builder setWalletTvc(Tvc walletTvc) {
		this.walletTvc = walletTvc;
		return this;
	}
}
