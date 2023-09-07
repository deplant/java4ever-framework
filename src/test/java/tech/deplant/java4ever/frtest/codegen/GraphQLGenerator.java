package tech.deplant.java4ever.frtest.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.generator.ContractWrapper;
import tech.deplant.java4ever.framework.generator.GeneratorConfig;
import tech.deplant.java4ever.frtest.DevTests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

public class GraphQLGenerator {

	private static System.Logger logger = System.getLogger(GraphQLGenerator.class.getName());

	public static void main(String[] args) {
		try {
			//updateGqlSchemaInfo(getSchemaFromGraphQL("codegen/generator-config.json"));
			generateFromSchema("codegen/gql.json");

		} catch (IOException /*| InterruptedException*/ e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGenerate() {
		try {
			//updateGqlSchemaInfo(getSchemaFromGraphQL("codegen/generator-config.json"));
			generateFromSchema("codegen/gql.json");

		} catch (IOException /*| InterruptedException*/ e) {
			throw new RuntimeException(e);
		}
	}

	private static void generateFromSchema(String jsonPath) throws JsonProcessingException {
		var mapper = JsonContext.ABI_JSON_MAPPER();
		var schema = mapper.readValue(new JsonResource(jsonPath).get(), GqlSchemaRoot.class);
	}

	private static void updateGqlSchemaInfo(String schemaFromGraphQL) {
		// update gql.json file with schema JSON
	}

	private static String httpRequest(final String url,
	                                  final String query,
	                                  final String method,
	                                  final String... headers) throws IOException, InterruptedException {

		//String uriString = url + URLEncoder.encode(query, StandardCharsets.UTF_8);
		//System.out.println(uriString);
		//String uriString = /*url + */URLEncoder.encode(query, StandardCharsets.UTF_8);
		HttpClient client = HttpClient.newBuilder()
		                              .version(HttpClient.Version.HTTP_1_1)
		                              .connectTimeout(Duration.ofSeconds(15))
		                              .followRedirects(HttpClient.Redirect.NEVER)
		                              .build();
		HttpResponse<String> response = null;
		final Map<String, String> headersChecked;

		if (method.equals("GET")) {

			final var builder = HttpRequest.newBuilder()
			                               .GET()
			                               .uri(URI.create(url));

			//headersChecked = Map.of("Accept", "application/json");

			Arrays.stream(headers).toList().forEach(header -> {
				final var nameValue = header.split(":");
				builder.header(nameValue[0].trim(), nameValue[1].trim());
			});
			final HttpRequest request = builder.build();

			response = client.send(
					request,
					HttpResponse.BodyHandlers.ofString()
			);
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
			logger.log(System.Logger.Level.INFO, () -> "HTTP Request: " + request.toString());
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		}
		if (Objs.isNull(response.statusCode()) || response.statusCode() != 200) {
			throw new RuntimeException("Failed : HTTP Error code : " + response.statusCode());
		}
		return response.body();
	}


	@Test
	@Disabled
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
		final var jsonRequest = "{\"query\":" + SDK_EMPTY.context().mapper().writeValueAsString("{messages(filter: {msg_type: {eq:2}}) { id body boc src created_at }}") + "}";
		String result1 = httpRequest(endpoint, jsonRequest,
		                             "POST","Accept: application/json","Content-Type: application/json");

		String result = httpRequest(endpoint,introspectionQueryText,
		                            "POST","Accept: application/json","Content-Type: application/json");

		logger.log(System.Logger.Level.INFO,result);

		return result;
	}

}
