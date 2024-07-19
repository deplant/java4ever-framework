package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record ZerostateMaster(Config config, String config_addr, String global_balance,
    List<OtherCurrency> global_balance_other, Float validator_list_hash_short) {
  public static QueryExecutorBuilder global_balance(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("global_balance", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
