package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.gql.QueryExecutorBuilder;
import tech.deplant.java4ever.binding.io.JsonResource;
import tech.deplant.java4ever.binding.reference.gql.GqlSchemaRoot;
import tech.deplant.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParseGql {

	public static final ObjectMapper MAPPER = JsonContext.ABI_JSON_MAPPER();

	public static void generateFromSchema(String jsonPath) throws IOException {
		var schema = MAPPER.readValue(new JsonResource(jsonPath).get(), GqlSchemaRoot.class).data().__schema();

		TypeSpec.Builder subscriptionClass = TypeSpec.classBuilder("Subscription")
		                                             .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

		TypeSpec.Builder mutationClass = TypeSpec.classBuilder("Mutation")
		                                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

		// OBJECT kind
		for (var type : schema.types()
		                      .stream()
		                      .filter(type -> type.kind().equals(GqlSchemaRoot.GqlKind.OBJECT) ||
		                                      type.kind().equals(GqlSchemaRoot.GqlKind.INPUT_OBJECT) ||
		                                      type.kind().equals(GqlSchemaRoot.GqlKind.INTERFACE) ||
		                                      type.kind().equals(GqlSchemaRoot.GqlKind.ENUM))
		                      .filter(type -> !type.name().startsWith("__"))
		                      .toList()) {
			switch (type) {
				case GqlSchemaRoot.GqlType.GqlObject obj -> {
					if (obj.interfaces().isEmpty()) {
						var recordBuilder = processObjects(obj);
						JavaFile.builder("tech.deplant.java4ever.binding.gql", recordBuilder.build())
						        .build()
						        .writeTo(Paths.get("src/gen/java"));
					}
				}
				case GqlSchemaRoot.GqlType.GqlInputObject input -> {
					var recordBuilder = processObjects(input);
					JavaFile.builder("tech.deplant.java4ever.binding.gql", recordBuilder.build())
					        .build()
					        .writeTo(Paths.get("src/gen/java"));
				}
				case GqlSchemaRoot.GqlType.GqlInterface(
						var kind, String intName, String intDescription, var possibleTypes
				) -> {
					TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(intName)
					                                            .addModifiers(Modifier.PUBLIC, Modifier.SEALED);

					Optional.ofNullable(intDescription)
					        .ifPresent(description -> interfaceBuilder.addJavadoc(CodeBlock.builder()
					                                                                       .addStatement(intDescription)
					                                                                       .build()));

					// let's find OBJECTs that have interfaces[] field containing this interface
					for (var dependentRecord : schema.types()
					                                 .stream()
					                                 .filter(type1 -> type1.kind().equals(GqlSchemaRoot.GqlKind.OBJECT))
					                                 .map(type2 -> (GqlSchemaRoot.GqlType.GqlObject) type2)
					                                 .filter(obj1 -> obj1.interfaces()
					                                                     .stream()
					                                                     .anyMatch(depInt -> depInt.name()
					                                                                               .equals(intName)))
					                                 .toList()) {

						var dependentRecordBuilder = processObjects(dependentRecord);
						dependentRecordBuilder.addSuperinterface(ClassName.get(
								"tech.deplant.java4ever.binding.gql",
								intName));
						interfaceBuilder.addType(dependentRecordBuilder.build());
					}

					JavaFile.builder("tech.deplant.java4ever.binding.gql", interfaceBuilder.build())
					        .build()
					        .writeTo(Paths.get("src/gen/java"));
				}
				case GqlSchemaRoot.GqlType.GqlEnum enu -> {
					TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(enu.name()).addModifiers(Modifier.PUBLIC);

					Optional.ofNullable(enu.description())
					        .ifPresent(description -> enumBuilder.addJavadoc(CodeBlock.builder()
					                                                                  .addStatement(description)
					                                                                  .build()));
					for (var enumValue : enu.enumValues()) {
						enumBuilder.addEnumConstant(enumValue.name());
					}

					JavaFile.builder("tech.deplant.java4ever.binding.gql", enumBuilder.build())
					        .build()
					        .writeTo(Paths.get("src/gen/java"));
				}
				default -> {
				}
			}

		}

	}

	private static TypeSpec.Builder processObjects(GqlSchemaRoot.GqlType.GqlInputObject input) {

		TypeSpec.Builder recordBuilder = TypeSpec.recordBuilder(input.name()).addModifiers(Modifier.PUBLIC);

		Optional.ofNullable(input.description())
		        .ifPresent(description -> recordBuilder.addJavadoc(CodeBlock.builder()
		                                                                    .addStatement(description)
		                                                                    .build()));

		for (var field : input.inputFields()) {
			recordBuilder.addRecordComponent(ParserUtils.processReservedNames(getClassName(field.type()), field.name())
			                                            .build());
		}
		return recordBuilder;
	}

	private static TypeSpec.Builder processObjects(GqlSchemaRoot.GqlType.GqlObject obj) throws IOException {

		TypeSpec.Builder recordBuilder = TypeSpec.recordBuilder(obj.name()).addModifiers(Modifier.PUBLIC);

		Optional.ofNullable(obj.description())
		        .ifPresent(description -> recordBuilder.addJavadoc(CodeBlock.builder()
		                                                                    .addStatement(description)
		                                                                    .build()));


		// for executors
		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(obj.name() + "Executor");

		for (var field : obj.fields()) {
			recordBuilder.addRecordComponent(ParserUtils.processReservedNames(getClassName(field.type()), field.name())
			                                            .build());
			if (!field.args().isEmpty()) {
				var queryExecutorBuilderClass = ClassName.get(QueryExecutorBuilder.class);
				var functionBuilder = MethodSpec.methodBuilder(field.name())
				                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				                                .returns(queryExecutorBuilderClass);


				functionBuilder.addCode(CodeBlock.builder()
				                                 .addStatement("var builder = new $T($S, objectFieldsTree)",
				                                               queryExecutorBuilderClass,
				                                               field.name())
				                                 .build());

				functionBuilder.addParameter(ParameterSpec.builder(ClassName.STRING, "objectFieldsTree").build());

				for (var arg : field.args()) {
					functionBuilder.addParameter(ParserUtils.processReservedNames(getClassName(arg.type()),
					                                                              arg.name())
					                                        .build());
					functionBuilder.addCode(CodeBlock.builder()
					                                 .addStatement("$T.ofNullable(" + arg.name() +
					                                               ").ifPresent(ar -> builder.addToQuery($S,ar))",
					                                               ClassName.get(Optional.class),
					                                               arg.name())
					                                 .build());
				}


				functionBuilder.addCode(CodeBlock.builder().addStatement("return builder").build());

				if (obj.name().equals("Query") || obj.name().equals("Mutation") || obj.name().equals("Subscription")) {
					classBuilder.addMethod(functionBuilder.build());
				} else {
					recordBuilder.addMethod(functionBuilder.build());
				}

			}
		}

		if (obj.name().equals("Query") || obj.name().equals("Mutation") || obj.name().equals("Subscription")) {

			JavaFile.builder("tech.deplant.java4ever.binding.gql", classBuilder.build())
			        .build()
			        .writeTo(Paths.get("src/gen/java"));
		}

		return recordBuilder;
	}

	private static TypeName getClassName(GqlSchemaRoot.GqlParam param) {
		return switch (param.kind()) {
			case "LIST" -> ParameterizedTypeName.get(ClassName.get(List.class), getClassName(param.ofType()));
			case "NON_NULL" -> getClassName(param.ofType());
			case "SCALAR" -> {
				final String typeName = switch (param.name()) {
					case "Int" -> "Integer";
					case "ID", "RempReceiptJson" -> "String";
					default -> param.name();
				};
				yield ClassName.bestGuess(typeName);
			}
			default -> List.of("BlockchainAccount", "BlockchainBlock", "BlockchainMessage", "BlockchainTransaction")
			               .contains(param.name()) ? ClassName.bestGuess("Node." + param.name()) : ClassName.bestGuess(
					param.name());
		};
	}

	private static String httpRequest(final String url,
	                                  final String query,
	                                  final String method,
	                                  final String... headers) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder()
		                              .version(HttpClient.Version.HTTP_1_1)
		                              .connectTimeout(Duration.ofSeconds(15))
		                              .followRedirects(HttpClient.Redirect.NEVER)
		                              .build();
		HttpResponse<String> response = null;
		final Map<String, String> headersChecked;

		if (method.equals("GET")) {

			final var builder = HttpRequest.newBuilder().GET().uri(URI.create(url));

			//headersChecked = Map.of("Accept", "application/json");

			Arrays.stream(headers).toList().forEach(header -> {
				final var nameValue = header.split(":");
				builder.header(nameValue[0].trim(), nameValue[1].trim());
			});
			final HttpRequest request = builder.build();

			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} else if (method.equals("POST")) {
			client = HttpClient.newHttpClient();

			final var builder = HttpRequest.newBuilder()
			                               .uri(URI.create(url))
			                               .POST(HttpRequest.BodyPublishers.ofString(query))
			                               .version(HttpClient.Version.HTTP_1_1);

			Arrays.stream(headers).toList().forEach(header -> {
				final var nameValue = header.split(":");
				builder.header(nameValue[0].trim(), nameValue[1].trim());
			});

			final HttpRequest request = builder
					//.header("Content-Type", "application/json")
					//.header("Accept", "application/json")
					//.header("Accept-Encoding","gzip, deflate")
					.build();
			//sync
			//logger.log(System.Logger.Level.INFO, () -> "HTTP Request: " + request.toString());
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		}
		if (Objs.isNull(response.statusCode()) || response.statusCode() != 200) {
			throw new RuntimeException("Failed : HTTP Error code : " + response.statusCode());
		}
		return response.body();
	}

	public static String getSchemaFromGraphQL(String endpoint) throws IOException, InterruptedException {

		// create client
		// query gql

		String introspectionQueryText = """
				{
					"operationName": "IntrospectionQuery",
					"variables": {},
					"query": "query IntrospectionQuery {
							             __schema {
							               queryType {
							                 name
							               }
							               mutationType {
							                 name
							               }
							               subscriptionType {
							                 name
							               }
							               types {
							                 ...FullType
							               }
							               directives {
							                 name
							                 description
							                 locations
							                 args {
							                   ...InputValue
							                 }
							               }
							             }
							           }
							           fragment FullType on __Type {
							             kind
							             name
							             description
							             fields(includeDeprecated: true) {
							               name
							               description
							               args {
							                 ...InputValue
							               }
							               type {
							                 ...TypeRef
							               }
							               isDeprecated
							               deprecationReason
							             }
							             inputFields {
							               ...InputValue
							             }
							             interfaces {
							               ...TypeRef
							             }
							             enumValues(includeDeprecated: true) {
							               name
							               description
							               isDeprecated
							               deprecationReason
							             }
							             possibleTypes {
							               ...TypeRef
							             }
							           }
							           fragment InputValue on __InputValue {
							             name
							             description
							             type {
							               ...TypeRef
							             }
							             defaultValue
							           }
							           fragment TypeRef on __Type {
							             kind
							             name
							             ofType {
							               kind
							               name
							               ofType {
							                 kind
							                 name
							                 ofType {
							                   kind
							                   name
							                   ofType {
							                     kind
							                     name
							                     ofType {
							                       kind
							                       name
							                       ofType {
							                         kind
							                         name
							                         ofType {
							                           kind
							                           name
							                         }
							                       }
							                     }
							                   }
							                 }
							               }
							             }
							           }"
				}
				""";
		final var jsonRequest = "{\"query\":" + MAPPER.writeValueAsString(
				"{messages(filter: {msg_type: {eq:2}}) { id body boc src created_at }}") + "}";
		return httpRequest(endpoint,
		                   introspectionQueryText,
		                   "POST",
		                   "Accept: application/json",
		                   "Content-Type: application/json");
	}
}
