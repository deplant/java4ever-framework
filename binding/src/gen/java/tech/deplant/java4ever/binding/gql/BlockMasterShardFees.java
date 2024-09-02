package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record BlockMasterShardFees(String create, List<OtherCurrency> create_other, String fees,
    List<OtherCurrency> fees_other, String shard, Integer workchain_id) {
  public static QueryExecutorBuilder create(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("create", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
