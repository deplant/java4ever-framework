package tech.deplant.java4ever.binding.gql;

/**
 * Specify how to sort results.
     * You can sort documents in result set using more than one field.;
 */
public record QueryOrderBy(String path, QueryOrderByDirection direction) {
}
