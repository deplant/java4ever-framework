package tech.deplant.java4ever.binding.gql;

public record ConfigProposalSetupFilter(FloatFilter bit_price, FloatFilter cell_price,
    IntFilter max_losses, FloatFilter max_store_sec, IntFilter max_tot_rounds,
    FloatFilter min_store_sec, IntFilter min_tot_rounds, IntFilter min_wins,
    ConfigProposalSetupFilter OR) {
}
