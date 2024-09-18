package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigSolc064WalletContract;

/**
 * Java template class for deploy of <strong>SafeMultisigSolc064WalletContract</strong> contract for Everscale blockchain.
 */
public record SafeMultisigSolc064WalletTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public SafeMultisigSolc064WalletTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public SafeMultisigSolc064WalletTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"version\":\"2.3\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"},{\"name\":\"lifetime\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"submitTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"allBalance\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}],\"outputs\":[{\"name\":\"transId\",\"type\":\"uint64\"}]},{\"name\":\"confirmTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"isConfirmed\",\"inputs\":[{\"name\":\"mask\",\"type\":\"uint32\"},{\"name\":\"index\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"confirmed\",\"type\":\"bool\"}]},{\"name\":\"getParameters\",\"inputs\":[],\"outputs\":[{\"name\":\"maxQueuedTransactions\",\"type\":\"uint8\"},{\"name\":\"maxCustodianCount\",\"type\":\"uint8\"},{\"name\":\"expirationTime\",\"type\":\"uint64\"},{\"name\":\"minValue\",\"type\":\"uint128\"},{\"name\":\"requiredTxnConfirms\",\"type\":\"uint8\"}]},{\"name\":\"getTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"trans\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getTransactions\",\"inputs\":[],\"outputs\":[{\"name\":\"transactions\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getCustodians\",\"inputs\":[],\"outputs\":[{\"name\":\"custodians\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"pubkey\",\"type\":\"uint256\"}]}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"m_ownerKey\",\"type\":\"uint256\"},{\"name\":\"m_requestsMask\",\"type\":\"uint256\"},{\"name\":\"m_transactions\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]},{\"name\":\"m_custodians\",\"type\":\"map(uint256,uint8)\"},{\"name\":\"m_custodianCount\",\"type\":\"uint8\"},{\"name\":\"m_defaultRequiredConfirmations\",\"type\":\"uint8\"},{\"name\":\"m_lifetime\",\"type\":\"uint64\"}],\"ABI version\":2}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<SafeMultisigSolc064WalletContract> prepareDeploy(int contextId,
      int workchainId, Credentials credentials, BigInteger[] owners, Integer reqConfirms,
      Long lifetime) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("owners", owners, 
        "reqConfirms", reqConfirms, 
        "lifetime", lifetime);
    return new DeployHandle<SafeMultisigSolc064WalletContract>(SafeMultisigSolc064WalletContract.class, contextId, abi(), tvc(), workchainId, credentials, initialDataFields, params, null);
  }
}
