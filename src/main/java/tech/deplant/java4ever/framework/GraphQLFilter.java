package tech.deplant.java4ever.framework;

import lombok.Value;

public interface GraphQLFilter {
	record In(String[] in) implements GraphQLFilter {
	}

	record Eq(Integer eq) implements GraphQLFilter {
	}

	record Gt(String gt) implements GraphQLFilter {
	}

	record Lt(String lt) implements GraphQLFilter {
	}
}