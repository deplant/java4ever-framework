package tech.deplant.java4ever.binding.gql;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.JsonContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public record HttpRequestHandle(HttpClient client, HttpRequest request) {

	private final static System.Logger logger = System.getLogger(HttpRequestHandle.class.getName());

	public static HttpRequestHandle ofGraphQL(String urlString, String body) throws JsonProcessingException {
		return ofGraphQL(urlString, body, HttpClient.Version.HTTP_1_1);
	}

	public static HttpRequestHandle ofGraphQL(String urlString, String gqlQuery, HttpClient.Version version) throws JsonProcessingException {
		String body = "{\"query\": " + JsonContext.ABI_JSON_MAPPER().writeValueAsString("{" + gqlQuery + "}") + " }";
		logger.log(System.Logger.Level.DEBUG, body);
		return new HttpRequestHandle(HttpClient.newHttpClient(), HttpRequest.newBuilder()
		                                                                    .uri(URI.create(urlString))
		                                                                    .POST(HttpRequest.BodyPublishers.ofString(
				                                                                    body))
		                                                                    .version(version)
		                                                                    .header("Content-Type", "application/json")
		                                                                    .header("Accept", "application/json")
		                                                                    .header("Accept-Encoding", "gzip, deflate")
		                                                                    .build());
	}

	public HttpResponse<String> send() throws IOException, InterruptedException {
		return client().send(request(), HttpResponse.BodyHandlers.ofString());
	}
}
