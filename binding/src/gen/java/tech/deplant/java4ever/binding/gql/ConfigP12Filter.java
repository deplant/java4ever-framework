package tech.deplant.java4ever.binding.gql;

public record ConfigP12Filter(BooleanFilter accept_msgs, BooleanFilter active,
    IntFilter actual_min_split, IntFilter addr_len_step, BooleanFilter basic,
    FloatFilter enabled_since, IntFilter flags, IntFilter max_addr_len, IntFilter max_split,
    IntFilter min_addr_len, IntFilter min_split, FloatFilter version, StringFilter vm_mode,
    IntFilter vm_version, IntFilter workchain_id, FloatFilter workchain_type_id,
    StringFilter zerostate_file_hash, StringFilter zerostate_root_hash, ConfigP12Filter OR) {
}
