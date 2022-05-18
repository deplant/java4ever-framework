package tech.deplant.java4ever.framework.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Data;
import tech.deplant.java4ever.framework.JSONContext;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.contract.ControllableContract;
import tech.deplant.java4ever.framework.contract.IContract;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Auction extends ControllableContract {

    public Auction(IContract contract) {
        super(contract.sdk(), contract.address(), contract.tvmKey(), contract.abi());
    }

//    public static Auction ofLocalConfig(ExplorerConfig config, Sdk sdk, boolean isBuyNever) {
//        var str = isBuyNever ? "auctionEVERtoNEVER" : "auctionNEVERtoEVER";
//        try {
//            return new Auction(config.accountController(str, sdk));
//        } catch (Sdk.SdkException e) {
//            config.removeAccountController(str);
//            throw e;
//        } catch (NullPointerException ex) {
//            config.removeAccountController(str);
//            log.warn("Auction not found!");
//            return null;
//        }
//    }

//    public void storeTo(ExplorerConfig config, boolean isBuyNever) {
//        var str = isBuyNever ? "auctionEVERtoNEVER" : "auctionNEVERtoEVER";
//        config.addAccountController(str, this.controller());
//    }

    public static Auction ofConfig(Sdk sdk, Artifact artifact) throws JsonProcessingException {
        return new Auction(ControllableContract.ofConfig(sdk, artifact, "auction"));
    }

    public CompletableFuture<Object> getBidCount() {
        return runGetter("_bidCount", null, null).thenApply(map -> map.get("_bidCount"));
    }

    public CompletableFuture<Object> getRevealCount() {
        return runGetter("_revealCount", null, null).thenApply(map -> map.get("_revealCount"));
    }

    public AuctionRules getRules() throws JsonProcessingException {
        return JSONContext.MAPPER.readValue(
                runGetter("_rules", null, null)
                        .thenApply(map -> map.get("_rules")).toString(),
                AuctionRules.class
        );
    }

    public CompletableFuture<BigInteger> getCommitHash(BigInteger price, BigInteger amount, BigInteger salt) {
        var inputs = Map.<String, Object>of(
                "priceReveal_", price,
                "amountReveal_", amount,
                "salt_", salt);
        return runGetter("getCommitHash", inputs, null).thenApply(
                map -> Data.hexToBigInt(map.get("bidHash").toString())
        );
    }

    public record AuctionRules(
            boolean isBuyNEVER,
            long startDate,
            long revealDate,
            long tradeDate,
            long closeDate,
            BigInteger minBid,
            BigInteger minPrice,
            BigInteger loserFactor) {
    }
}