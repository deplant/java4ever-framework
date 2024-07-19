package tech.deplant.java4ever.binding.gql;

public record ConfigP42PayoutsFilter(IntFilter license_type, IntFilter payout_percent,
    ConfigP42PayoutsFilter OR) {
}
