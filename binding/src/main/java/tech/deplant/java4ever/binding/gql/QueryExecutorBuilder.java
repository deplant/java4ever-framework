package tech.deplant.java4ever.binding.gql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import tech.deplant.java4ever.binding.JsonContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryExecutorBuilder {

	String method;

	String fields;

	Map<String, String> args = new HashMap<>();

	public QueryExecutorBuilder(String method, String fields) {
		this.method = method;
		this.fields = fields;
	}

	public <T> void addToQuery(String name, T someValue) {
		var mapper = JsonContext.ABI_JSON_MAPPER()
		                        .configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false)
		                        .setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			this.args.put(name, mapper.writeValueAsString(someValue));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	public String toGraphQLQuery() {

		var filters = this.args.entrySet()
		                       .stream()
		                       .map(entrySet -> entrySet.getKey() + ": " + entrySet.getValue())
		                       .collect(Collectors.joining(", "));

		return """
				%s( %s ){
				  %s
				}
				""".formatted(this.method, filters, this.fields);
	}

}
