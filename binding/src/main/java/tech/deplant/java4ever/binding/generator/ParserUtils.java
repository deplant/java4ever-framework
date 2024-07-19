package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.deplant.javapoet.AnnotationSpec;
import tech.deplant.javapoet.ParameterSpec;
import tech.deplant.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Set;

public class ParserUtils {


	public final static Set<String> JAVA_RESERVED_WORDS = Set.of("abstract",
	                                                             "continue",
	                                                             "for",
	                                                             "new",
	                                                             "switch",
	                                                             "assert",
	                                                             "default",
	                                                             "goto",
	                                                             "package",
	                                                             "synchronize",
	                                                             "boolean",
	                                                             "do",
	                                                             "if",
	                                                             "private",
	                                                             "this",
	                                                             "break",
	                                                             "double",
	                                                             "implements",
	                                                             "protected",
	                                                             "throw",
	                                                             "byte",
	                                                             "else",
	                                                             "import",
	                                                             "public",
	                                                             "throws",
	                                                             "case",
	                                                             "enum",
	                                                             "instanceof",
	                                                             "return",
	                                                             "transient",
	                                                             "catch",
	                                                             "extends",
	                                                             "int",
	                                                             "short",
	                                                             "try",
	                                                             "char",
	                                                             "final",
	                                                             "interface",
	                                                             "static",
	                                                             "void",
	                                                             "class",
	                                                             "finally",
	                                                             "long",
	                                                             "strictfp",
	                                                             "volatile",
	                                                             "const",
	                                                             "float",
	                                                             "native",
	                                                             "super",
	                                                             "while");

	// Null-safe length
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	// Copy of StringUtils.capitalize() of Apache Commons
	public static String capitalize(final String str) {
		final int strLen = length(str);
		if (strLen == 0) {
			return str;
		}

		final int firstCodepoint = str.codePointAt(0);
		final int newCodePoint = Character.toTitleCase(firstCodepoint);
		if (firstCodepoint == newCodePoint) {
			// already capitalized
			return str;
		}

		final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
		int outOffset = 0;
		newCodePoints[outOffset++] = newCodePoint; // copy the first code point
		for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
			final int codePoint = str.codePointAt(inOffset);
			newCodePoints[outOffset++] = codePoint; // copy the remaining ones
			inOffset += Character.charCount(codePoint);
		}
		return new String(newCodePoints, 0, outOffset);
	}

	public static String camelCase(final String str) {
		final String[] wordArray = str.split("_");
		final StringBuilder builder = new StringBuilder();
		builder.append(wordArray[0].toLowerCase());
		Arrays.stream(wordArray)
		      .skip(1)
		      .forEach(w -> {
			      builder.append(w.substring(0, 1).toUpperCase());
			      builder.append(w.substring(1).toLowerCase());
		      });
		return builder.toString();
	}

	public static String toCapitalCase(final String str) {
		return capitalize(camelCase(str));
	}


	public static AnnotationSpec renamedFieldAnnotation(String originalName) {
		return AnnotationSpec.builder(JsonProperty.class)
		                     .addMember("value", "$S", originalName)
		                     .build();
	}


	public static ParameterSpec.Builder processReservedNames(TypeName paramType, String paramName) {
		ParameterSpec.Builder paramBuilder;
		if (JAVA_RESERVED_WORDS.contains(paramName)) {
			paramBuilder = ParameterSpec.builder(paramType, "_" + paramName);
			paramBuilder.addAnnotation(renamedFieldAnnotation(paramName));
		} else if (paramName.contains(" ")) {
			paramBuilder = ParameterSpec.builder(paramType, paramName.trim().replace(" ","__"));
			paramBuilder.addAnnotation(renamedFieldAnnotation(paramName));
		} else {
			paramBuilder = ParameterSpec.builder(paramType, paramName);
		}
		return paramBuilder;
	}

}
