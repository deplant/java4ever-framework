package tech.deplant.java4ever.framework.contract.multisig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Byte;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>MultisigWalletGoshContract</strong> contract for Everscale blockchain.
 */
public class MultisigWalletGoshContract extends MultisigContract {
  public MultisigWalletGoshContract(int contextId, String address) throws JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public MultisigWalletGoshContract(int contextId, String address, ContractAbi abi) {
    super(contextId,address,abi,Credentials.NONE);
  }

  public MultisigWalletGoshContract(int contextId, String address, Credentials credentials) throws
      JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),credentials);
  }

  @JsonCreator
  public MultisigWalletGoshContract(int contextId, String address, ContractAbi abi,
      Credentials credentials) {
    super(contextId,address,abi,credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"version\":\"2.3\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"acceptTransfer\",\"inputs\":[{\"name\":\"payload\",\"type\":\"bytes\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"submitTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"allBalance\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[{\"name\":\"transId\",\"type\":\"uint64\"}]},{\"name\":\"confirmTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"isConfirmed\",\"inputs\":[{\"name\":\"mask\",\"type\":\"uint32\"},{\"name\":\"index\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"confirmed\",\"type\":\"bool\"}]},{\"name\":\"getParameters\",\"inputs\":[],\"outputs\":[{\"name\":\"maxQueuedTransactions\",\"type\":\"uint8\"},{\"name\":\"maxQueuedLimits\",\"type\":\"uint8\"},{\"name\":\"maxCustodianCount\",\"type\":\"uint8\"},{\"name\":\"maxLimitPeriod\",\"type\":\"uint32\"},{\"name\":\"expirationTime\",\"type\":\"uint64\"},{\"name\":\"minValue\",\"type\":\"uint128\"},{\"name\":\"requiredTxnConfirms\",\"type\":\"uint8\"},{\"name\":\"requiredLimConfirms\",\"type\":\"uint8\"},{\"name\":\"requiredUpdConfirms\",\"type\":\"uint8\"}]},{\"name\":\"getTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"trans\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"}]}]},{\"name\":\"getTransactions\",\"inputs\":[],\"outputs\":[{\"name\":\"transactions\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"}]}]},{\"name\":\"getTransactionIds\",\"inputs\":[],\"outputs\":[{\"name\":\"ids\",\"type\":\"uint64[]\"}]},{\"name\":\"getCustodians\",\"inputs\":[],\"outputs\":[{\"name\":\"custodians\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"pubkey\",\"type\":\"uint256\"}]}]},{\"name\":\"createLimit\",\"inputs\":[{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"}]},{\"name\":\"confirmLimit\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"changeLimit\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"newLimitId\",\"type\":\"uint64\"}]},{\"name\":\"deleteLimit\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"getLimits\",\"inputs\":[],\"outputs\":[{\"name\":\"limits\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]}]},{\"name\":\"getPendingLimit\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"limit\",\"type\":\"tuple\",\"components\":[{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"parentId\",\"type\":\"uint64\"},{\"name\":\"limit\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]}]}]},{\"name\":\"getPendingLimits\",\"inputs\":[],\"outputs\":[{\"name\":\"pendingLimits\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"info\",\"type\":\"tuple\",\"components\":[{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"parentId\",\"type\":\"uint64\"},{\"name\":\"limit\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]}]}]}]},{\"name\":\"getLimit\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"limit\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]}]},{\"name\":\"submitUpdate\",\"inputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"},{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}]},{\"name\":\"confirmUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"executeUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"},{\"name\":\"code\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"getUpdateRequests\",\"inputs\":[],\"outputs\":[{\"name\":\"updates\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"uint256\"},{\"name\":\"custodians\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}]}]},{\"name\":\"TheBigBang\",\"inputs\":[{\"name\":\"returnMoney\",\"type\":\"address\"}],\"outputs\":[]}],\"events\":[{\"name\":\"TransferAccepted\",\"inputs\":[{\"name\":\"payload\",\"type\":\"bytes\"}]},{\"name\":\"LimitOverrun\",\"inputs\":[{\"name\":\"limitId\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"}]}],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"m_ownerKey\",\"type\":\"uint256\"},{\"name\":\"m_requestsMask\",\"type\":\"uint256\"},{\"name\":\"m_transactions\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"}]},{\"name\":\"m_custodians\",\"type\":\"map(uint256,uint8)\"},{\"name\":\"m_custodianCount\",\"type\":\"uint8\"},{\"name\":\"m_limits\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]},{\"name\":\"m_pendingLimits\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"parentId\",\"type\":\"uint64\"},{\"name\":\"limit\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"period\",\"type\":\"uint32\"},{\"name\":\"required\",\"type\":\"uint8\"},{\"name\":\"spent\",\"type\":\"uint256\"},{\"name\":\"start\",\"type\":\"uint32\"},{\"name\":\"votes\",\"type\":\"uint8\"},{\"name\":\"deletionMask\",\"type\":\"uint32\"}]}]},{\"name\":\"m_limitRequestsMask\",\"type\":\"uint256\"},{\"name\":\"m_updateRequests\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"uint256\"},{\"name\":\"custodians\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}]},{\"name\":\"m_updateRequestsMask\",\"type\":\"uint32\"},{\"name\":\"m_requiredVotes\",\"type\":\"uint8\"},{\"name\":\"m_defaultRequiredConfirmations\",\"type\":\"uint8\"}],\"ABI version\":2}");
  }

  public FunctionHandle<Void> acceptTransfer(Byte[] payload) {
    Map<String, Object> params = Map.of("payload", payload);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "acceptTransfer", params, null);
  }

  public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "sendTransaction", params, null);
  }

  public FunctionHandle<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
      Boolean bounce, Boolean allBalance, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "allBalance", allBalance, 
        "payload", payload);
    return new FunctionHandle<ResultOfSubmitTransaction>(ResultOfSubmitTransaction.class, contextId(), address(), abi(), credentials(), "submitTransaction", params, null);
  }

  public FunctionHandle<Void> confirmTransaction(BigInteger transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "confirmTransaction", params, null);
  }

  public FunctionHandle<ResultOfIsConfirmed> isConfirmed(Long mask, Integer index) {
    Map<String, Object> params = Map.of("mask", mask, 
        "index", index);
    return new FunctionHandle<ResultOfIsConfirmed>(ResultOfIsConfirmed.class, contextId(), address(), abi(), credentials(), "isConfirmed", params, null);
  }

  public FunctionHandle<ResultOfGetParameters> getParameters() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetParameters>(ResultOfGetParameters.class, contextId(), address(), abi(), credentials(), "getParameters", params, null);
  }

  public FunctionHandle<ResultOfGetTransaction> getTransaction(BigInteger transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionHandle<ResultOfGetTransaction>(ResultOfGetTransaction.class, contextId(), address(), abi(), credentials(), "getTransaction", params, null);
  }

  public FunctionHandle<ResultOfGetTransactions> getTransactions() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetTransactions>(ResultOfGetTransactions.class, contextId(), address(), abi(), credentials(), "getTransactions", params, null);
  }

  public FunctionHandle<ResultOfGetTransactionIds> getTransactionIds() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetTransactionIds>(ResultOfGetTransactionIds.class, contextId(), address(), abi(), credentials(), "getTransactionIds", params, null);
  }

  public FunctionHandle<ResultOfGetCustodians> getCustodians() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetCustodians>(ResultOfGetCustodians.class, contextId(), address(), abi(), credentials(), "getCustodians", params, null);
  }

  public FunctionHandle<ResultOfCreateLimit> createLimit(BigInteger value, Long period,
      Integer required) {
    Map<String, Object> params = Map.of("value", value, 
        "period", period, 
        "required", required);
    return new FunctionHandle<ResultOfCreateLimit>(ResultOfCreateLimit.class, contextId(), address(), abi(), credentials(), "createLimit", params, null);
  }

  public FunctionHandle<Void> confirmLimit(BigInteger limitId) {
    Map<String, Object> params = Map.of("limitId", limitId);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "confirmLimit", params, null);
  }

  public FunctionHandle<ResultOfChangeLimit> changeLimit(BigInteger limitId, BigInteger value,
      Long period, Integer required) {
    Map<String, Object> params = Map.of("limitId", limitId, 
        "value", value, 
        "period", period, 
        "required", required);
    return new FunctionHandle<ResultOfChangeLimit>(ResultOfChangeLimit.class, contextId(), address(), abi(), credentials(), "changeLimit", params, null);
  }

  public FunctionHandle<Void> deleteLimit(BigInteger limitId) {
    Map<String, Object> params = Map.of("limitId", limitId);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "deleteLimit", params, null);
  }

  public FunctionHandle<ResultOfGetLimits> getLimits() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetLimits>(ResultOfGetLimits.class, contextId(), address(), abi(), credentials(), "getLimits", params, null);
  }

  public FunctionHandle<ResultOfGetPendingLimit> getPendingLimit(BigInteger limitId) {
    Map<String, Object> params = Map.of("limitId", limitId);
    return new FunctionHandle<ResultOfGetPendingLimit>(ResultOfGetPendingLimit.class, contextId(), address(), abi(), credentials(), "getPendingLimit", params, null);
  }

  public FunctionHandle<ResultOfGetPendingLimits> getPendingLimits() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetPendingLimits>(ResultOfGetPendingLimits.class, contextId(), address(), abi(), credentials(), "getPendingLimits", params, null);
  }

  public FunctionHandle<ResultOfGetLimit> getLimit(BigInteger limitId) {
    Map<String, Object> params = Map.of("limitId", limitId);
    return new FunctionHandle<ResultOfGetLimit>(ResultOfGetLimit.class, contextId(), address(), abi(), credentials(), "getLimit", params, null);
  }

  public FunctionHandle<ResultOfSubmitUpdate> submitUpdate(BigInteger codeHash, BigInteger[] owners,
      Integer reqConfirms) {
    Map<String, Object> params = Map.of("codeHash", codeHash, 
        "owners", owners, 
        "reqConfirms", reqConfirms);
    return new FunctionHandle<ResultOfSubmitUpdate>(ResultOfSubmitUpdate.class, contextId(), address(), abi(), credentials(), "submitUpdate", params, null);
  }

  public FunctionHandle<Void> confirmUpdate(BigInteger updateId) {
    Map<String, Object> params = Map.of("updateId", updateId);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "confirmUpdate", params, null);
  }

  public FunctionHandle<Void> executeUpdate(BigInteger updateId, TvmCell code) {
    Map<String, Object> params = Map.of("updateId", updateId, 
        "code", code);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "executeUpdate", params, null);
  }

  public FunctionHandle<ResultOfGetUpdateRequests> getUpdateRequests() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetUpdateRequests>(ResultOfGetUpdateRequests.class, contextId(), address(), abi(), credentials(), "getUpdateRequests", params, null);
  }

  public FunctionHandle<Void> TheBigBang(Address returnMoney) {
    Map<String, Object> params = Map.of("returnMoney", returnMoney);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "TheBigBang", params, null);
  }
}
