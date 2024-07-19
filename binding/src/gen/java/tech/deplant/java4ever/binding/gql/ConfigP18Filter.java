package tech.deplant.java4ever.binding.gql;

public record ConfigP18Filter(StringFilter bit_price_ps, StringFilter cell_price_ps,
    StringFilter mc_bit_price_ps, StringFilter mc_cell_price_ps, FloatFilter utime_since,
    ConfigP18Filter OR) {
}
