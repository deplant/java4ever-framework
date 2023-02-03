package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.utils.Strings;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HexFormat;
import java.util.TimeZone;


public class Convert {

	private static System.Logger logger = System.getLogger(Convert.class.getName());

	public static BigInteger toNanos(String number, CurrencyUnit unit) {
		return toNanos(new BigDecimal(number), unit);
	}

	public static BigInteger toNanos(BigDecimal number, CurrencyUnit unit) {
		return number.multiply(unit.factor()).toBigInteger();
	}

	public static String base64ToHexString(String base64string) {
		return HexFormat.of().formatHex(Base64.getDecoder().decode(base64string));
	}

	public static LocalDateTime longToDateTime(long unixTime) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime),
		                               TimeZone.getTimeZone("Europe/Moscow").toZoneId());
	}

	public static String dateTimeToString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public static String decimalToString(BigDecimal decimalAmount, int scale) {
		return decimalAmount.movePointRight(scale).toBigIntegerExact().toString();
	}

	public static BigInteger hexToBigInt(String hexUint) {
		if (hexUint.startsWith("0x")) {
			hexUint = hexUint.substring(2);
		}
		return new BigInteger(hexUint, 16);
	}

	public static BigDecimal hexToDec(String stringAmount, int scale) {
		if (stringAmount.startsWith("-0x")) {
			stringAmount = "-" + stringAmount.substring(3);
		} else if (stringAmount.startsWith("0x")) {
			stringAmount = stringAmount.substring(2);
		}
		return stringToDecimal(stringAmount, scale, 16);
	}


	public static BigDecimal hexToDecOrZero(String stringAmount, int scale) {
		if (Strings.isEmpty(stringAmount)) {
			return BigDecimal.ZERO;
		}
		return hexToDec(stringAmount, scale);
	}

	public static BigDecimal strToDec(String stringAmount, int scale) {
		return stringToDecimal(stringAmount, scale, 10);
	}

	private static BigDecimal stringToDecimal(String stringAmount, int scale, int base) {
		return new BigDecimal(new BigInteger(stringAmount, base), scale);
	}

	/**
	 * Utility method for preparing hex strings
	 *
	 * @param text Text string to encode.
	 * @return Hex string
	 */
	public static String strToHex(String text) {
		final byte[] data;
		final char[] hexCode = "0123456789ABCDEF".toCharArray();
		StringBuilder r;
		try {
			data = text.getBytes(StandardCharsets.UTF_8.name());
			r = new StringBuilder(data.length * 2);
			for (byte b : data) {
				r.append(hexCode[(b >> 4) & 0xF]);
				r.append(hexCode[(b & 0xF)]);
			}
			return r.toString();
		} catch (UnsupportedEncodingException e) {
			logger.log(System.Logger.Level.ERROR, () -> e.getMessage());
			return null;
		}


	}

	public static String hexToStr(String text) {
		final int len = text.length();

		// "111" is not a valid hex encoding.
		if (len % 2 != 0) {
			throw new IllegalArgumentException("hexBinary needs to be even-length: " + text);
		}
		byte[] out = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			int h = hexCharToBin(text.charAt(i));
			int l = hexCharToBin(text.charAt(i + 1));
			if (h == -1 || l == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + text);
			}
			out[i / 2] = (byte) (h * 16 + l);
		}
		return new String(out, StandardCharsets.UTF_8);
	}

	public static String padLeftZeros(String inputString, int length) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append('0');
		}
		sb.append(inputString);

		return sb.toString();
	}

	/**
	 * Custom hex2bin function based on new switch-case syntax
	 *
	 * @param hex Char to convert to binary int
	 * @return Binary integer value, -1 for wrong values
	 */
	private static int hexCharToBin(char hex) {
		return switch (hex) {
			case '0' -> 0b0000;
			case '1' -> 0b0001;
			case '2' -> 0b0010;
			case '3' -> 0b0011;
			case '4' -> 0b0100;
			case '5' -> 0b0101;
			case '6' -> 0b0110;
			case '7' -> 0b0111;
			case '8' -> 0b1000;
			case '9' -> 0b1001;
			case 'A' -> 0b1010;
			case 'B' -> 0b1011;
			case 'C' -> 0b1100;
			case 'D' -> 0b1101;
			case 'E' -> 0b1110;
			case 'F' -> 0b1111;
			default -> -1;
		};
	}

}
