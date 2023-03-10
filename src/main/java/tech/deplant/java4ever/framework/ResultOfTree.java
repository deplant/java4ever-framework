package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Net;

public record ResultOfTree<RETURN>(Net.ResultOfQueryTransactionTree queryTree,
                                   RETURN decodedOutuput) {
}
