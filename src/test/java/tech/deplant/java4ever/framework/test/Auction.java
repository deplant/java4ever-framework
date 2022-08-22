package tech.deplant.java4ever.framework.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.JSONContext;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.ArtifactABI;
import tech.deplant.java4ever.framework.contract.OwnedContract;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.util.Map;

public class Auction extends OwnedContract {

    public Auction(Sdk sdk, Address address, Credentials owner) {
        super(sdk, address, ArtifactABI.ofResource(""), owner);
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

//    public static Auction ofConfig(Sdk sdk, Artifact artifact) throws JsonProcessingException {
//        return new Auction(ControllableContract.ofConfig(sdk, artifact, "auction"));
//    }

    public Object getBidCount() {
        return runGetter("_bidCount", null, null).get("_bidCount");
    }

    public Object getRevealCount() {
        return runGetter("_revealCount", null, null).get("_revealCount");
    }

    public AuctionRules getRules() throws JsonProcessingException {
        return JSONContext.MAPPER.readValue(
                runGetter("_rules", null, null).get("_rules").toString(),
                AuctionRules.class
        );
    }

    public BigInteger getCommitHash(BigInteger price, BigInteger amount, BigInteger salt) {
        var inputs = Map.<String, Object>of(
                "priceReveal_", price,
                "amountReveal_", amount,
                "salt_", salt);
        return new BigInteger(runGetter("getCommitHash", inputs, null).get("bidHash").toString());
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