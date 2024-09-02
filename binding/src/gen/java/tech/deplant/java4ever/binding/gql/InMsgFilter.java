package tech.deplant.java4ever.binding.gql;

public record InMsgFilter(StringFilter fwd_fee, StringFilter ihr_fee, MsgEnvelopeFilter in_msg,
    StringFilter msg_id, IntFilter msg_type, InMsgTypeEnumFilter msg_type_name,
    MsgEnvelopeFilter out_msg, StringFilter proof_created, StringFilter proof_delivered,
    StringFilter transaction_id, StringFilter transit_fee, InMsgFilter OR) {
}
