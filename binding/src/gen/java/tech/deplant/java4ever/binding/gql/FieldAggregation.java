package tech.deplant.java4ever.binding.gql;

/**
 * Specify field and aggregation function used to collect aggregated value;
 */
public record FieldAggregation(String field, AggregationFn fn) {
}
