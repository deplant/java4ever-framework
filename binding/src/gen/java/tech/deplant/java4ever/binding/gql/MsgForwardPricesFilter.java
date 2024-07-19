package tech.deplant.java4ever.binding.gql;

public record MsgForwardPricesFilter(StringFilter bit_price, StringFilter cell_price,
    IntFilter first_frac, FloatFilter ihr_price_factor, StringFilter lump_price,
    IntFilter next_frac, MsgForwardPricesFilter OR) {
}
