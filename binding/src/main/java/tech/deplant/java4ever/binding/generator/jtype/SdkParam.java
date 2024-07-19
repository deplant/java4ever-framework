package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.TypeReference;
import tech.deplant.javapoet.AnnotationSpec;
import tech.deplant.javapoet.ParameterSpec;
import tech.deplant.javapoet.TypeName;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

import java.util.Map;
import java.util.Objects;

public record SdkParam(TypeName refClassName,
                       String parameterName,
                       String origParamName,
                       boolean hasReserved,
                       String summary,
                       String description,
                       SdkObject libType) {

	public final static Map<String, String> RESERVED_FIELD_NAMES = Map.of("public", "public_key",
	                                                                      "secret", "secret_key",
	                                                                      "switch", "switch_to",
	                                                                      "ABI version", "abi_version_major");

	public static SdkParam ofApiType(ApiType paramType,
	                                 Map<ParserEngine.SdkType, SdkObject> typeLibrary) {

		// TYPE NAME
		var typeReference = TypeReference.fromApiType(paramType);
		var javaType = typeReference.toTypeDeclaration(typeLibrary);
		TypeName className;
		String paramName = "";
		String reservedName = "";
		boolean hasReserved = false;

		if (javaType instanceof SdkDummy dummy) {
			className = TypeReference.fromApiType(dummy.type()).toTypeName();
		} else {
			className = typeReference.toTypeName();
		}

		if ("Context".equals(className.toString())) {
			paramName = "ctx";
		} else {
			paramName = paramType.name(); //ParserUtils.camelCase(paramType.name());
		}

		if (Objects.nonNull(paramName)) {
			reservedName = RESERVED_FIELD_NAMES.getOrDefault(paramName, paramName); // checks for reserved words or defaults to paramName
			hasReserved = !reservedName.equals(paramName);
		}

		return new SdkParam(className,
		                    ParserUtils.camelCase(reservedName),
		                    paramName,
		                    hasReserved,
		                    paramType.summary(),
		                    paramType.description(),
		                    typeReference.toTypeDeclaration(typeLibrary));
	}

	public static AnnotationSpec renamedFieldAnnotation(String originalName) {
		return AnnotationSpec.builder(JsonProperty.class)
		                     .addMember("value", "$S", originalName)
		                     .build();
	}

	public ParameterSpec.Builder poeticize() {

		var componentBuilder = ParameterSpec.builder(refClassName(), parameterName());
		// ANNOTATION
		if (hasReserved()) {
			// if name was changed by reserved word filter
			// save original name as annotation for Jackson
			componentBuilder.addAnnotation(renamedFieldAnnotation(origParamName()));
		}
		// JAVADOC
		componentBuilder.addJavadoc(new SdkDocs(summary(), description()).poeticize().build());
		// OK
		return componentBuilder;
	}

}
