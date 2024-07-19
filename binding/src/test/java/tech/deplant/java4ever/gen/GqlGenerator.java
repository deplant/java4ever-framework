package tech.deplant.java4ever.gen;

import tech.deplant.java4ever.binding.generator.ParserEngine;
import  tech.deplant.java4ever.binding.generator.ParseGql;
import tech.deplant.java4ever.binding.generator.reference.ApiReference;

import java.io.IOException;

public class GqlGenerator {
	public static void main(String[] args) {
		ApiReference apiReference = null;
		try {
			//ParseGql.updateGqlSchemaInfo(ParseGql.getSchemaFromGraphQL("codegen/generator-config.json"));
			ParseGql.generateFromSchema("gql.json");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
