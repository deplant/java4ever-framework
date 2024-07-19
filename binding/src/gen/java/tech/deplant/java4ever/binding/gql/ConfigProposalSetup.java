package tech.deplant.java4ever.binding.gql;

public record ConfigProposalSetup(Float bit_price, Float cell_price, Integer max_losses,
    Float max_store_sec, Integer max_tot_rounds, Float min_store_sec, Integer min_tot_rounds,
    Integer min_wins) {
}
