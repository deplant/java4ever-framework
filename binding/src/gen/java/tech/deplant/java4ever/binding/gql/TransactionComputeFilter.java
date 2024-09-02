package tech.deplant.java4ever.binding.gql;

public record TransactionComputeFilter(BooleanFilter account_activated, IntFilter compute_type,
    ComputeTypeEnumFilter compute_type_name, IntFilter exit_arg, IntFilter exit_code,
    IntFilter gas_credit, StringFilter gas_fees, StringFilter gas_limit, StringFilter gas_used,
    IntFilter mode, BooleanFilter msg_state_used, IntFilter skipped_reason,
    SkipReasonEnumFilter skipped_reason_name, BooleanFilter success,
    StringFilter vm_final_state_hash, StringFilter vm_init_state_hash, FloatFilter vm_steps,
    TransactionComputeFilter OR) {
}
