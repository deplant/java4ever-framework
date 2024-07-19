package tech.deplant.java4ever.binding.gql;

public record ConfigP39Filter(StringFilter adnl_addr, StringFilter map_key, FloatFilter seqno,
    StringFilter signature_r, StringFilter signature_s, StringFilter temp_public_key,
    FloatFilter valid_until, ConfigP39Filter OR) {
}
