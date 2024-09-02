package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.commons.Strings;
import tech.deplant.commons.regex.*;
import tech.deplant.javapoet.CodeBlock;

import static java.util.Objects.requireNonNullElse;

public record SdkDocs(String summary, String description) {

	public CodeBlock.Builder poeticize() {
		CodeBlock.Builder docsBuilder = CodeBlock.builder();

		var mdLinkRegExp = new Then(new Symbol('['),
		                            new NoneOf(new Word("[]()")),
		                            Special.PLUS,
		                            new Symbol(']'),
		                            new Symbol('('),
		                            new NoneOf(new Word("[]()")),
		                            Special.PLUS,
		                            new Symbol(')'));
		String mdLinksPatternString = mdLinkRegExp.toString();


		String processedDescription = description();
		if (Strings.isNotEmpty(processedDescription)) {
			processedDescription = processedDescription
					.replace("<", "&lt;")
					.replace(">", "&gt;");
		}

		//TODO Do something with MD-style links in description

		String processedSummary = summary();
		if (Strings.isNotEmpty(processedSummary)) {
			processedSummary = processedSummary
					.replace("<", "&lt;")
					.replace(">", "&gt;");
		}
		//TODO Do something with MD-style links in summary

		if (processedDescription != null || processedSummary != null) {
			docsBuilder.add(String.format("%s %s\n",
			                              requireNonNullElse(processedDescription, ""),
			                              requireNonNullElse(processedSummary, "")));
		}
		return docsBuilder;
	}
}
