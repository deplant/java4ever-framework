package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP4Collection</strong> contract for Everscale blockchain.
 */
public record TIP4Collection(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP4Collection(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TIP4Collection(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TIP4Collection(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"codeNft\",\"type\":\"cell\"},{\"name\":\"codeIndex\",\"type\":\"cell\"},{\"name\":\"codeIndexBasis\",\"type\":\"cell\"},{\"name\":\"ownerPubkey\",\"type\":\"uint256\"},{\"name\":\"json\",\"type\":\"string\"},{\"name\":\"mintingFee\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"mintNft\",\"inputs\":[{\"name\":\"json\",\"type\":\"string\"}],\"outputs\":[]},{\"name\":\"withdraw\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"onTokenBurned\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setRemainOnNft\",\"inputs\":[{\"name\":\"remainOnNft\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setMintingFee\",\"inputs\":[{\"name\":\"mintingFee\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"mintingFee\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"owner\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"pubkey\",\"type\":\"uint256\"}]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"indexBasisCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"indexBasisCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"hash\",\"type\":\"uint256\"}]},{\"name\":\"resolveIndexBasis\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"indexBasis\",\"type\":\"address\"}]},{\"name\":\"indexCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"indexCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"hash\",\"type\":\"uint256\"}]},{\"name\":\"getJson\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"json\",\"type\":\"string\"}]},{\"name\":\"totalSupply\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"count\",\"type\":\"uint128\"}]},{\"name\":\"nftCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"nftCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"}]},{\"name\":\"nftAddress\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"id\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"nft\",\"type\":\"address\"}]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]}],\"events\":[{\"name\":\"OwnershipTransferred\",\"inputs\":[{\"name\":\"oldOwner\",\"type\":\"uint256\"},{\"name\":\"newOwner\",\"type\":\"uint256\"}]},{\"name\":\"NftCreated\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"nft\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"},{\"name\":\"creator\",\"type\":\"address\"}]},{\"name\":\"NftBurned\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"nft\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"}]}],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_supportedInterfaces\",\"type\":\"optional(cell)\"},{\"name\":\"_codeNft\",\"type\":\"cell\"},{\"name\":\"_totalSupply\",\"type\":\"uint128\"},{\"name\":\"_json\",\"type\":\"string\"},{\"name\":\"_codeIndex\",\"type\":\"cell\"},{\"name\":\"_codeIndexBasis\",\"type\":\"cell\"},{\"name\":\"_indexDeployValue\",\"type\":\"uint128\"},{\"name\":\"_indexDestroyValue\",\"type\":\"uint128\"},{\"name\":\"_deployIndexBasisValue\",\"type\":\"uint128\"},{\"name\":\"_owner\",\"type\":\"uint256\"},{\"name\":\"_remainOnNft\",\"type\":\"uint128\"},{\"name\":\"_lastTokenId\",\"type\":\"uint128\"},{\"name\":\"_mintingFee\",\"type\":\"uint128\"}]}");
  }

  public FunctionHandle<Void> mintNft(String json) {
    Map<String, Object> params = Map.of("json", json);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "mintNft", params, null);
  }

  public FunctionHandle<Void> withdraw(Address dest, BigInteger value) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "withdraw", params, null);
  }

  public FunctionHandle<Void> onTokenBurned(BigInteger id, Address owner, Address manager) {
    Map<String, Object> params = Map.of("id", id, 
        "owner", owner, 
        "manager", manager);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onTokenBurned", params, null);
  }

  public FunctionHandle<Void> setRemainOnNft(BigInteger remainOnNft) {
    Map<String, Object> params = Map.of("remainOnNft", remainOnNft);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setRemainOnNft", params, null);
  }

  public FunctionHandle<Void> setMintingFee(BigInteger mintingFee) {
    Map<String, Object> params = Map.of("mintingFee", mintingFee);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setMintingFee", params, null);
  }

  public FunctionHandle<ResultOfMintingFee> mintingFee() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfMintingFee>(ResultOfMintingFee.class, sdk(), address(), abi(), credentials(), "mintingFee", params, null);
  }

  public FunctionHandle<ResultOfOwner> owner() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfOwner>(ResultOfOwner.class, sdk(), address(), abi(), credentials(), "owner", params, null);
  }

  public FunctionHandle<Void> transferOwnership(BigInteger newOwner) {
    Map<String, Object> params = Map.of("newOwner", newOwner);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "transferOwnership", params, null);
  }

  public FunctionHandle<ResultOfIndexBasisCode> indexBasisCode() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfIndexBasisCode>(ResultOfIndexBasisCode.class, sdk(), address(), abi(), credentials(), "indexBasisCode", params, null);
  }

  public FunctionHandle<ResultOfIndexBasisCodeHash> indexBasisCodeHash() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfIndexBasisCodeHash>(ResultOfIndexBasisCodeHash.class, sdk(), address(), abi(), credentials(), "indexBasisCodeHash", params, null);
  }

  public FunctionHandle<ResultOfResolveIndexBasis> resolveIndexBasis() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfResolveIndexBasis>(ResultOfResolveIndexBasis.class, sdk(), address(), abi(), credentials(), "resolveIndexBasis", params, null);
  }

  public FunctionHandle<ResultOfIndexCode> indexCode() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfIndexCode>(ResultOfIndexCode.class, sdk(), address(), abi(), credentials(), "indexCode", params, null);
  }

  public FunctionHandle<ResultOfIndexCodeHash> indexCodeHash() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfIndexCodeHash>(ResultOfIndexCodeHash.class, sdk(), address(), abi(), credentials(), "indexCodeHash", params, null);
  }

  public FunctionHandle<ResultOfGetJson> getJson() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfGetJson>(ResultOfGetJson.class, sdk(), address(), abi(), credentials(), "getJson", params, null);
  }

  public FunctionHandle<ResultOfTotalSupply> totalSupply() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfTotalSupply>(ResultOfTotalSupply.class, sdk(), address(), abi(), credentials(), "totalSupply", params, null);
  }

  public FunctionHandle<ResultOfNftCode> nftCode() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfNftCode>(ResultOfNftCode.class, sdk(), address(), abi(), credentials(), "nftCode", params, null);
  }

  public FunctionHandle<ResultOfNftCodeHash> nftCodeHash() {
    Map<String, Object> params = Map.of("answerId", 0);
    return new FunctionHandle<ResultOfNftCodeHash>(ResultOfNftCodeHash.class, sdk(), address(), abi(), credentials(), "nftCodeHash", params, null);
  }

  public FunctionHandle<ResultOfNftAddress> nftAddress(BigInteger id) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "id", id);
    return new FunctionHandle<ResultOfNftAddress>(ResultOfNftAddress.class, sdk(), address(), abi(), credentials(), "nftAddress", params, null);
  }

  public FunctionHandle<ResultOfSupportsInterface> supportsInterface(Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", 0, 
        "interfaceID", interfaceID);
    return new FunctionHandle<ResultOfSupportsInterface>(ResultOfSupportsInterface.class, sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
  }

  public record ResultOfMintingFee(BigInteger value0) {
  }

  public record ResultOfOwner(BigInteger pubkey) {
  }

  public record ResultOfIndexBasisCode(TvmCell code) {
  }

  public record ResultOfIndexBasisCodeHash(BigInteger hash) {
  }

  public record ResultOfResolveIndexBasis(Address indexBasis) {
  }

  public record ResultOfIndexCode(TvmCell code) {
  }

  public record ResultOfIndexCodeHash(BigInteger hash) {
  }

  public record ResultOfGetJson(String json) {
  }

  public record ResultOfTotalSupply(BigInteger count) {
  }

  public record ResultOfNftCode(TvmCell code) {
  }

  public record ResultOfNftCodeHash(BigInteger codeHash) {
  }

  public record ResultOfNftAddress(Address nft) {
  }

  public record ResultOfSupportsInterface(Boolean value0) {
  }
}
