package tech.deplant.java4ever.binding.gql;

public record TransactionBounceFilter(IntFilter bounce_type, BounceTypeEnumFilter bounce_type_name,
    StringFilter fwd_fees, StringFilter msg_fees, FloatFilter msg_size_bits,
    FloatFilter msg_size_cells, StringFilter req_fwd_fees, TransactionBounceFilter OR) {
}
