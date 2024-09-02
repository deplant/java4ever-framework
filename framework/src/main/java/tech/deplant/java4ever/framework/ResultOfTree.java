package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Net;
import tech.deplant.commons.Objs;

import java.util.Arrays;

/**
 * The type Result of tree.
 *
 * @param <RETURN> the type parameter
 */
public record ResultOfTree<RETURN>(Net.ResultOfQueryTransactionTree queryTree,
                                   RETURN decodedOutput) {

	/**
	 * Extracts address where internal deploy message was sent.
	 * Method checks messages list for specific sender and if
	 * he sent messages to "constructor" of other contract,
	 * destination address will be returned.
	 *
	 * @param sender Message sender that deploys new contract
	 * @return New contract address
	 */
	public String extractDeployAddress(String sender) {
		return Arrays
				.stream(queryTree().messages())
				.filter(msg ->
						        msg.src().equals(sender) &&
						        Objs.isNotNull(msg.decodedBody()) &&
						        Objs.isNotNull(msg.decodedBody().name()) &&
						        msg.decodedBody().name().equals("constructor"))
				.findFirst()
				.orElseThrow()
				.dst();
	}
}
