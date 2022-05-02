package tech.deplant.java4ever.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.framework.type.AbiAddressAdapter;
import tech.deplant.java4ever.framework.type.UintBigIntegerAdapter;

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

@Log4j2
public class Data {

    //TODO Move to appropriate class
    public static final BigInteger NANOTON = new BigInteger("1"); // A1 == 1
    public static final BigInteger NANOEVER = new BigInteger("1");// A1 == 1_== 1E-9 EVER
    public static final BigInteger MICROTON = new BigInteger("1000"); // A6 == 1_000
    public static final BigInteger MICROEVER = new BigInteger("1000"); // A6 == 1_000 == 1E-6 EVER
    public static final BigInteger MILLITON = new BigInteger("1000000"); // A8 == 1_000 000
    public static final BigInteger MILLIEVER = new BigInteger("1000000"); // A8 == 1_000 000 == 1E-3 EVER
    public static final BigInteger TON = new BigInteger("1000000000"); // A3 == 1_000 000 000 (1E9)
    public static final BigInteger EVER = new BigInteger("1000000000"); // A4 == 1_000 000 000 (1E9)
    public static final BigInteger KILOTON = new BigInteger("1000000000000"); // A9 == 1_000 000 000 000 (1E12)
    public static final BigInteger KILOEVER = new BigInteger("1000000000000"); // A9 == 1_000 000 000 000 (1E12) == 1E3 EVER
    public static final BigInteger MEGATON = new BigInteger("1000000000000000"); // A11 == 1_000 000 000 000 000 (1E15)
    public static final BigInteger MEGAEVER = new BigInteger("1000000000000000"); // A11 == 1_000 000 000 000 000 (1E15) == 1E6 EVER
    public static final BigInteger GIGATON = new BigInteger("1000000000000000000"); // A13 == 1_000 000 000 000 000 000 (1E18)
    public static final BigInteger GIGAEVER = new BigInteger("1000000000000000000"); // A13 == 1_000 000 000 000 000 000 (1E18) == 1E9 EVER
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BigInteger.class, new UintBigIntegerAdapter())
            .registerTypeAdapter(Address.class, new AbiAddressAdapter())
            .create();

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
        if (stringAmount.startsWith("0x")) {
            stringAmount = stringAmount.substring(2);
        }
        return stringToDecimal(stringAmount, scale, 16);
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
     * @throws UnsupportedEncodingException
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
            log.error(e.getMessage());
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
