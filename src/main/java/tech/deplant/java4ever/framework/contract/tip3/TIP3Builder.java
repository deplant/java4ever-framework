package tech.deplant.java4ever.framework.contract.tip3;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.GiverContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;

import java.math.BigInteger;

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

	public TIP3Builder() {
	}

	public TIP3TokenRootContract build(Sdk sdk,
	                           GiverContract giver,
	                           BigInteger value) throws JsonProcessingException, EverSdkException {
		return new TIP3TokenRootTemplate().prepareDeploy(sdk,
		                                                 this.rootKeys,
		                                                 this.name,
		                                                 this.symbol,
		                                                 this.decimals,
		                                                 this.ownerAddress,
		                                                 this.walletTvc.codeCell(sdk),
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

	public Credentials rootKeys() {
		return rootKeys;
	}

	public TIP3Builder setRootKeys(Credentials rootKeys) {
		this.rootKeys = rootKeys;
		return this;
	}

	public Integer randomNonce() {
		return randomNonce;
	}

	public TIP3Builder setRandomNonce(Integer randomNonce) {
		this.randomNonce = randomNonce;
		return this;
	}

	public Address ownerAddress() {
		return ownerAddress;
	}

	public TIP3Builder setOwnerAddress(Address ownerAddress) {
		this.ownerAddress = ownerAddress;
		return this;
	}

	public TIP3Builder setOwnerAddress(String ownerAddress) {
		this.ownerAddress = new Address(ownerAddress);
		return this;
	}

	public String name() {
		return name;
	}

	public TIP3Builder setName(String name) {
		this.name = name;
		return this;
	}

	public String symbol() {
		return symbol;
	}

	public TIP3Builder setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public int decimals() {
		return decimals;
	}

	public TIP3Builder setDecimals(int decimals) {
		this.decimals = decimals;
		return this;
	}

	public boolean mintDisabled() {
		return mintDisabled;
	}

	public TIP3Builder setMintDisabled(boolean mintDisabled) {
		this.mintDisabled = mintDisabled;
		return this;
	}

	public boolean burnByRootDisabled() {
		return burnByRootDisabled;
	}

	public TIP3Builder setBurnByRootDisabled(boolean burnByRootDisabled) {
		this.burnByRootDisabled = burnByRootDisabled;
		return this;
	}

	public boolean burnPaused() {
		return burnPaused;
	}

	public TIP3Builder setBurnPaused(boolean burnPaused) {
		this.burnPaused = burnPaused;
		return this;
	}

	public Tvc walletTvc() {
		return walletTvc;
	}

	public TIP3Builder setWalletTvc(Tvc walletTvc) {
		this.walletTvc = walletTvc;
		return this;
	}
}
