package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Objs;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Unstable;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.TypeReference;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record SdkFunction(String functionModule,
                          ApiFunction function,
                          //JavaType functionReturn,
                          Map<ParserEngine.SdkType, SdkObject> typeLibrary) {

	private final static System.Logger logger = System.getLogger(SdkFunction.class.getName());

	private String constructCallParams(MethodSpec.Builder methodBuilder,
	                                   List<Object> statementArgs,
	                                   SdkParam param) {
		StringBuilder template = new StringBuilder();
		if (Objs.isNotNull(param.libType()) &&
		    param.libType().isStructure() &&
		    Strings.safeSubstrEquals(param.libType().name(), 0, 8, false, "ParamsOf")
		) {
			StructType structType = (StructType) param.libType().type();
			template.append("new $T(");
			statementArgs.add(param.refClassName());

			String innerFields = Arrays
					.stream(structType.struct_fields())
					.map(
							apiType -> constructCallParams(methodBuilder,
							                               statementArgs,
							                               SdkParam.ofApiType(apiType,
							                                                  typeLibrary())))
					.collect(Collectors.joining(", "));
			template.append(innerFields);
			template.append(")");
		} else {
			template.append("$N");
			var spec = param.poeticize().build();
			methodBuilder.addParameter(spec);
			statementArgs.add(spec);
		}
		return template.toString();
	}

	public MethodSpec.Builder poeticize() {

		// METADATA
		var methodBuilder = MethodSpec
				.methodBuilder(ParserUtils.camelCase(function().name()))
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addException(ClassName.get(EverSdkException.class));

		// BODY
		// Args for all call params
		List<Object> statementArgs = new ArrayList<>();
		// Context param
		//methodBuilder.addParameter(ClassName.get(EverSdkContext.class), "ctx");
		// Function SDK name
		// adds function name as a first arg to statementArgs array
		statementArgs.add(String.format("%s.%s", functionModule().toLowerCase(), function().name()));
		// call template for all variants
		String templateString = "%RETURN_KEY%EverSdk.%CALL_TYPE%(ctxId, $S, %PARAMS%%RETURN_CLASS%%APP_OBJ%)";
		methodBuilder.addParameter(ClassName.INT, "ctxId");
		for (ApiType param : function().params()) {
			logger.log(System.Logger.Level.TRACE, () -> function().name() + "\\" + param.name() + "\\" + param.type());
			switch (param.name()) {
				case "context", "_context" -> {
					// we're always adding context, so no reason to do something
				}
				case "params" -> {
					SdkParam parsedParam = SdkParam.ofApiType(param, typeLibrary());
					templateString = templateString.replace("%PARAMS%", constructCallParams(methodBuilder,
					                                                                        statementArgs,
					                                                                        parsedParam));
				}
				case "callback" -> {
					templateString = templateString.replace("%APP_OBJ%", ", callback");
					templateString = templateString.replace("%CALL_TYPE%", "asyncCallback");
					var paramClass = ClassName.get(JsonNode.class);//ClassName.get(CallbackHandler.class);
					methodBuilder.addParameter(ParameterizedTypeName.get(ClassName.get(Consumer.class), paramClass),
					                           "callback");
					//methodBuilder.addParameter(ClassName.get(EverSdkSubscription.class), "eventHandler");
				}
				case "app_object", "password_provider" -> {
					templateString = templateString.replace("%APP_OBJ%", ", appObject");
					templateString = templateString.replace("%CALL_TYPE%", "asyncAppObject");
					if (param instanceof GenericType gen) {
//						TypeName[] appObjectParams = Arrays.stream(gen.generic_args()).map(arg -> switch(arg) {
//							case null -> null;
//							case RefType ref -> SdkParam.ofApiType(ref, typeLibrary());
//							default -> null;
//						}).filter(Objects::nonNull).map(SdkParam::refClassName).toArray(TypeName[]::new);
//						appObjectParams[0].
						String refName = switch(gen.generic_args()[0]) {
							case null -> null;
							case RefType ref -> ref.ref_name().split("\\.")[1];
							default -> null;
						};
						String appObjectInterfaceName = Strings.substr(refName, 8);
						try {
							methodBuilder.addParameter(ClassName.bestGuess(appObjectInterfaceName), "appObject");
						} catch (IllegalArgumentException e) {
							logger.log(System.Logger.Level.ERROR, function().name());
							logger.log(System.Logger.Level.ERROR, appObjectInterfaceName);
							throw e;
						}


					}
				}
				default -> logger.log(System.Logger.Level.WARNING, () -> "Unknown parameter: " + param.name());
			}
		}

		templateString = templateString.replace("%APP_OBJ%", "");
		templateString = templateString.replace("%PARAMS%", "null");

		if (Objs.isNotNull(function().result())) {
			var resultReference = TypeReference.fromApiType(function().result());
			if (!resultReference.isVoid()) {
				//
				templateString = templateString.replace("%RETURN_KEY%", "return ");
				templateString = templateString.replace("%CALL_TYPE%", "async");
				templateString = templateString.replace("%RETURN_CLASS%", ", $T.class");
				var typeName = resultReference.toTypeName();
				// adds return class to method builder
				methodBuilder.returns(ParameterizedTypeName.get(ClassName.get(CompletableFuture.class), typeName));
				// adds return class as a final arg to statementArgs array
				statementArgs.add(typeName);
			}
		}
		// void result
		templateString = templateString.replace("%RETURN_KEY%", "");
		templateString = templateString.replace("%CALL_TYPE%", "asyncVoid");
		templateString = templateString.replace("%RETURN_CLASS%", "");

		final String finalTemplateString = templateString;
		logger.log(System.Logger.Level.TRACE, () -> "Template: " + finalTemplateString);
		statementArgs.forEach(arg -> logger.log(System.Logger.Level.TRACE, () -> "Arg[]: " + arg.toString()));

		methodBuilder.addCode(CodeBlock
				                      .builder()
				                      .addStatement(
						                      templateString,
						                      statementArgs.toArray())
				                      .build());

		// check for Unstable notation in comments
		if ((Objs.isNotNull(function().summary()) && function().summary().toUpperCase().contains("UNSTABLE")) ||
		    (Objs.isNotNull(function().description()) && function().description().toUpperCase().contains("UNSTABLE"))) {
			methodBuilder.addAnnotation(AnnotationSpec.builder(Unstable.class).build());
		}

		// check for Deprecated notation in comments
		if ((Objs.isNotNull(function().summary()) && function().summary().toUpperCase().contains("DEPRECATED")) ||
		    (Objs.isNotNull(function().description()) &&
		     function().description().toUpperCase().contains("DEPRECATED"))) {
			methodBuilder.addAnnotation(AnnotationSpec.builder(Deprecated.class).build());
		}

		// JAVADOC
		methodBuilder.addJavadoc(new SdkDocs(function().summary(), function().description()).poeticize().build());

		return methodBuilder;
	}
}