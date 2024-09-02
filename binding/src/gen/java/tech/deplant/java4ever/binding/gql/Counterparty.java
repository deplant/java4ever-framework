package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

/**
 * Counterparty;
 */
public record Counterparty(String account, String counterparty, Float last_message_at,
    String last_message_id, Boolean last_message_is_reverse, String last_message_value,
    String cursor) {
  public static QueryExecutorBuilder last_message_value(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("last_message_value", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
