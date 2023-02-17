package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionCall;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>SurfMultisigWallet</strong> contract for Everscale blockchain.
 */
public record SurfMultisigWallet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements MultisigWallet {
  public SurfMultisigWallet(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"acceptTransfer\",\"inputs\":[{\"name\":\"payload\",\"type\":\"bytes\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"submitTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"allBalance\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[{\"name\":\"transId\",\"type\":\"uint64\"}]},{\"name\":\"confirmTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"isConfirmed\",\"inputs\":[{\"name\":\"mask\",\"type\":\"uint32\"},{\"name\":\"index\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"confirmed\",\"type\":\"bool\"}]},{\"name\":\"getParameters\",\"inputs\":[],\"outputs\":[{\"name\":\"maxQueuedTransactions\",\"type\":\"uint8\"},{\"name\":\"maxCustodianCount\",\"type\":\"uint8\"},{\"name\":\"expirationTime\",\"type\":\"uint64\"},{\"name\":\"minValue\",\"type\":\"uint128\"},{\"name\":\"requiredTxnConfirms\",\"type\":\"uint8\"},{\"name\":\"requiredUpdConfirms\",\"type\":\"uint8\"}]},{\"name\":\"getTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"trans\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"}]}]},{\"name\":\"getTransactions\",\"inputs\":[],\"outputs\":[{\"name\":\"transactions\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"}]}]},{\"name\":\"getTransactionIds\",\"inputs\":[],\"outputs\":[{\"name\":\"ids\",\"type\":\"uint64[]\"}]},{\"name\":\"getCustodians\",\"inputs\":[],\"outputs\":[{\"name\":\"custodians\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"pubkey\",\"type\":\"uint256\"}]}]},{\"name\":\"submitUpdate\",\"inputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"},{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}]},{\"name\":\"confirmUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"executeUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"},{\"name\":\"code\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"getUpdateRequests\",\"inputs\":[],\"outputs\":[{\"name\":\"updates\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"uint256\"},{\"name\":\"custodians\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"}]}]}],\"events\":[{\"name\":\"TransferAccepted\",\"inputs\":[{\"name\":\"payload\",\"type\":\"bytes\"}]}],\"data\":[]}");
  }

  public FunctionCall<Void> acceptTransfer(String payload) {
    Map<String, Object> params = Map.of("payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "acceptTransfer", params, null);
  }

  public FunctionCall<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce,
      Integer flags, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "flags", flags, 
        "payload", payload);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "sendTransaction", params, null);
  }

  public FunctionCall<ResultOfSubmitTransaction> submitTransaction(Address dest, BigInteger value,
      Boolean bounce, Boolean allBalance, TvmCell payload) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value, 
        "bounce", bounce, 
        "allBalance", allBalance, 
        "payload", payload);
    return new FunctionCall<ResultOfSubmitTransaction>(sdk(), address(), abi(), credentials(), "submitTransaction", params, null);
  }

  public FunctionCall<Void> confirmTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "confirmTransaction", params, null);
  }

  public FunctionCall<ResultOfIsConfirmed> isConfirmed(Integer mask, Integer index) {
    Map<String, Object> params = Map.of("mask", mask, 
        "index", index);
    return new FunctionCall<ResultOfIsConfirmed>(sdk(), address(), abi(), credentials(), "isConfirmed", params, null);
  }

  public FunctionCall<ResultOfGetParameters> getParameters() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetParameters>(sdk(), address(), abi(), credentials(), "getParameters", params, null);
  }

  public FunctionCall<ResultOfGetTransaction> getTransaction(Long transactionId) {
    Map<String, Object> params = Map.of("transactionId", transactionId);
    return new FunctionCall<ResultOfGetTransaction>(sdk(), address(), abi(), credentials(), "getTransaction", params, null);
  }

  public FunctionCall<ResultOfGetTransactions> getTransactions() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetTransactions>(sdk(), address(), abi(), credentials(), "getTransactions", params, null);
  }

  public FunctionCall<ResultOfGetTransactionIds> getTransactionIds() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetTransactionIds>(sdk(), address(), abi(), credentials(), "getTransactionIds", params, null);
  }

  public FunctionCall<ResultOfGetCustodians> getCustodians() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetCustodians>(sdk(), address(), abi(), credentials(), "getCustodians", params, null);
  }

  public FunctionCall<ResultOfSubmitUpdate> submitUpdate(BigInteger codeHash, BigInteger[] owners,
      Integer reqConfirms) {
    Map<String, Object> params = Map.of("codeHash", codeHash, 
        "owners", owners, 
        "reqConfirms", reqConfirms);
    return new FunctionCall<ResultOfSubmitUpdate>(sdk(), address(), abi(), credentials(), "submitUpdate", params, null);
  }

  public FunctionCall<Void> confirmUpdate(Long updateId) {
    Map<String, Object> params = Map.of("updateId", updateId);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "confirmUpdate", params, null);
  }

  public FunctionCall<Void> executeUpdate(Long updateId, TvmCell code) {
    Map<String, Object> params = Map.of("updateId", updateId, 
        "code", code);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "executeUpdate", params, null);
  }

  public FunctionCall<ResultOfGetUpdateRequests> getUpdateRequests() {
    Map<String, Object> params = Map.of();
    return new FunctionCall<ResultOfGetUpdateRequests>(sdk(), address(), abi(), credentials(), "getUpdateRequests", params, null);
  }

  public record ResultOfSubmitTransaction(Long transId) {
  }

  public record ResultOfIsConfirmed(Boolean confirmed) {
  }

  public record ResultOfGetParameters(Integer maxQueuedTransactions, Integer maxCustodianCount,
      Long expirationTime, BigInteger minValue, Integer requiredTxnConfirms,
      Integer requiredUpdConfirms) {
  }

  public record ResultOfGetTransaction(Map<String, Object> trans) {
  }

  public record ResultOfGetTransactions(Map<String, Object>[] transactions) {
  }

  public record ResultOfGetTransactionIds(Long[] ids) {
  }

  public record ResultOfGetCustodians(Map<String, Object>[] custodians) {
  }

  public record ResultOfSubmitUpdate(Long updateId) {
  }

  public record ResultOfGetUpdateRequests(Map<String, Object>[] updates) {
  }
}
