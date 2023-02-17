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
import tech.deplant.java4ever.framework.FunctionCall;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>TIP4Collection</strong> contract for Everscale blockchain.
 */
public record TIP4Collection(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TIP4Collection(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"codeNft\",\"type\":\"cell\"},{\"name\":\"codeIndex\",\"type\":\"cell\"},{\"name\":\"codeIndexBasis\",\"type\":\"cell\"},{\"name\":\"ownerPubkey\",\"type\":\"uint256\"},{\"name\":\"json\",\"type\":\"string\"},{\"name\":\"mintingFee\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"mintNft\",\"inputs\":[{\"name\":\"json\",\"type\":\"string\"}],\"outputs\":[]},{\"name\":\"withdraw\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"onTokenBurned\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setRemainOnNft\",\"inputs\":[{\"name\":\"remainOnNft\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setMintingFee\",\"inputs\":[{\"name\":\"mintingFee\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"mintingFee\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"owner\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"pubkey\",\"type\":\"uint256\"}]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"indexBasisCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"indexBasisCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"hash\",\"type\":\"uint256\"}]},{\"name\":\"resolveIndexBasis\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"indexBasis\",\"type\":\"address\"}]},{\"name\":\"indexCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"indexCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"hash\",\"type\":\"uint256\"}]},{\"name\":\"getJson\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"json\",\"type\":\"string\"}]},{\"name\":\"totalSupply\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"count\",\"type\":\"uint128\"}]},{\"name\":\"nftCode\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"code\",\"type\":\"cell\"}]},{\"name\":\"nftCodeHash\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"}]},{\"name\":\"nftAddress\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"id\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"nft\",\"type\":\"address\"}]},{\"name\":\"supportsInterface\",\"inputs\":[{\"name\":\"answerId\",\"type\":\"uint32\"},{\"name\":\"interfaceID\",\"type\":\"uint32\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"bool\"}]}],\"events\":[{\"name\":\"OwnershipTransferred\",\"inputs\":[{\"name\":\"oldOwner\",\"type\":\"uint256\"},{\"name\":\"newOwner\",\"type\":\"uint256\"}]},{\"name\":\"NftCreated\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"nft\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"},{\"name\":\"creator\",\"type\":\"address\"}]},{\"name\":\"NftBurned\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"nft\",\"type\":\"address\"},{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"manager\",\"type\":\"address\"}]}],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_supportedInterfaces\",\"type\":\"optional(cell)\"},{\"name\":\"_codeNft\",\"type\":\"cell\"},{\"name\":\"_totalSupply\",\"type\":\"uint128\"},{\"name\":\"_json\",\"type\":\"string\"},{\"name\":\"_codeIndex\",\"type\":\"cell\"},{\"name\":\"_codeIndexBasis\",\"type\":\"cell\"},{\"name\":\"_indexDeployValue\",\"type\":\"uint128\"},{\"name\":\"_indexDestroyValue\",\"type\":\"uint128\"},{\"name\":\"_deployIndexBasisValue\",\"type\":\"uint128\"},{\"name\":\"_owner\",\"type\":\"uint256\"},{\"name\":\"_remainOnNft\",\"type\":\"uint128\"},{\"name\":\"_lastTokenId\",\"type\":\"uint128\"},{\"name\":\"_mintingFee\",\"type\":\"uint128\"}]}");
  }

  public FunctionCall<Void> mintNft(String json) {
    Map<String, Object> params = Map.of("json", json);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "mintNft", params, null);
  }

  public FunctionCall<Void> withdraw(Address dest, BigInteger value) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "withdraw", params, null);
  }

  public FunctionCall<Void> onTokenBurned(BigInteger id, Address owner, Address manager) {
    Map<String, Object> params = Map.of("id", id, 
        "owner", owner, 
        "manager", manager);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "onTokenBurned", params, null);
  }

  public FunctionCall<Void> setRemainOnNft(BigInteger remainOnNft) {
    Map<String, Object> params = Map.of("remainOnNft", remainOnNft);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "setRemainOnNft", params, null);
  }

  public FunctionCall<Void> setMintingFee(BigInteger mintingFee) {
    Map<String, Object> params = Map.of("mintingFee", mintingFee);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "setMintingFee", params, null);
  }

  public FunctionCall<ResultOfMintingFee> mintingFee(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfMintingFee>(sdk(), address(), abi(), credentials(), "mintingFee", params, null);
  }

  public FunctionCall<ResultOfOwner> owner(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfOwner>(sdk(), address(), abi(), credentials(), "owner", params, null);
  }

  public FunctionCall<Void> transferOwnership(BigInteger newOwner) {
    Map<String, Object> params = Map.of("newOwner", newOwner);
    return new FunctionCall<Void>(sdk(), address(), abi(), credentials(), "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfIndexBasisCode> indexBasisCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexBasisCode>(sdk(), address(), abi(), credentials(), "indexBasisCode", params, null);
  }

  public FunctionCall<ResultOfIndexBasisCodeHash> indexBasisCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexBasisCodeHash>(sdk(), address(), abi(), credentials(), "indexBasisCodeHash", params, null);
  }

  public FunctionCall<ResultOfResolveIndexBasis> resolveIndexBasis(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfResolveIndexBasis>(sdk(), address(), abi(), credentials(), "resolveIndexBasis", params, null);
  }

  public FunctionCall<ResultOfIndexCode> indexCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCode>(sdk(), address(), abi(), credentials(), "indexCode", params, null);
  }

  public FunctionCall<ResultOfIndexCodeHash> indexCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCodeHash>(sdk(), address(), abi(), credentials(), "indexCodeHash", params, null);
  }

  public FunctionCall<ResultOfGetJson> getJson(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfGetJson>(sdk(), address(), abi(), credentials(), "getJson", params, null);
  }

  public FunctionCall<ResultOfTotalSupply> totalSupply(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfTotalSupply>(sdk(), address(), abi(), credentials(), "totalSupply", params, null);
  }

  public FunctionCall<ResultOfNftCode> nftCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfNftCode>(sdk(), address(), abi(), credentials(), "nftCode", params, null);
  }

  public FunctionCall<ResultOfNftCodeHash> nftCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfNftCodeHash>(sdk(), address(), abi(), credentials(), "nftCodeHash", params, null);
  }

  public FunctionCall<ResultOfNftAddress> nftAddress(Integer answerId, BigInteger id) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "id", id);
    return new FunctionCall<ResultOfNftAddress>(sdk(), address(), abi(), credentials(), "nftAddress", params, null);
  }

  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(sdk(), address(), abi(), credentials(), "supportsInterface", params, null);
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
