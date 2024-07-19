package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.commons.Objs;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.generator.jtype.*;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.java4ever.binding.io.JsonResource;
import tech.deplant.javapoet.CodeBlock;
import tech.deplant.javapoet.JavaFile;
import tech.deplant.javapoet.MethodSpec;
import tech.deplant.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.requireNonNullElse;

public class ParserEngine {

	private final static System.Logger logger = System.getLogger(ParserEngine.class.getName());

	public static ApiReference ofJsonResource(String resourceName) throws JsonProcessingException {
		return JsonContext.SDK_JSON_MAPPER().readValue(new JsonResource(resourceName).get(),
		                                               ApiReference.class);
	}

	public static void parse(ApiReference parsedApiReference) throws IOException {

		final String apiVersion = parsedApiReference.version();
		// library to store all parsed types
		// they'll be used later to construct correct method params and bodies
		final Map<SdkType, SdkObject> typeLibrary = new HashMap<>();
		// library to store references to child classes from EnumOfTypes
		final Map<SdkType, SdkInterfaceParent> eotReferences = new HashMap<>();

		// type parsing loop
		// we parse all modules to get info about all possible types
		// that's because function of one module can depend on type from other module
		// so we need to know about other modules types
		for (var module : parsedApiReference.modules()) {
			String moduleCapitalName = ParserUtils.capitalize(module.name());

			final Map<String, TypeSpec.Builder> appObjects = new HashMap<>();

			// type parsing loop
			for (ApiType type : module.types()) {
				switch (type) {
					case EnumOfConsts en -> {
						var jEnum = new SdkEnum(en.name(),
						                        en.enum_consts(),
						                        new SdkDocs(en.summary(), en.description()));
						typeLibrary.put(new SdkType(moduleCapitalName, en.name()), jEnum);
					}
					case StructType struct -> {
						boolean isParams = false;
						boolean isResult = false;
						if (struct.name().length() >= 8) {
							isParams = "ParamsOf".equals(struct.name().substring(0, 8));
							isResult = "ResultOf".equals(struct.name().substring(0, 8));
						}
						boolean isSimpleWrapper = struct.struct_fields().length == 1;
						typeLibrary.put(new SdkType(moduleCapitalName, struct.name()), new SdkRecord(struct,
						                                                                             struct.name(),
						                                                                             null,
						                                                                             isSimpleWrapper,
						                                                                             isParams,
						                                                                             isResult,
						                                                                             typeLibrary));
					}
					case EnumOfTypes eot -> {
						List<SdkRecord> records = new ArrayList<>();
						// patch for 'Abi' interface that messes with module with the same name
						String interfaceName = "Abi".equals(eot.name()) ? "ABI" : eot.name();
						boolean isAppObject = Strings.safeSubstrEquals(interfaceName, 0, 11, false, "ParamsOfApp") ||
						              Strings.safeSubstrEquals(interfaceName, 0, 11, false, "ResultOfApp");
						String appObjectName = "";
						if (isAppObject) {
							appObjectName = Strings.substr(interfaceName,8);
							if (!appObjects.containsKey(appObjectName)) {
								logger.log(System.Logger.Level.WARNING,appObjectName);
								appObjects.put(appObjectName,TypeSpec.interfaceBuilder(appObjectName));
							} else {
								logger.log(System.Logger.Level.WARNING,"Has appObject: " + appObjectName);
							}
						}
						for (ApiType eotChildType : eot.enum_types()) {
							switch (eotChildType) {
//								case StructType str when isAppObject -> {
//									var appObjectBuilder = appObjects.get(appObjectName);
//									//if (interfaceName.equals("ParamsOf" + appObjectName)) {
//										//var aoMethodBuilder = MethodSpec.methodBuilder(eotChildType.name());
//										//aoMethodBuilder.
//										//appObjectBuilder.addMethod(aoMethodBuilder.build());
//									//}
////									records.add(SdkRecord.ofApiType(str,
////									                                typeLibrary,
////									                                new SdkInterfaceParent(
////											                                moduleCapitalName,
////											                                interfaceName,
////											                                str.name())));
//								}
								case StructType str -> records.add(SdkRecord.ofApiType(str,
								                                                       typeLibrary,
								                                                       new SdkInterfaceParent(
										                                                       moduleCapitalName,
										                                                       interfaceName,
										                                                       str.name())));
								case RefType ref -> eotReferences.put(TypeReference.fromApiType(ref).toSdkType(),
								                                      new SdkInterfaceParent(moduleCapitalName,
								                                                             interfaceName,
								                                                             ref.name()));
								default -> throw new IllegalStateException(
										"Unexpected value: " + eotChildType);
							}
						}
						//if (!isAppObject) {
							var javaInterface = new SdkInterface(eot,
							                                     interfaceName,
							                                     new SdkDocs(eot.summary(), eot.description()),
							                                     records);
							typeLibrary.put(new SdkType(moduleCapitalName, interfaceName), javaInterface);
						//}
					}
					default ->  typeLibrary.put(new SdkType(moduleCapitalName, type.name()), new SdkDummy(type));
				}
			}
		}

		// add references from EOT interfaces
		eotReferences.forEach((typ, sup) ->
				                      Objs.notNullDo(
						                      typeLibrary.get(typ),
						                      jtype -> typeLibrary.put(typ,
						                                               ((SdkRecord) jtype).withSuperInterface(sup))
				                      ));

		// main file building loop
		// loops modules again, now to write them
		for (var module : parsedApiReference.modules()) {
			if ("debot".equals(module.name())) {
				continue;
			}
			final TypeSpec.Builder moduleBuilder = moduleToBuilder(module,
			                                                       ParserUtils.capitalize(module.name()),
			                                                       apiVersion);
			String moduleCapitalName = ParserUtils.capitalize(module.name());

			// let's find types that correspond to module in library
			var types = typeLibrary.entrySet().stream().filter(
					entry -> moduleCapitalName.equals(entry.getKey().module())

			).map(Map.Entry::getValue).toList();

			eotReferences.forEach((typ, sup) -> {
			});

			// let's write these types
			for (SdkObject typeRef : types) {
				var builder = typeRef.poeticize();
				if (!Objects.isNull(builder)) {
					moduleBuilder.addType(builder.build());
				}
			}

			// function writing loop
			// functions receive full lib info
			for (ApiFunction function : module.functions()) {
				moduleBuilder.addMethod(new SdkFunction(module.name().toLowerCase(),
				                                        function,
				                                        typeLibrary).poeticize()
				                                                    .build());
			}

			// file writing loop
			JavaFile javaFile = JavaFile
					.builder("tech.deplant.java4ever.binding", moduleBuilder.build())
					.build();
			javaFile.writeTo(Paths.get("src/gen/java"));
		}
	}

	public static TypeSpec.Builder moduleToBuilder(ApiModule module, String moduleNameCapitalized, String version) {
		return TypeSpec
				.classBuilder(moduleNameCapitalized)
				.addJavadoc(moduleDocs(module, moduleNameCapitalized, version).build())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
	}

	public static CodeBlock.Builder moduleDocs(ApiModule module, String moduleNameCapitalized, String version) {
		return CodeBlock
				.builder()
				.add(String.format("""
									<strong>%s</strong>
									Contains methods of "%s" module of EVER-SDK API
									<p>
									%s %s
									@version %s
									""",
				                   moduleNameCapitalized,
				                   module.name(),
				                   requireNonNullElse(module.summary(), ""),
				                   requireNonNullElse(module.description(), ""),
				                   version));
	}

	public record SdkType(String module, String name) {
	}

	public record SdkInterfaceParent(String module, String name, String variantName) {
	}

}
