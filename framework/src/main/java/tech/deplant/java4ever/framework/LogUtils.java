package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Net;

import java.util.function.Supplier;

/**
 * The type Log utils.
 */
public class LogUtils {

	/**
	 * The constant CALL_LOG_BLOCK.
	 */
	public final static String CALL_LOG_BLOCK = """
			\n|-----------------------------------------------------------
			|%s (%s): 
			|  TR_ID: %s
			|  MSG_ID: %s
			|  (%s)--{%s E}-->(%s)
			|  Result: %d (%s)
			|  Fees: %s E
			|  Out Messages: [%s]
			|-----------------------------------------------------------
			""";

	/**
	 * Enquoted list agg string.
	 *
	 * @param elements the elements
	 * @return the string
	 */
	public static String enquotedListAgg(String[] elements) {
		return "\"" + String.join("\",\"", elements) + "\"";
	}

	/**
	 * Dest of message string.
	 *
	 * @param messageNode the message node
	 * @return the string
	 */
	public static String destOfMessage(Net.MessageNode messageNode) {
		return messageNode.dst().length() > 0 ? messageNode.dst() : "ext";
	}

	/**
	 * Source of message string.
	 *
	 * @param messageNode the message node
	 * @return the string
	 */
	public static String sourceOfMessage(Net.MessageNode messageNode) {
		return messageNode.src().length() > 0 ? messageNode.src() : "ext";
	}

	/**
	 * Type of message string.
	 *
	 * @param messageNode the message node
	 * @return the string
	 */
	public static String typeOfMessage(Net.MessageNode messageNode) {
		if (sourceOfMessage(messageNode) == "ext") {
			return "EXTERNAL CALL";
		} else if (destOfMessage(messageNode) == "ext") {
			return "EVENT";
		} else {
			return "INTERNAL MSG";
		}
	}

	/**
	 * Name of message string.
	 *
	 * @param messageNode the message node
	 * @return the string
	 */
	public static String nameOfMessage(Net.MessageNode messageNode) {
		if (messageNode.decodedBody() == null || messageNode.decodedBody().name() == null) {
			return "Unknown";
		} else {
			return messageNode.decodedBody().name();
		}
	}


	/**
	 * Trace.
	 *
	 * @param logger  the logger
	 * @param message the message
	 */
	public static void trace(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.TRACE, () -> message);
	}

	/**
	 * Trace.
	 *
	 * @param logger      the logger
	 * @param lazyMessage the lazy message
	 */
	public static void trace(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.TRACE, lazyMessage);
	}

	/**
	 * Debug.
	 *
	 * @param logger  the logger
	 * @param message the message
	 */
	public static void debug(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.DEBUG, () -> message);
	}

	/**
	 * Debug.
	 *
	 * @param logger      the logger
	 * @param lazyMessage the lazy message
	 */
	public static void debug(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.DEBUG, lazyMessage);
	}

	/**
	 * Info.
	 *
	 * @param logger  the logger
	 * @param message the message
	 */
	public static void info(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.INFO, () -> message);
	}

	/**
	 * Info.
	 *
	 * @param logger      the logger
	 * @param lazyMessage the lazy message
	 */
	public static void info(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.INFO, lazyMessage);
	}

	/**
	 * Warn.
	 *
	 * @param logger  the logger
	 * @param message the message
	 */
	public static void warn(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.WARNING, () -> message);
	}

	/**
	 * Warn.
	 *
	 * @param logger      the logger
	 * @param lazyMessage the lazy message
	 */
	public static void warn(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.WARNING, lazyMessage);
	}

	/**
	 * Error.
	 *
	 * @param logger  the logger
	 * @param message the message
	 */
	public static void error(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.ERROR, () -> message);
	}

	/**
	 * Error.
	 *
	 * @param logger      the logger
	 * @param lazyMessage the lazy message
	 */
	public static void error(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.ERROR, lazyMessage);
	}
}
