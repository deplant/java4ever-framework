package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

/**
 * Block create fees;
 */
public record ConfigP14(String basechain_block_fee, String masterchain_block_fee) {
  public static QueryExecutorBuilder basechain_block_fee(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("basechain_block_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder masterchain_block_fee(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("masterchain_block_fee", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
