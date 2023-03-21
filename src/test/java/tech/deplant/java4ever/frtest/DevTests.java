package tech.deplant.java4ever.frtest;

import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.frtest.unit.AbiTests;
import tech.deplant.java4ever.utils.Objs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class DevTests {

	private static System.Logger logger = System.getLogger(DevTests.class.getName());

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
	public void get_graphql_schema() throws IOException, InterruptedException {

		// create client
		// query gql

		String queryParams = """
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

		String result = httpRequest("http://185.20.226.96/graphql",queryParams,
		"POST","Accept: application/json","Content-Type: application/json");

		logger.log(System.Logger.Level.INFO,result);
	}
}
