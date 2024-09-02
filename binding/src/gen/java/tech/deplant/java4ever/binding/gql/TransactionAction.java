package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record TransactionAction(String action_list_hash, Integer msgs_created, Boolean no_funds,
    Integer result_arg, Integer result_code, Integer skipped_actions, Integer spec_actions,
    Integer status_change, AccountStatusChangeEnum status_change_name, Boolean success,
    Integer tot_actions, String total_action_fees, String total_fwd_fees, Float total_msg_size_bits,
    Float total_msg_size_cells, Boolean valid) {
  public static QueryExecutorBuilder total_action_fees(String objectFieldsTree,
      BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_action_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder total_fwd_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("total_fwd_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
