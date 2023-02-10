package tech.deplant.java4ever.framework.contract;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.crypto.Credentials;

/**
 * Java wrapper class for usage of <strong>TIP4Collection</strong> contract for Everscale blockchain.
 */
public record TIP4Collection(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public FunctionCall<Void> mintNft(String json) {
    Map<String, Object> params = Map.of("json", json);
    return new FunctionCall<Void>(this, "mintNft", params, null);
  }

  public FunctionCall<Void> withdraw(Address dest, BigInteger value) {
    Map<String, Object> params = Map.of("dest", dest, 
        "value", value);
    return new FunctionCall<Void>(this, "withdraw", params, null);
  }

  public FunctionCall<Void> onTokenBurned(BigInteger id, Address owner, Address manager) {
    Map<String, Object> params = Map.of("id", id, 
        "owner", owner, 
        "manager", manager);
    return new FunctionCall<Void>(this, "onTokenBurned", params, null);
  }

  public FunctionCall<Void> setRemainOnNft(BigInteger remainOnNft) {
    Map<String, Object> params = Map.of("remainOnNft", remainOnNft);
    return new FunctionCall<Void>(this, "setRemainOnNft", params, null);
  }

  public FunctionCall<Void> setMintingFee(BigInteger mintingFee) {
    Map<String, Object> params = Map.of("mintingFee", mintingFee);
    return new FunctionCall<Void>(this, "setMintingFee", params, null);
  }

  public FunctionCall<ResultOfMintingFee> mintingFee(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfMintingFee>(this, "mintingFee", params, null);
  }

  public FunctionCall<ResultOfOwner> owner(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfOwner>(this, "owner", params, null);
  }

  public FunctionCall<Void> transferOwnership(BigInteger newOwner) {
    Map<String, Object> params = Map.of("newOwner", newOwner);
    return new FunctionCall<Void>(this, "transferOwnership", params, null);
  }

  public FunctionCall<ResultOfIndexBasisCode> indexBasisCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexBasisCode>(this, "indexBasisCode", params, null);
  }

  public FunctionCall<ResultOfIndexBasisCodeHash> indexBasisCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexBasisCodeHash>(this, "indexBasisCodeHash", params, null);
  }

  public FunctionCall<ResultOfResolveIndexBasis> resolveIndexBasis(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfResolveIndexBasis>(this, "resolveIndexBasis", params, null);
  }

  public FunctionCall<ResultOfIndexCode> indexCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCode>(this, "indexCode", params, null);
  }

  public FunctionCall<ResultOfIndexCodeHash> indexCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfIndexCodeHash>(this, "indexCodeHash", params, null);
  }

  public FunctionCall<ResultOfGetJson> getJson(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfGetJson>(this, "getJson", params, null);
  }

  public FunctionCall<ResultOfTotalSupply> totalSupply(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfTotalSupply>(this, "totalSupply", params, null);
  }

  public FunctionCall<ResultOfNftCode> nftCode(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfNftCode>(this, "nftCode", params, null);
  }

  public FunctionCall<ResultOfNftCodeHash> nftCodeHash(Integer answerId) {
    Map<String, Object> params = Map.of("answerId", answerId);
    return new FunctionCall<ResultOfNftCodeHash>(this, "nftCodeHash", params, null);
  }

  public FunctionCall<ResultOfNftAddress> nftAddress(Integer answerId, BigInteger id) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "id", id);
    return new FunctionCall<ResultOfNftAddress>(this, "nftAddress", params, null);
  }

  public FunctionCall<ResultOfSupportsInterface> supportsInterface(Integer answerId,
      Integer interfaceID) {
    Map<String, Object> params = Map.of("answerId", answerId, 
        "interfaceID", interfaceID);
    return new FunctionCall<ResultOfSupportsInterface>(this, "supportsInterface", params, null);
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
