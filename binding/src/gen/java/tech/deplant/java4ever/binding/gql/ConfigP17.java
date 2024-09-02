package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

/**
 * Validator stake parameters;
 */
public record ConfigP17(String max_stake, Float max_stake_factor, String min_stake,
    String min_total_stake) {
  public static QueryExecutorBuilder max_stake(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("max_stake", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder min_stake(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("min_stake", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder min_total_stake(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("min_total_stake", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
