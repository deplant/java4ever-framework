package tech.deplant.java4ever.binding.gql;

public record ConfigP12(Boolean accept_msgs, Boolean active, Integer actual_min_split,
    Integer addr_len_step, Boolean basic, Float enabled_since, Integer flags, Integer max_addr_len,
    Integer max_split, Integer min_addr_len, Integer min_split, Float version, String vm_mode,
    Integer vm_version, Integer workchain_id, Float workchain_type_id, String zerostate_file_hash,
    String zerostate_root_hash) {
}
