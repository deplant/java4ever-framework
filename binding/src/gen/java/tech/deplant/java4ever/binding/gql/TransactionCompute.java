package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.Optional;

public record TransactionCompute(Boolean account_activated, Integer compute_type,
    ComputeTypeEnum compute_type_name, Integer exit_arg, Integer exit_code, Integer gas_credit,
    String gas_fees, String gas_limit, String gas_used, Integer mode, Boolean msg_state_used,
    Integer skipped_reason, SkipReasonEnum skipped_reason_name, Boolean success,
    String vm_final_state_hash, String vm_init_state_hash, Float vm_steps) {
  public static QueryExecutorBuilder gas_fees(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_fees", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gas_limit(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_limit", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder gas_used(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("gas_used", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
