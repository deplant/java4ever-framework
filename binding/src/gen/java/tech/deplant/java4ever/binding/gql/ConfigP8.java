package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

/**
 * Global version;
 */
public record ConfigP8(String capabilities, Float version) {
  public static QueryExecutorBuilder capabilities(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("capabilities", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
