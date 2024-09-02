package tech.deplant.java4ever.binding.gql;

public record OutMsgFilter(StringFilter import_block_lt, InMsgFilter imported,
    StringFilter msg_env_hash, StringFilter msg_id, IntFilter msg_type,
    OutMsgTypeEnumFilter msg_type_name, StringFilter next_addr_pfx, IntFilter next_workchain,
    MsgEnvelopeFilter out_msg, InMsgFilter reimport, StringFilter transaction_id, OutMsgFilter OR) {
}
