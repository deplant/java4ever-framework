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
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.multisig2.UpdateMultisigWallet2Contract;

/**
 * Java template class for deploy of <strong>UpdateMultisigWallet2Contract</strong> contract for Everscale blockchain.
 */
public record UpdateMultisigWallet2Template(ContractAbi abi, Tvc tvc) implements Template {
  public UpdateMultisigWallet2Template(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public UpdateMultisigWallet2Template() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"},{\"name\":\"lifetime\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"submitTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"allBalance\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}],\"outputs\":[{\"name\":\"transId\",\"type\":\"uint64\"}]},{\"name\":\"confirmTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"isConfirmed\",\"inputs\":[{\"name\":\"mask\",\"type\":\"uint32\"},{\"name\":\"index\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"confirmed\",\"type\":\"bool\"}]},{\"name\":\"getParameters\",\"inputs\":[],\"outputs\":[{\"name\":\"maxQueuedTransactions\",\"type\":\"uint8\"},{\"name\":\"maxCustodianCount\",\"type\":\"uint8\"},{\"name\":\"expirationTime\",\"type\":\"uint64\"},{\"name\":\"minValue\",\"type\":\"uint128\"},{\"name\":\"requiredTxnConfirms\",\"type\":\"uint8\"},{\"name\":\"requiredUpdConfirms\",\"type\":\"uint8\"}]},{\"name\":\"getTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"trans\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getTransactions\",\"inputs\":[],\"outputs\":[{\"name\":\"transactions\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getCustodians\",\"inputs\":[],\"outputs\":[{\"name\":\"custodians\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"pubkey\",\"type\":\"uint256\"}]}]},{\"name\":\"submitUpdate\",\"inputs\":[{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"owners\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}],\"outputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}]},{\"name\":\"confirmUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"executeUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"},{\"name\":\"code\",\"type\":\"optional(cell)\"}],\"outputs\":[]},{\"name\":\"getUpdateRequests\",\"inputs\":[],\"outputs\":[{\"name\":\"updates\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"custodians\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}]}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"m_ownerKey\",\"type\":\"uint256\"},{\"name\":\"m_requestsMask\",\"type\":\"uint256\"},{\"name\":\"m_transactions\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]},{\"name\":\"m_custodians\",\"type\":\"map(uint256,uint8)\"},{\"name\":\"m_custodianCount\",\"type\":\"uint8\"},{\"name\":\"m_updateRequests\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"custodians\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}]},{\"name\":\"m_updateRequestsMask\",\"type\":\"uint32\"},{\"name\":\"m_requiredVotes\",\"type\":\"uint8\"},{\"name\":\"m_defaultRequiredConfirmations\",\"type\":\"uint8\"},{\"name\":\"m_lifetime\",\"type\":\"uint32\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<UpdateMultisigWallet2Contract> prepareDeploy(Sdk sdk, Credentials credentials,
      BigInteger[] owners, Integer reqConfirms, Long lifetime) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("owners", owners, 
        "reqConfirms", reqConfirms, 
        "lifetime", lifetime);
    return new DeployHandle<UpdateMultisigWallet2Contract>(UpdateMultisigWallet2Contract.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
