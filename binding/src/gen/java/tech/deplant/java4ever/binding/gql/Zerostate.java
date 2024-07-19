package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * The initial state of the workchain before first block was generated;
 */
public record Zerostate(String id, List<ZerostateAccounts> accounts, String boc, String file_hash,
    Integer global_id, List<ZerostateLibraries> libraries, ZerostateMaster master, String root_hash,
    String total_balance, List<OtherCurrency> total_balance_other, Integer workchain_id) {
  public static QueryExecutorBuilder total_balance(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_balance", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
