package tech.deplant.java4ever.binding.gql;

public record ConfigP42Filter(ConfigP42PayoutsArrayFilter payouts, StringFilter threshold,
    ConfigP42Filter OR) {
}
