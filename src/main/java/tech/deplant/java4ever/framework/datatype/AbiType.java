package tech.deplant.java4ever.framework.datatype;

import tech.deplant.commons.regex.*;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;

import java.util.Locale;
import java.util.regex.Pattern;

public record AbiType(AbiTypePrefix prefix, int size, boolean isArray) {

	private final static System.Logger logger = System.getLogger(AbiType.class.getName());

	public AbiType(AbiTypePrefix prefix, int size) {
		this(prefix, size, false);
	}

	public AbiType(AbiTypePrefix prefix) {
		this(prefix, 0, false);
	}

	public static AbiType of(String typeString) throws EverSdkException {
		if (typeString.contains("optional")) {
			return new AbiType(AbiTypePrefix.OPTIONAL, 0, false);
		}

		// Size pattern matching
		var expr = new Then(new GroupOf(new Occurences(new AnyOf(new Word("a-zA-Z")), 1)),
		                    new GroupOf(new Occurences(Special.DIGIT, 1, 3))).toPattern();
		var matcher = expr.matcher(typeString);
		while (matcher.find()) {
			return new AbiType(AbiTypePrefix.valueOf(matcher.group(1).toUpperCase()),
			                   Integer.parseInt(matcher.group(2)),
			                   arrayMatcher(typeString));
		}
		// Type pattern  matching
		matcher = Pattern.compile("([a-zA-Z]+)").matcher(typeString);
		while (matcher.find()) {
			return new AbiType(AbiTypePrefix.valueOf(matcher.group(1).toUpperCase()),
			                   0,
			                   arrayMatcher(typeString));
		}
		var ex = new EverSdkException(new EverSdkException.ErrorResult(-300,
		                                                               "ABI Type parsing failed! Type: " + typeString),
		                              new RuntimeException());
		logger.log(System.Logger.Level.WARNING, () -> "ABI type parsing failed! Type: " + typeString);
		throw ex;
	}

	public static boolean arrayMatcher(String typeString) {
		var arrayPattern = new Then(new GroupOf(new Then(new Occurences(new AnyOf(new Word("a-zA-Z")), 1),
		                                                 new Occurences(Special.DIGIT, 0, 3))),
		                            new GroupOf(new Then(new Symbol('['), new Symbol(']')))).toPattern();
		var matcher = arrayPattern.matcher(typeString);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	boolean hasSize() {
		return switch (prefix()){
			case INT, UINT, BYTES -> {
				if (size() > 0) {
					yield true;
				} else {
					yield false;
				}
			}
			default -> false;
		};
	}

	public Abi.AbiParam toAbiParam(String paramName, Abi.AbiParam[] components) {
		return new Abi.AbiParam(paramName, abiName(), null);
	}

	public String abiName() {
		var builder = new StringBuilder();
		builder.append(prefix().name().toLowerCase(Locale.ROOT));
		if (hasSize()) {
			builder.append(size());
		}
		return builder.toString();
	}

}
