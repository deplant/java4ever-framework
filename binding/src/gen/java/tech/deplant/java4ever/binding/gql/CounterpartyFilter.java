package tech.deplant.java4ever.binding.gql;

public record CounterpartyFilter(StringFilter account, StringFilter counterparty,
    FloatFilter last_message_at, StringFilter last_message_id,
    BooleanFilter last_message_is_reverse, StringFilter last_message_value) {
}
