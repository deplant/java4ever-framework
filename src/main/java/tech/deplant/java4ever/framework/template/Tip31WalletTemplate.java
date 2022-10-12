package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.abi.ContractAbi;

public class Tip31WalletTemplate extends ContractTemplate {

	public static final ContractTvc DEFAULT_TVC = ContractTvc.ofResource("artifacts/tip31/TokenWallet.tvc");

	public Tip31WalletTemplate() throws JsonProcessingException {
		super(DEFAULT_ABI(), DEFAULT_TVC);
	}

	public Tip31WalletTemplate(ContractAbi abi, ContractTvc tvc) {
		super(abi, tvc);
	}

	public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
		return ContractAbi.ofResource("artifacts/tip31/TokenWallet.abi.json");
	}


}

