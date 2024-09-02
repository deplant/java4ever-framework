package tech.deplant.java4ever.binding.gql;

public record TransactionActionFilter(StringFilter action_list_hash, IntFilter msgs_created,
    BooleanFilter no_funds, IntFilter result_arg, IntFilter result_code, IntFilter skipped_actions,
    IntFilter spec_actions, IntFilter status_change,
    AccountStatusChangeEnumFilter status_change_name, BooleanFilter success, IntFilter tot_actions,
    StringFilter total_action_fees, StringFilter total_fwd_fees, FloatFilter total_msg_size_bits,
    FloatFilter total_msg_size_cells, BooleanFilter valid, TransactionActionFilter OR) {
}
