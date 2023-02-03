package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Net;

import java.util.function.Supplier;

public class LogUtils {

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

	public static String enquotedListAgg(String[] elements) {
		return "\"" + String.join("\",\"", elements) + "\"";
	}

	public static String destOfMessage(Net.MessageNode messageNode) {
		return messageNode.dst().length() > 0 ? messageNode.dst() : "ext";
	}

	public static String sourceOfMessage(Net.MessageNode messageNode) {
		return messageNode.src().length() > 0 ? messageNode.src() : "ext";
	}

	public static String typeOfMessage(Net.MessageNode messageNode) {
		if (sourceOfMessage(messageNode) == "ext") {
			return "EXTERNAL CALL";
		} else if (destOfMessage(messageNode) == "ext") {
			return "EVENT";
		} else {
			return "INTERNAL MSG";
		}
	}

	public static String nameOfMessage(Net.MessageNode messageNode) {
		if (messageNode.decodedBody() == null || messageNode.decodedBody().name() == null) {
			return "Unknown";
		} else {
			return messageNode.decodedBody().name();
		}
	}


	public static void trace(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.TRACE, () -> message);
	}

	public static void trace(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.TRACE, lazyMessage);
	}

	public static void debug(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.DEBUG, () -> message);
	}

	public static void debug(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.DEBUG, lazyMessage);
	}

	public static void info(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.INFO, () -> message);
	}

	public static void info(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.INFO, lazyMessage);
	}

	public static void warn(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.WARNING, () -> message);
	}

	public static void warn(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.WARNING, lazyMessage);
	}

	public static void error(final System.Logger logger, final String message) {
		logger.log(System.Logger.Level.ERROR, () -> message);
	}

	public static void error(final System.Logger logger, final Supplier<String> lazyMessage) {
		logger.log(System.Logger.Level.ERROR, lazyMessage);
	}
}
