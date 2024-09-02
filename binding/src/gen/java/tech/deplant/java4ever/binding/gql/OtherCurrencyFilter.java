package tech.deplant.java4ever.binding.gql;

public record OtherCurrencyFilter(FloatFilter currency, StringFilter value,
    OtherCurrencyFilter OR) {
}
